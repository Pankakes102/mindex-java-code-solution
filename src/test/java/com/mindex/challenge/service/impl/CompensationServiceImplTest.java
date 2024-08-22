package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * Unit/Integration Test for the Compensation Service.
 *
 * @author Michael Szczepanski
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImplTest {
    private String compensationUrl;
    private String compensationByEmployeeIdUrl;

    @Autowired
    private CompensationService compensationService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        compensationUrl = "http://localhost:" + port + "/compensation";
        compensationByEmployeeIdUrl = "http://localhost:" + port + "/compensation/{employeeId}";
    }

    @Test
    public void testCreateRead() {
        final Employee employee = new Employee();
        employee.setEmployeeId("16a596ae-edd3-4847-99fe-c4518e82c86f"); //John Lennon

        final Compensation compensation = new Compensation();

        compensation.setSalary(10_000);
        compensation.setEffectiveDate(new Date());
        compensation.setEmployee(employee);

        //Create checks
        final Compensation createdCompensation = restTemplate.postForEntity(compensationUrl, compensation, Compensation.class).getBody();

        Assertions.assertNotNull(createdCompensation);
        assertCompensationEquivalence(compensation, createdCompensation);

        //Read Checks
        final Compensation readCompensation = restTemplate.getForEntity(compensationByEmployeeIdUrl, Compensation.class, employee.getEmployeeId()).getBody();

        Assertions.assertNotNull(readCompensation);
        Assertions.assertEquals(createdCompensation.getCompensationId(), readCompensation.getCompensationId());
        assertCompensationEquivalence(createdCompensation, readCompensation);
    }

    private static void assertCompensationEquivalence(Compensation expected, Compensation actual) {
        Assertions.assertEquals(expected.getSalary(), actual.getSalary());
        Assertions.assertEquals(expected.getEffectiveDate(), actual.getEffectiveDate());
        Assertions.assertEquals(expected.getEmployee().getEmployeeId(), actual.getEmployee().getEmployeeId());
    }
}
