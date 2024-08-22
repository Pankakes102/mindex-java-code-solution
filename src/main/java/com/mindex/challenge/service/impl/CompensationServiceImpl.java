package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * This class implements and defines the logic for the Compensation Service.
 *
 * @author Michael Szczepanski
 */
@Service
public class CompensationServiceImpl implements CompensationService {
    private static final Logger LOG = LoggerFactory.getLogger(CompensationServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CompensationRepository compensationRepository;

    @Override
    public Compensation create(Compensation compensation) {
        LOG.debug("Creating compensation [{}]", compensation);

        final Employee compensationEmployee = compensation.getEmployee();

        if (compensationEmployee == null || compensationEmployee.getEmployeeId() == null) {
            throw new IllegalArgumentException("Employee ID must be provided in order to create Compensation.");
        }

        final Employee validEmployee = getEmployeeById(compensationEmployee.getEmployeeId());

        compensation.setEmployee(validEmployee);
        compensation.setCompensationId(UUID.randomUUID().toString());

        compensationRepository.insert(compensation);

        return compensation;
    }

    @Override
    public Compensation read(String employeeId) {
        if (employeeId == null) {
            throw new IllegalArgumentException("Cannot fetch Compensation for an invalid Employee ID.");
        }

        final Employee validEmployee = getEmployeeById(employeeId);

        final Compensation compensation = compensationRepository.findByEmployee(validEmployee);

        if (compensation == null) {
            throw new IllegalArgumentException("Compensation does not exist for this employeeId: " + employeeId);
        }

        return compensation;
    }

    /**
     * Fetches an Employee by id and will throw an exception if there is no employee.
     *
     * @param employeeId employee ID to lookup
     * @return valid employee if found or throws exception
     */
    private Employee getEmployeeById(String employeeId) {
        final Employee employee = employeeRepository.findByEmployeeId(employeeId);

        if (employee == null) {
            throw new IllegalArgumentException("Invalid employeeId: " + employeeId);
        }

        return employee;
    }
}
