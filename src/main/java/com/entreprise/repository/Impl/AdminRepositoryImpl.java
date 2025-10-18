package com.entreprise.repository.Impl;

import com.entreprise.config.AppConfig;
import com.entreprise.entity.*;
import com.entreprise.repository.AdminRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class AdminRepositoryImpl implements AdminRepository {

    @Override
    public Admin save(Admin admin) {
        EntityManager em = AppConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (admin.getId() == null) em.persist(admin);
            else admin = em.merge(admin);
            tx.commit();
            return admin;
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally { em.close(); }
    }

    @Override
    public Optional<Admin> findById(Long id) {
        EntityManager em = AppConfig.getEntityManagerFactory().createEntityManager();
        try { return Optional.ofNullable(em.find(Admin.class, id)); }
        finally { em.close(); }
    }

    @Override
    public List<Admin> findAll(int page, int size) {
        EntityManager em = AppConfig.getEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery("SELECT a FROM Admin a ORDER BY a.id ASC", Admin.class)
                .setFirstResult(Math.max(0, page) * Math.max(1, size))
                .setMaxResults(Math.max(1, size))
                .getResultList();
        } finally { em.close(); }
    }

    @Override
    public Service saveService(Service service) {
        EntityManager em = AppConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (service.getId() == null) em.persist(service);
            else service = em.merge(service);
            tx.commit();
            return service;
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally { em.close(); }
    }

    @Override
    public Optional<Service> findServiceById(Long id) {
        EntityManager em = AppConfig.getEntityManagerFactory().createEntityManager();
        try { return Optional.ofNullable(em.find(Service.class, id)); }
        finally { em.close(); }
    }

    @Override
    public Optional<Service> findServiceByLibelle(String libelle) {
        EntityManager em = AppConfig.getEntityManagerFactory().createEntityManager();
        try {
            List<Service> list = em.createQuery(
                    "SELECT s FROM Service s WHERE s.libelle = :lib", Service.class)
                .setParameter("lib", libelle)
                .getResultList();
            return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
        } finally { em.close(); }
    }

    @Override
    public List<Service> findAllServices() {
        EntityManager em = AppConfig.getEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery("SELECT s FROM Service s ORDER BY s.id ASC", Service.class)
                .getResultList();
        } finally { em.close(); }
    }

    @Override
    public List<Service> findServicesSansManager(int page, int size) {
        EntityManager em = AppConfig.getEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery(
                    "SELECT s FROM Service s " +
                    "WHERE NOT EXISTS (SELECT 1 FROM Manager m WHERE m.service = s) " +
                    "ORDER BY s.id ASC", Service.class)
                .setFirstResult(Math.max(0, page) * Math.max(1, size))
                .setMaxResults(Math.max(1, size))
                .getResultList();
        } finally { em.close(); }
    }

    @Override
    public Employee saveEmploye(Employee employee) {
        EntityManager em = AppConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (employee.getId() == null) em.persist(employee);
            else employee = em.merge(employee);
            tx.commit();
            return employee;
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally { em.close(); }
    }

    @Override
    public Employee saveEmployeInService(Employee employee, Service service) {
        EntityManager em = AppConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            service = em.merge(service);

            if (employee instanceof Manager manager) {
                manager.setService(service);
                if (manager.getId() == null) em.persist(manager);
                else employee = em.merge(manager);
            } else if (employee instanceof Developer developer) {
                developer.setService(service);
                if (developer.getId() == null) em.persist(developer);
                else employee = em.merge(developer);
            } else {
                throw new IllegalArgumentException();
            }

            tx.commit();
            return employee;
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally { em.close(); }
    }

    @Override
    public Optional<Employee> findEmployeById(Long employeeId) {
        EntityManager em = AppConfig.getEntityManagerFactory().createEntityManager();
        try { return Optional.ofNullable(em.find(Employee.class, employeeId)); }
        finally { em.close(); }
    }

    @Override
    public Optional<Employee> findEmployeByTel(String tel) {
        EntityManager em = AppConfig.getEntityManagerFactory().createEntityManager();
        try {
            TypedQuery<Employee> q = em.createQuery(
                "SELECT e FROM Employee e WHERE e.tel = :tel", Employee.class);
            q.setParameter("tel", tel);
            List<Employee> list = q.getResultList();
            return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
        } finally { em.close(); }
    }

    // ... imports et entête inchangés

    @Override
    public List<Employee> findAllEmployees(int page, int size) {
        var em = AppConfig.getEntityManagerFactory().createEntityManager();
        try {
            List<Long> ids = em.createQuery(
                    "SELECT e.id FROM Employee e ORDER BY e.id ASC", Long.class)
                .setFirstResult(Math.max(0, page) * Math.max(1, size))
                .setMaxResults(Math.max(1, size))
                .getResultList();

            if (ids.isEmpty()) return List.of();

            List<Employee> result = em.createQuery(
                    "SELECT DISTINCT e FROM Employee e " +
                    "LEFT JOIN FETCH TREAT(e AS Manager).service " +
                    "LEFT JOIN FETCH TREAT(e AS Developer).service " +
                    "WHERE e.id IN :ids " +
                    "ORDER BY e.id ASC", Employee.class)
                .setParameter("ids", ids)
                .getResultList();

            var order = new java.util.LinkedHashSet<>(ids);
            result.sort((a, b) -> {
                int ia = new java.util.ArrayList<>(order).indexOf(a.getId());
                int ib = new java.util.ArrayList<>(order).indexOf(b.getId());
                return Integer.compare(ia, ib);
            });

            return result;
        } finally { em.close(); }
    }

    @Override
    public List<Employee> findAllEmployeesByService(Long serviceId, int page, int size) {
        var em = AppConfig.getEntityManagerFactory().createEntityManager();
        try {
            List<Long> ids = em.createQuery(
                    "SELECT e.id FROM Employee e " +
                    "WHERE (TYPE(e) = Manager AND e.id IN (SELECT m.id FROM Manager m WHERE m.service.id = :sid)) " +
                    "   OR (TYPE(e) = Developer AND e.id IN (SELECT d.id FROM Developer d WHERE d.service.id = :sid)) " +
                    "ORDER BY e.id ASC", Long.class)
                .setParameter("sid", serviceId)
                .setFirstResult(Math.max(0, page) * Math.max(1, size))
                .setMaxResults(Math.max(1, size))
                .getResultList();

            if (ids.isEmpty()) return List.of();

            List<Employee> result = em.createQuery(
                    "SELECT DISTINCT e FROM Employee e " +
                    "LEFT JOIN FETCH TREAT(e AS Manager).service " +
                    "LEFT JOIN FETCH TREAT(e AS Developer).service " +
                    "WHERE e.id IN :ids " +
                    "ORDER BY e.id ASC", Employee.class)
                .setParameter("ids", ids)
                .getResultList();

            var order = new java.util.LinkedHashSet<>(ids);
            result.sort((a, b) -> {
                int ia = new java.util.ArrayList<>(order).indexOf(a.getId());
                int ib = new java.util.ArrayList<>(order).indexOf(b.getId());
                return Integer.compare(ia, ib);
            });

            return result;
        } finally { em.close(); }
    }


    @Override
    public long countEmployeesByService(Long serviceId) {
        EntityManager em = AppConfig.getEntityManagerFactory().createEntityManager();
        try {
            Long countManagers = em.createQuery(
                "SELECT COUNT(m) FROM Manager m WHERE m.service.id = :sid", Long.class)
                .setParameter("sid", serviceId)
                .getSingleResult();

            Long countDevelopers = em.createQuery(
                "SELECT COUNT(d) FROM Developer d WHERE d.service.id = :sid", Long.class)
                .setParameter("sid", serviceId)
                .getSingleResult();

            return countManagers + countDevelopers;
        } finally { em.close(); }
    }

    @Override
    public long countAllEmployees() {
        EntityManager em = AppConfig.getEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery("SELECT COUNT(e) FROM Employee e", Long.class)
                .getSingleResult();
        } finally { em.close(); }
    }

    @Override
    public Optional<Manager> findManagerByService(Long serviceId) {

        EntityManager em = AppConfig.getEntityManagerFactory().createEntityManager();
        try {
            List<Manager> list = em.createQuery(
                "SELECT m FROM Manager m WHERE m.service.id = :sid", Manager.class)
                .setParameter("sid", serviceId)
                .getResultList();
            return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
        } finally { em.close(); }
    }

    @Override
    public boolean serviceHasManager(Long serviceId) {

        EntityManager em = AppConfig.getEntityManagerFactory().createEntityManager();
        try {
            Long cnt = em.createQuery(
                "SELECT COUNT(m) FROM Manager m WHERE m.service.id = :sid", Long.class)
                .setParameter("sid", serviceId)
                .getSingleResult();
            return cnt != null && cnt > 0;
        } finally { em.close(); }
    }
}
