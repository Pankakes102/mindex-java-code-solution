# Coding Challenge
## What's Provided
A simple [Spring Boot](https://projects.spring.io/spring-boot/) web application has been created and bootstrapped 
with data. The application contains information about all employees at a company. On application start-up, an in-memory 
Mongo database is bootstrapped with a serialized snapshot of the database. While the application runs, the data may be
accessed and mutated in the database without impacting the snapshot.

### How to Run
The application may be executed by running `gradlew bootRun`.

### How to Use
The following endpoints are available to use:
```
* CREATE
    * HTTP Method: POST 
    * URL: localhost:8080/employee
    * PAYLOAD: Employee
    * RESPONSE: Employee
* READ
    * HTTP Method: GET 
    * URL: localhost:8080/employee/{id}
    * RESPONSE: Employee
* UPDATE
    * HTTP Method: PUT 
    * URL: localhost:8080/employee/{id}
    * PAYLOAD: Employee
    * RESPONSE: Employee
```
The Employee has a JSON schema of:
```json
{
  "type":"Employee",
  "properties": {
    "employeeId": {
      "type": "string"
    },
    "firstName": {
      "type": "string"
    },
    "lastName": {
          "type": "string"
    },
    "position": {
          "type": "string"
    },
    "department": {
          "type": "string"
    },
    "directReports": {
      "type": "array",
      "items" : "string"
    }
  }
}
```
For all endpoints that require an "id" in the URL, this is the "employeeId" field.

## What to Implement
Clone or download the repository, do not fork it.

### Task 1
Create a new type, ReportingStructure, that has two properties: employee and numberOfReports.

For the field "numberOfReports", this should equal the total number of reports under a given employee. The number of 
reports is determined to be the number of directReports for an employee and all of their distinct reports. For example, 
given the following employee structure:
```
                    John Lennon
                /               \
         Paul McCartney         Ringo Starr
                               /        \
                          Pete Best     George Harrison
```
The numberOfReports for employee John Lennon (employeeId: 16a596ae-edd3-4847-99fe-c4518e82c86f) would be equal to 4. 

This new type should have a new REST endpoint created for it. This new endpoint should accept an employeeId and return 
the fully filled out ReportingStructure for the specified employeeId. The values should be computed on the fly and will 
not be persisted.

### Task 2
Create a new type, Compensation. A Compensation has the following fields: employee, salary, and effectiveDate. Create 
two new Compensation REST endpoints. One to create and one to read by employeeId. These should persist and query the 
Compensation from the persistence layer.

## Delivery
Please upload your results to a publicly accessible Git repo. Free ones are provided by Github and Bitbucket.

## Thoughts/Ideas
### Task 1
In preparation for Task 1, I had ran the application locally and entered the REST Endpoints into Insomnia so that I
could more easily re-run the same calls over an over for local testing.

Next I went through the code base and familiarized myself with how this application is set up and what to expect when I
implement my solution for this task.

One of the first things that I noticed in this project was that there are no Javadocs. Being that this is a Coding 
challenge, I will refrain from editing the existing code and adding any updates where i may see fit. 
Such as:
- adding in documentation to classes and methods. 
- Doing data validation within the existing service to check that the employee data is not empty or "invalid" or if an
employee already exists with such data. 
- Adding/Implementing any new frameworks to help with the data structure (i.e. JPA Hibernate, to help with the relationships and automatic data fetching for employee records).
- Updating the existing Controller to use @RequestMapping("/employee") in order to cut down on repetitive pathing for each endpoint.

#### Plan of attack
In order to complete this task, the instructions state that i must declare a new REST Endpoint and Type.
I was planning on creating a new controller specifically for the Reporting Structure, so that if there were any other
actions needed for a Reporting Structure they could be added there, however after looking at this, i think im going to
just append into the existing classes because the reporting structure cannot exist without employees, and it should be 
under "/employee/{id}/reporting-structure".
To achieve this i will need to start by creating the Type, then update the Service Interface, then implement the service
call and lastly expose the logic via the controller.

#### Afterthoughts
Per my plan of attack, I had altered the interface, service and controller. All under the guise/assumption that this
endpoint and per the wording of the question/requirements should "have a new REST endpoint created for it". Interpreting
that literally and only creating one Endpoint in an existing controller. 
However, after implementing this, and running some tests locally via Insomnia:
- Use Case 1, Run with Paul's eid, verify that there are 0.
- Use Case 2, Run with Ringo's eid, verify that there are 2.
- Use Case 3, Run with Jonh's eid, verify that there are 4.

I was going to hop over into the /test directory and add me some tests, and the way that the tests are set up, led me
to believe that I should Not in fact had appended to existing things and just went with my gut and create a new 
Controller and Service for this functionality. 
So I will commit these changes and then refactor accordingly. This should allow me to add in unit tests to verify that
the service itself is working and returning expected data, as well as the controller is returning the expected JSON.

One thing to note is that the Task Description states "return the fully filled out ReportingStructure". In my eyes, the 
Employee itself should have already of had the Direct Reporter Employees relationships filled out, the service layer
shouldn't have to do that, but in order to achieve that data displaying appropriately in the JSON response, I would have
had to of about doubled the computing time to clear and reset the Employee Lists after i fetched for the Employee Data. 
Whereas if this exercise was using JPA, there would not be a need to re-call the "getEmployeeById" for each employee, as
its data would already be on the fetched Employee object. 

Having some more explicit instructions or definitions as to what the "fully filled out Reporting Structure" Should have
been would be beneficial. However, im going to assume that it means that the employee we fetched for and the number is 
all that needs to be present, and that the list of Direct Reports does not also have to have all the employee
information attached to the employee its returning. 

#### Refactor Notes
Refactor went smooth, just moved the contents into new files. 
Creating the Unit test were a bit tricky, however. Being used to JUnit 5 and Mockito, I dove right in with @ExtendsWith
and the junit juipter api @Test annotation. I wrote a couple simple tests and when I went to run them, it said that 
gradle could not find any tests :). So I found out then that I was using the wrong @Test annotation. 
After updating it, both test failed because the mocking was throwing a Null Pointer Exception on the EmployeeRepository, 
indicating that there was an issue mocking/injecting via the @ExtendsWith. This lead met to do a quick google search, 
only to find that with JUnit 4, you have to use @RunWith, so I updated it. 
Then the test were failing because I was not able to inject a mock that was an interface. I tried to look up other solutions
for this issue, but I just defaulted to using the actual Implementation here instead, as that is in fact what the unit
tests are supposed to be testing. I would be very interested to learn if there was a way to design the tests like I did
and use Interfaces instead of the Implemented Class. 
The existing Employee Service Implementation Test confused me at first because it was not mocking anything out, and it 
was also making requests with the Rest Template. I would consider these full Integration Tests rather than Unit tests
I guess? Seeing that it did in fact spin up the entire Employee Service and Spring Boot Context. It was also a little
confusing as to why there was only one test for all 3 pieces of functionality on the service, but like I had mentioned 
before, im going to refrain from altering any of the existing code if possible. Wondering now if i should further refactor
my unit tests and append Unit in some form to the existing tests and then mimic what he other ServiceImpl is doing to
get better code coverage from a Rest Template standpoint. 

## Authors
Initial Commit Provided with the Mindex Java Code Challenge.
Michael Szczepanski