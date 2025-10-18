package com.entreprise.repository;

import com.entreprise.entity.Employee;
import com.entreprise.entity.Manager;
import com.entreprise.entity.Specialite;

import java.util.List;
import java.util.Optional;

public interface ManagerRepository {

    Optional<Manager> findById(Long id);

    Optional<Long> findServiceIdByManager(Long managerId);

    List<Employee> findAllEmployeesDeMonService(Long managerId, int page, int size);

    List<Employee> findAllEmployeesDeMonServiceParSpecialite(Long managerId, Specialite specialite, int page, int size);

    long countEmployeesDeMonService(Long managerId);
    long countEmployeesDeMonServiceParSpecialite(Long managerId, Specialite specialite);
}
