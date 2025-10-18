package com.entreprise.views;

import com.entreprise.entity.*;

import java.util.List;

public final class DisplayUtils {
    private DisplayUtils() {}

    public static void printEmployees(List<Employee> items) {
        if (items == null || items.isEmpty()) {
            System.out.println("\nAucun employé.");
            return;
        }
        for (Employee e : items) {
            printEmployee(e);
            System.out.println();
        }
    }

    public static void printEmployee(Employee e) {
        if (e == null) {
            System.out.println("\nAucun détail.");
            return;
        }
        System.out.println("ID : " + e.getId());
        System.out.println("Nom : " + e.getNom());
        System.out.println("Téléphone : " + e.getTel());
        System.out.printf("Salaire : %.2f%n", e.getSalaire());

        if (e instanceof Admin) {
            System.out.println("Fonction : Admin");
        } else if (e instanceof Manager m) {
            System.out.println("Fonction : Manager");
            if (m.getPrime() != null) System.out.printf("Prime : %.2f%n", m.getPrime());
            Service s = m.getService();
            if (s != null && s.getId() != null)
                System.out.println("Service : [" + s.getId() + "] " + s.getLibelle());
        } else if (e instanceof Developer d) {
            System.out.println("Fonction : Développeur");
            if (d.getSpecialite() != null)
                System.out.println("Spécialité : " + d.getSpecialite());
            Service s = d.getService();
            if (s != null && s.getId() != null)
                System.out.println("Service : [" + s.getId() + "] " + s.getLibelle());
        } else {
            System.out.println("Fonction : Employé");
        }
    }
}
