package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Unit Tests for the Compensation Service Implementation.
 *
 * @author Michael Szczepanski
 */
@RunWith(MockitoJUnitRunner.class)
public class CompensationServiceImplUnitTest {
    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private CompensationRepository compensationRepository;

    @InjectMocks
    private CompensationServiceImpl compensationService;

    @Test
    public void nullEmployeeThrowsIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            compensationService.create(new Compensation());
        });

        Mockito.verify(employeeRepository, Mockito.never()).findByEmployeeId(Mockito.anyString());
        Mockito.verify(compensationRepository, Mockito.never()).insert(Mockito.anyIterable());
    }

    @Test
    public void nullEmployeeIdThrowsIllegalArgumentException() {
        final Employee employee = new Employee();
        final Compensation compensation = new Compensation();
        compensation.setEmployee(employee);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            compensationService.create(compensation);
        });

        Mockito.verify(employeeRepository, Mockito.never()).findByEmployeeId(Mockito.anyString());
        Mockito.verify(compensationRepository, Mockito.never()).insert(Mockito.anyIterable());
    }

    @Test
    public void invalidEmployeeIdThrowsIllegalArgumentException() {
        final Employee employee = new Employee();
        employee.setEmployeeId("Testing");

        final Compensation compensation = new Compensation();
        compensation.setEmployee(employee);

        Mockito.when(employeeRepository.findByEmployeeId(Mockito.anyString())).thenReturn(null);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            compensationService.create(compensation);
        });

        Mockito.verify(employeeRepository).findByEmployeeId(Mockito.anyString());
        Mockito.verify(compensationRepository, Mockito.never()).insert(Mockito.anyIterable());
    }

    @Test
    public void readWithNullIdThrowsIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            compensationService.read(null);
        });

        Mockito.verify(employeeRepository, Mockito.never()).findByEmployeeId(Mockito.anyString());
        Mockito.verify(compensationRepository, Mockito.never()).findByEmployee(Mockito.any());
    }

    @Test
    public void readWithInvalidIdThrowsIllegalArgumentException() {
        Mockito.when(employeeRepository.findByEmployeeId(Mockito.anyString())).thenReturn(null);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            compensationService.read("Testing Employee ID");
        });

        Mockito.verify(employeeRepository).findByEmployeeId(Mockito.anyString());
        Mockito.verify(compensationRepository, Mockito.never()).findByEmployee(Mockito.any());
    }

    @Test
    public void noCompensationRecordThrowsIllegalArgumentException() {
        Mockito.when(employeeRepository.findByEmployeeId(Mockito.anyString())).thenReturn(new Employee());
        Mockito.when(compensationRepository.findByEmployee(Mockito.any())).thenReturn(null);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            compensationService.read("Testing Employee ID");
        });

        Mockito.verify(employeeRepository).findByEmployeeId(Mockito.anyString());
        Mockito.verify(compensationRepository).findByEmployee(Mockito.any());
    }
}
