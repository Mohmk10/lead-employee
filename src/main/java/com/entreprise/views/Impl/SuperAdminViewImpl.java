package com.entreprise.views.Impl;

import com.entreprise.entity.Admin;
import com.entreprise.services.AdminService;
import com.entreprise.views.*;

import java.util.Optional;

public class SuperAdminViewImpl implements SuperAdminView {

    private final AdminService adminService;
    private final AdminView adminView;
    private final ManagerView managerView;

    public SuperAdminViewImpl(AdminService adminService, AdminView adminView, ManagerView managerView) {
        this.adminService = adminService;
        this.adminView = adminView;
        this.managerView = managerView;
    }

    @Override
    public void menuPrincipal() {
        int choix;
        do {
            System.out.println("\n===== MENU SUPER ADMIN =====");
            System.out.println("1 - Créer un Admin");
            System.out.println("2 - Se connecter");
            System.out.println("3 - Quitter");
            choix = ConsoleIO.saisieInt("Choix");

            switch (choix) {
                case 1 -> creerAdmin();
                case 2 -> seConnecter();
                case 3 -> System.out.println("\nFin du programme.");
                default -> System.out.println("Choix invalide.");
            }
        } while (choix != 3);
    }

    private void creerAdmin() {
        System.out.println("\n=== Création d’un Admin ===");
        String nom = ConsoleIO.saisieTexte("Nom");
        String tel = ConsoleIO.saisieTexte("Téléphone");
        double salaire = ConsoleIO.saisieDoublePositif("Salaire");

        try {
            Admin admin = new Admin(nom, tel, java.math.BigDecimal.valueOf(salaire));
            adminService.addEmploye(admin);
            System.out.println("\nAdmin créé avec succès.");
        } catch (IllegalArgumentException e) {
            System.out.println("\n" + e.getMessage());
        }
        ConsoleIO.pause();
    }

    private void seConnecter() {
        System.out.println("\n1 - Espace Admin");
        System.out.println("2 - Espace Manager");
        int choix = ConsoleIO.saisieInt("Choix");
        switch (choix) {
            case 1 -> {
                long aid = ConsoleIO.saisieLong("Votre ID Admin");
                Optional<Admin> a = adminService.findById(aid);
                if (a.isEmpty()) {
                    System.out.println("\nAccès refusé (Admin introuvable).");
                    ConsoleIO.pause();
                    return;
                }
                adminView.menuPrincipal();
            }
            case 2 -> {
                long mid = ConsoleIO.saisieLong("Votre ID Manager");
                managerView.menuPrincipal(mid);
            }
            default -> System.out.println("Choix invalide.");
        }
    }
}
