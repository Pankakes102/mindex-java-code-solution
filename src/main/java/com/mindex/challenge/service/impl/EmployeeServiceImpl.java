package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Employee create(Employee employee) {
        LOG.debug("Creating employee [{}]", employee);

        employee.setEmployeeId(UUID.randomUUID().toString());
        employeeRepository.insert(employee);

        return employee;
    }

    @Override
    public Employee read(String id) {
        LOG.debug("Retrieving employee with id [{}]", id);

        return getEmployeeById(id);
    }

    @Override
    public Employee update(Employee employee) {
        LOG.debug("Updating employee [{}]", employee);

        return employeeRepository.save(employee);
    }

    @Override
    public ReportingStructure fetchReportingStructureForEmployee(String id) {
        final Employee employee = getEmployeeById(id);

        return new ReportingStructure(employee, getNumberOfReports(employee));
    }

    /**
     * This method will recursively fetch for all the Direct Reports on the provided Employee and cumulate the amount of
     * employees it has iterated over.
     *
     * @param employee employee to recursively fetch
     * @return total number of employees under the provided employee based on the Direct Reports List.
     */
    private int getNumberOfReports(Employee employee) {
        final List<Employee> currentReports = employee.getDirectReports();
        int numberOfReports = 0;

        if (currentReports != null && !currentReports.isEmpty()) {
            numberOfReports += currentReports.size();

            for (Employee reportingEmployee : currentReports) {
                final Employee currentEmployee = getEmployeeById(reportingEmployee.getEmployeeId());

                numberOfReports += getNumberOfReports(currentEmployee);
            }
        }

        return numberOfReports;
    }

    /**
     * This method will get an Employee by ID.
     *
     * @param id Employee ID to lookup.
     * @return The Employee object if one is found. if there does not exist an employee by the provided ID, a
     * RuntimeException will be thrown.
     */
    private Employee getEmployeeById(String id) {
        final Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        return employee;
    }
}
