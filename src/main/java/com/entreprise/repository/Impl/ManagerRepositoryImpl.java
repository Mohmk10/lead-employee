package com.entreprise.repository.Impl;

import com.entreprise.config.AppConfig;
import com.entreprise.entity.Employee;
import com.entreprise.entity.Manager;
import com.entreprise.entity.Specialite;
import com.entreprise.repository.ManagerRepository;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

public class ManagerRepositoryImpl implements ManagerRepository {

    @Override
    public Optional<Manager> findById(Long id) {
        var em = AppConfig.getEntityManagerFactory().createEntityManager();
        try { return Optional.ofNullable(em.find(Manager.class, id)); }
        finally { em.close(); }
    }

    @Override
    public Optional<Long> findServiceIdByManager(Long managerId) {
        var em = AppConfig.getEntityManagerFactory().createEntityManager();
        try {
            List<Long> list = em.createQuery(
                    "SELECT m.service.id FROM Manager m WHERE m.id = :mid", Long.class)
                .setParameter("mid", managerId)
                .getResultList();
            return list.isEmpty() ? Optional.empty() : Optional.ofNullable(list.get(0));
        } finally { em.close(); }
    }

    @Override
    public List<Employee> findAllEmployeesDeMonService(Long managerId, int page, int size) {
        var em = AppConfig.getEntityManagerFactory().createEntityManager();
        try {
            Long sid = findServiceIdByManager(managerId).orElse(null);
            if (sid == null) return List.of();

            List<Long> ids = em.createQuery(
                    "SELECT e.id FROM Employee e " +
                    "WHERE (TYPE(e) = Manager AND e.id IN (SELECT m.id FROM Manager m WHERE m.service.id = :sid)) " +
                    "   OR (TYPE(e) = Developer AND e.id IN (SELECT d.id FROM Developer d WHERE d.service.id = :sid)) " +
                    "ORDER BY e.id ASC", Long.class)
                .setParameter("sid", sid)
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

            var order = new LinkedHashSet<>(ids);
            result.sort((a, b) -> {
                int ia = new ArrayList<>(order).indexOf(a.getId());
                int ib = new ArrayList<>(order).indexOf(b.getId());
                return Integer.compare(ia, ib);
            });

            return result;
        } finally { em.close(); }
    }

    @Override
    public List<Employee> findAllEmployeesDeMonServiceParSpecialite(Long managerId, Specialite specialite, int page, int size) {
        var em = AppConfig.getEntityManagerFactory().createEntityManager();
        try {
            Long sid = findServiceIdByManager(managerId).orElse(null);
            if (sid == null) return List.of();

            List<Long> ids = em.createQuery(
                    "SELECT e.id FROM Employee e " +
                    "WHERE TYPE(e) = Developer " +
                    "  AND e.id IN (SELECT d.id FROM com.entreprise.entity.Developer d WHERE d.service.id = :sid AND d.specialite = :sp) " +
                    "ORDER BY e.id ASC", Long.class)
                .setParameter("sid", sid)
                .setParameter("sp", specialite)
                .setFirstResult(Math.max(0, page) * Math.max(1, size))
                .setMaxResults(Math.max(1, size))
                .getResultList();

            if (ids.isEmpty()) return List.of();

            List<Employee> result = em.createQuery(
                    "SELECT DISTINCT e FROM Employee e " +
                    "LEFT JOIN FETCH TREAT(e AS com.entreprise.entity.Developer).service " +
                    "WHERE e.id IN :ids " +
                    "ORDER BY e.id ASC", Employee.class)
                .setParameter("ids", ids)
                .getResultList();

            var order = new LinkedHashSet<>(ids);
            result.sort((a, b) -> {
                int ia = new ArrayList<>(order).indexOf(a.getId());
                int ib = new ArrayList<>(order).indexOf(b.getId());
                return Integer.compare(ia, ib);
            });

            return result;
        } finally { em.close(); }
    }

    @Override
    public long countEmployeesDeMonService(Long managerId) {
        var em = AppConfig.getEntityManagerFactory().createEntityManager();
        try {
            Long sid = findServiceIdByManager(managerId).orElse(null);
            if (sid == null) return 0L;

            Long cManagers = em.createQuery(
                    "SELECT COUNT(m) FROM Manager m WHERE m.service.id = :sid", Long.class)
                .setParameter("sid", sid)
                .getSingleResult();

            Long cDevelopers = em.createQuery(
                    "SELECT COUNT(d) FROM com.entreprise.entity.Developer d WHERE d.service.id = :sid", Long.class)
                .setParameter("sid", sid)
                .getSingleResult();

            return cManagers + cDevelopers;
        } finally { em.close(); }
    }

    @Override
    public long countEmployeesDeMonServiceParSpecialite(Long managerId, Specialite specialite) {
        var em = AppConfig.getEntityManagerFactory().createEntityManager();
        try {
            Long sid = findServiceIdByManager(managerId).orElse(null);
            if (sid == null) return 0L;

            return em.createQuery(
                    "SELECT COUNT(d) FROM com.entreprise.entity.Developer d WHERE d.service.id = :sid AND d.specialite = :sp", Long.class)
                .setParameter("sid", sid)
                .setParameter("sp", specialite)
                .getSingleResult();
        } finally { em.close(); }
    }
}
