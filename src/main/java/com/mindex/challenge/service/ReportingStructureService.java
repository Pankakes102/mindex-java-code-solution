package com.mindex.challenge.service;

import com.mindex.challenge.data.ReportingStructure;

/**
 * This interface will define the structure of the Reporting Structure Service.
 *
 * @author Michael Szczepanski
 */
public interface ReportingStructureService {
    /**
     * Given an employee ID, fetch the employee information and calculate the amount of employees within their reporting
     * structure.
     *
     * @param id Employee ID to lookup and calculate
     * @return the number of Direct Reports for all employees in the provided employees Direct Reports List
     */
    ReportingStructure fetchReportingStructureForEmployee(String id);
}
