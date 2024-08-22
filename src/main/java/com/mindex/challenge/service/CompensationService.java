package com.mindex.challenge.service;

import com.mindex.challenge.data.Compensation;

/**
 * This interface defines the methods necessary to implement the Compensation Service.
 *
 * @author Michael Szczepanski
 */
public interface CompensationService {
    Compensation create(Compensation compensation);
    Compensation read(String employeeId);
}
