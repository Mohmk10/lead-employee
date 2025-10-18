package com.entreprise.entity;

import java.math.BigDecimal;

import jakarta.persistence.*;

@Entity
@Table(name = "admin")
@PrimaryKeyJoinColumn(name = "id_employee")
public class Admin extends Employee {

    protected Admin() {}

    public Admin(String nom, String tel, BigDecimal salaire) {
        super(nom, tel, salaire);
    }

    @Override
    public String toString() { return "Fonction: Admin"; }
}
