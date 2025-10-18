package com.entreprise.services;

import com.entreprise.entity.Employee;
import com.entreprise.entity.Manager;
import com.entreprise.entity.Specialite;

import java.util.List;
import java.util.Optional;

public interface ManagerService {

    Optional<Manager> findById(Long id);

    List<Employee> getAllEmployeesDeMonService(Long managerId, int page, int size);
    List<Employee> getAllEmployeesDeMonServiceParSpecialite(Long managerId, Specialite specialite, int page, int size);

    long countEmployeesDeMonService(Long managerId);
    long countEmployeesDeMonServiceParSpecialite(Long managerId, Specialite specialite);

    Optional<Employee> getEmployeDetails(Long employeeId);
}
