package com.entreprise.services;

import com.entreprise.entity.*;

import java.util.List;
import java.util.Optional;

public interface AdminService {

    Optional<Admin> findById(Long id);
    List<Admin> findAll(int page, int size);

    Service addService(Service service);
    Optional<Service> findServiceById(Long id);
    List<Service> getAllServices();
    List<Service> getServicesSansManager(int page, int size);

    Employee addEmploye(Employee employee);
    Employee addEmployeInService(Employee employee, Service service);

    Optional<Employee> getEmployeByTel(String tel);
    List<Employee> getAllEmployees(int page, int size);
    List<Employee> getEmployeesByService(Long serviceId, int page, int size);

    long countEmployeesByService(Long serviceId);
    long countAllEmployees();

    Optional<Manager> getManagerByService(Long serviceId);
    boolean serviceHasManager(Long serviceId);
}
