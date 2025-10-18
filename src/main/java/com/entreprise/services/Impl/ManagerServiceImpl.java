package com.entreprise.services.Impl;

import com.entreprise.entity.Employee;
import com.entreprise.entity.Manager;
import com.entreprise.entity.Specialite;
import com.entreprise.repository.ManagerRepository;
import com.entreprise.services.ManagerService;

import java.util.List;
import java.util.Optional;

public class ManagerServiceImpl implements ManagerService {

    private final ManagerRepository managerRepository;

    public ManagerServiceImpl(ManagerRepository managerRepository) {
        this.managerRepository = managerRepository;
    }

    @Override
    public Optional<Manager> findById(Long id) {
        return managerRepository.findById(id);
    }

    @Override
    public List<Employee> getAllEmployeesDeMonService(Long managerId, int page, int size) {
        return managerRepository.findAllEmployeesDeMonService(managerId, page, size);
    }

    @Override
    public List<Employee> getAllEmployeesDeMonServiceParSpecialite(Long managerId, Specialite specialite, int page, int size) {
        return managerRepository.findAllEmployeesDeMonServiceParSpecialite(managerId, specialite, page, size);
    }

    @Override
    public long countEmployeesDeMonService(Long managerId) {
        return managerRepository.countEmployeesDeMonService(managerId);
    }

    @Override
    public long countEmployeesDeMonServiceParSpecialite(Long managerId, Specialite specialite) {
        return managerRepository.countEmployeesDeMonServiceParSpecialite(managerId, specialite);
    }

    @Override
    public Optional<Employee> getEmployeDetails(Long employeeId) {
        // Optionnel : si besoin d’un détail ponctuel pour un affichage
        return Optional.empty();
    }
}
