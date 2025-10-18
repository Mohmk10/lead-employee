package com.entreprise.services.Impl;

import com.entreprise.entity.*;
import com.entreprise.repository.AdminRepository;
import com.entreprise.services.AdminService;
import com.entreprise.services.errors.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;

    public AdminServiceImpl(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public Optional<Admin> findById(Long id) {
        return adminRepository.findById(id);
    }

    @Override
    public List<Admin> findAll(int page, int size) {
        return adminRepository.findAll(page, size);
    }

    @Override
    public Service addService(Service service) {
        adminRepository.findServiceByLibelle(service.getLibelle()).ifPresent(s -> { throw new DuplicateTelephoneException(); });
        return adminRepository.saveService(service);
    }

    @Override
    public Optional<Service> findServiceById(Long id) {
        return adminRepository.findServiceById(id);
    }

    @Override
    public List<Service> getAllServices() {
        return adminRepository.findAllServices();
    }

    @Override
    public List<Service> getServicesSansManager(int page, int size) {
        return adminRepository.findServicesSansManager(page, size);
    }

    @Override
    public Employee addEmploye(Employee employee) {

        if (employee.getSalaire() == null || employee.getSalaire().compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativeSalaryException();
        }
        adminRepository.findEmployeByTel(employee.getTel())
                .ifPresent(e -> { throw new DuplicateTelephoneException(); });

        if (employee instanceof Admin) {
            return adminRepository.saveEmploye(employee);
        }

        throw new UnsupportedOperationException();
    }

    @Override
    public Employee addEmployeInService(Employee employee, Service service) {
        
        adminRepository.findEmployeByTel(employee.getTel()).ifPresent(e -> { throw new DuplicateTelephoneException(); });

        if (employee instanceof Manager && adminRepository.serviceHasManager(service.getId())) {
            throw new ServiceAlreadyHasManagerException();
        }

        if (employee.getSalaire() == null || employee.getSalaire().compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativeSalaryException();
        }
        if (employee instanceof Manager m) {
            if (m.getPrime() == null || m.getPrime().compareTo(BigDecimal.ZERO) < 0) {
                throw new NegativePrimeException();
            }
        }
        return adminRepository.saveEmployeInService(employee, service);
    }

    @Override
    public Optional<Employee> getEmployeByTel(String tel) {
        return adminRepository.findEmployeByTel(tel);
    }

    @Override
    public List<Employee> getAllEmployees(int page, int size) {
        return adminRepository.findAllEmployees(page, size);
    }

    @Override
    public List<Employee> getEmployeesByService(Long serviceId, int page, int size) {
        return adminRepository.findAllEmployeesByService(serviceId, page, size);
    }

    @Override
    public long countEmployeesByService(Long serviceId) {
        return adminRepository.countEmployeesByService(serviceId);
    }

    @Override
    public long countAllEmployees() {
        return adminRepository.countAllEmployees();
    }

    @Override
    public Optional<Manager> getManagerByService(Long serviceId) {
        return adminRepository.findManagerByService(serviceId);
    }

    @Override
    public boolean serviceHasManager(Long serviceId) {
        return adminRepository.serviceHasManager(serviceId);
    }
}
