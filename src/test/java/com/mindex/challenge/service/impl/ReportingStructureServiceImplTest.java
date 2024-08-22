package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

/**
 * Unit tests for the Reporting Structure Service Implementation.
 *
 * @author Michael Szczepanski
 */
@RunWith(MockitoJUnitRunner.class)
public class ReportingStructureServiceImplTest {
    private static final String EMPLOYEE_ID = "1";

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private ReportingStructureServiceImpl reportingStructureService;

    @Test
    public void invalidEmployeeIdThrowsRuntimeException() {
        Mockito.when(employeeRepository.findByEmployeeId(Mockito.anyString())).thenReturn(null);

        final RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            reportingStructureService.fetchReportingStructureForEmployee(EMPLOYEE_ID);
        });

        Assertions.assertTrue(exception.getMessage().contains("Invalid employeeId"));
        Assertions.assertTrue(exception.getMessage().contains(EMPLOYEE_ID));
    }

    @Test
    public void emptyDirectReportsReturnsZero() {
        Mockito.when(employeeRepository.findByEmployeeId(Mockito.anyString())).thenReturn(new Employee());

        final ReportingStructure reportingStructure = reportingStructureService.fetchReportingStructureForEmployee(EMPLOYEE_ID);

        Mockito.verify(employeeRepository).findByEmployeeId(Mockito.anyString());

        Assertions.assertNotNull(reportingStructure);
        Assertions.assertNotNull(reportingStructure.getEmployee());
        Assertions.assertNull(reportingStructure.getEmployee().getDirectReports());
        Assertions.assertEquals(0, reportingStructure.getNumberOfReports());
    }

    @Test
    public void calculateDirectReportsReturnsValue() {
        final Employee john = createEmployee(EMPLOYEE_ID);
        final Employee ringo = createEmployee("2");
        final Employee paul = createEmployee("3");
        final Employee pete = createEmployee("4");
        final Employee george = createEmployee("5");

        final List<Employee> paulsDirectReports = Arrays.asList(pete, george);
        final List<Employee> johnsDirectReports = Arrays.asList(ringo, paul);

        paul.setDirectReports(paulsDirectReports);
        john.setDirectReports(johnsDirectReports);

        Mockito.when(employeeRepository.findByEmployeeId(EMPLOYEE_ID)).thenReturn(john);
        Mockito.when(employeeRepository.findByEmployeeId("2")).thenReturn(ringo);
        Mockito.when(employeeRepository.findByEmployeeId("3")).thenReturn(paul);
        Mockito.when(employeeRepository.findByEmployeeId("4")).thenReturn(pete);
        Mockito.when(employeeRepository.findByEmployeeId("5")).thenReturn(george);

        final ReportingStructure reportingStructure = reportingStructureService.fetchReportingStructureForEmployee(EMPLOYEE_ID);

        Mockito.verify(employeeRepository, Mockito.times(5)).findByEmployeeId(Mockito.anyString());

        Assertions.assertNotNull(reportingStructure);
        Assertions.assertNotNull(reportingStructure.getEmployee());
        Assertions.assertEquals(EMPLOYEE_ID, reportingStructure.getEmployee().getEmployeeId());
        Assertions.assertEquals(4, reportingStructure.getNumberOfReports());
    }

    private Employee createEmployee(String id) {
        final Employee employee = new Employee();

        employee.setEmployeeId(id);

        return employee;
    }
}
