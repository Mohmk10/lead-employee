package com.entreprise.entity;

import java.math.BigDecimal;

import jakarta.persistence.*;

@Entity
@Table(name = "manager")
@PrimaryKeyJoinColumn(name = "id_employee")
public class Manager extends Employee {

    @Column(name = "prime", nullable = false)
    private BigDecimal prime;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "service_id", nullable = false, unique = true)
    private Service service;

    protected Manager() {}

    public Manager(String nom, String tel, BigDecimal salaire, BigDecimal prime, Service service) {
        super(nom, tel, salaire);
        this.prime = prime;
        this.service = service;
    }

    public BigDecimal getPrime() { return prime; }
    public void setPrime(BigDecimal prime) { this.prime = prime; }
    public Service getService() { return service; }
    public void setService(Service service) { this.service = service; }

    @Override
    public String toString() { return "\nFonction: Manager, \nPrime: " + prime; }
}



