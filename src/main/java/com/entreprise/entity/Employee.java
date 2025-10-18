package com.entreprise.entity;

import java.math.BigDecimal;

import jakarta.persistence.*;

@Entity
@Table(name = "employee")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "tel", nullable = false, unique = true)
    private String tel;

    @Column(name = "salaire", nullable = false)
    private BigDecimal salaire;


    protected Employee() {}

    public Employee(String nom, String tel, BigDecimal salaire) {
        this.nom = nom;
        this.tel = tel;
        this.salaire = salaire;
    }

    public Long getId() { return id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getTel() { return tel; }
    public void setTel(String tel) { this.tel = tel; }
    public BigDecimal getSalaire() { return salaire; }
    public void setSalaire(BigDecimal salaire) { this.salaire = salaire; }

    @Override
    public String toString() {
        return "ID: " + id +
               "\nNom: " + nom +
               "\nTel: " + tel +
               "\nSalaire: " + salaire;
    }
}
