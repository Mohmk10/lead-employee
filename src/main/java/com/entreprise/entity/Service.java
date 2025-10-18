package com.entreprise.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "service")
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "libelle", nullable = false, unique = true)
    private String libelle;

    protected Service() {}

    public Service(String libelle) { this.libelle = libelle; }

    public Long getId() { return id; }
    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }

    @Override
    public String toString() { return "Service-ID: " + id + ", Libellé: " + libelle; }
}
