package com.entreprise.views.Impl;

import com.entreprise.entity.Employee;
import com.entreprise.entity.Manager;
import com.entreprise.entity.Specialite;
import com.entreprise.services.ManagerService;
import com.entreprise.views.ConsoleIO;
import com.entreprise.views.DisplayUtils;
import com.entreprise.views.ManagerView;

import java.util.List;
import java.util.Optional;

public class ManagerViewImpl implements ManagerView {

    private final ManagerService managerService;

    public ManagerViewImpl(ManagerService managerService) {
        this.managerService = managerService;
    }

    @Override
    public void menuPrincipal(long managerId) {
        Optional<Manager> current = managerService.findById(managerId);
        if (current.isEmpty()) {
            System.out.println("\nAccès refusé (Manager introuvable).");
            ConsoleIO.pause();
            return;
        }

        int choix;
        do {
            System.out.println("\n===== MENU MANAGER =====");
            System.out.println("1 - Lister employés de mon service");
            System.out.println("2 - Lister employés par spécialité");
            System.out.println("3 - Quitter (retour)");
            choix = ConsoleIO.saisieInt("Choix");

            switch (choix) {
                case 1 -> listerEmployes(managerId);
                case 2 -> listerParSpecialite(managerId);
                case 3 -> System.out.println("\nRetour.");
                default -> System.out.println("\nChoix invalide.");
            }
        } while (choix != 3);
    }

    private void listerEmployes(Long managerId) {
        long total = managerService.countEmployeesDeMonService(managerId);
        int page = 0, size = (int) Math.min(50, Math.max(1, total));
        if (total > 10) {
            page = Math.max(0, ConsoleIO.saisieInt("Page (0..n)"));
            size = Math.max(1, ConsoleIO.saisieInt("Taille de page (>0)"));
        }
        List<Employee> list = managerService.getAllEmployeesDeMonService(managerId, page, size);
        DisplayUtils.printEmployees(list);
        ConsoleIO.pause();
    }

    private void listerParSpecialite(Long managerId) {
        System.out.println("\nSpécialité : 1-FULLSTACK  2-FRONTEND  3-BACKEND");
        int ch = ConsoleIO.saisieInt("Choix");
        Specialite sp = switch (ch) {
            case 1 -> Specialite.FULLSTACK;
            case 2 -> Specialite.FRONTEND;
            case 3 -> Specialite.BACKEND;
            default -> Specialite.FULLSTACK;
        };

        long total = managerService.countEmployeesDeMonServiceParSpecialite(managerId, sp);
        int page = 0, size = (int) Math.min(50, Math.max(1, total));
        if (total > 10) {
            page = Math.max(0, ConsoleIO.saisieInt("Page (0..n)"));
            size = Math.max(1, ConsoleIO.saisieInt("Taille de page (>0)"));
        }
        List<Employee> list = managerService.getAllEmployeesDeMonServiceParSpecialite(managerId, sp, page, size);
        DisplayUtils.printEmployees(list);
        ConsoleIO.pause();
    }

    @Override
    public int saisieInt(String message) { return ConsoleIO.saisieInt(message); }

    @Override
    public long saisieLong(String message) { return ConsoleIO.saisieLong(message); }

    @Override
    public double saisieDoublePositif(String message) { return ConsoleIO.saisieDoublePositif(message); }

    @Override
    public String saisieTexte(String message) { return ConsoleIO.saisieTexte(message); }

    @Override
    public void afficherListe(List<Employee> items) { DisplayUtils.printEmployees(items); }

    @Override
    public void afficherDetail(Employee item) { DisplayUtils.printEmployee(item); }
}
