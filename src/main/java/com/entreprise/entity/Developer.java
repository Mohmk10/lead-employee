package com.entreprise.entity;

import java.math.BigDecimal;

import jakarta.persistence.*;

@Entity
@Table(name = "developer")
@PrimaryKeyJoinColumn(name = "id_employee")
public class Developer extends Employee {

    @Enumerated(EnumType.STRING)
    @Column(name = "specialite", nullable = false)
    private Specialite specialite;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

    protected Developer() {}

    public Developer(String nom, String tel, BigDecimal salaire, Specialite specialite, Service service) {
        super(nom, tel, salaire);
        this.specialite = specialite;
        this.service = service;
    }

    public Specialite getSpecialite() { return specialite; }
    public void setSpecialite(Specialite specialite) { this.specialite = specialite; }
    public Service getService() { return service; }
    public void setService(Service service) { this.service = service; }

    @Override
    public String toString() { return "Fonction: Developer " + specialite; }
}
