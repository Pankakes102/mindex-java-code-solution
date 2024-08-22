package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This class implements and defines the logic for the Compensation Service.
 *
 * @author Michael Szczepanski
 */
public class CompensationServiceImpl implements CompensationService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CompensationRepository compensationRepository;

    @Override
    public Compensation create(Compensation compensation) {
        return null;
    }

    @Override
    public Compensation read(String employeeId) {
        return null;
    }
}
