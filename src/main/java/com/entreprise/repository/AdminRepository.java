package com.entreprise.repository;

import com.entreprise.entity.*;

import java.util.List;
import java.util.Optional;

public interface AdminRepository {

    Admin save(Admin admin);
    Optional<Admin> findById(Long id);
    List<Admin> findAll(int page, int size);

    Service saveService(Service service);
    Optional<Service> findServiceById(Long id);
    Optional<Service> findServiceByLibelle(String libelle);
    List<Service> findAllServices();
    List<Service> findServicesSansManager(int page, int size);
    Optional<Manager> findManagerByService(Long serviceId);
    boolean serviceHasManager(Long serviceId);

    Employee saveEmploye(Employee employee);
    Employee saveEmployeInService(Employee employee, Service service);
    Optional<Employee> findEmployeById(Long employeeId);
    Optional<Employee> findEmployeByTel(String tel);
    List<Employee> findAllEmployees(int page, int size);
    List<Employee> findAllEmployeesByService(Long serviceId, int page, int size);
    long countEmployeesByService(Long serviceId);
    long countAllEmployees();
}
