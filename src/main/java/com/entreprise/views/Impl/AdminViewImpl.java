package com.entreprise.views.Impl;

import com.entreprise.entity.*;
import com.entreprise.services.AdminService;
import com.entreprise.services.errors.*;
import com.entreprise.views.*;

import java.util.List;
import java.util.Optional;

public class AdminViewImpl implements AdminView {

    private final AdminService adminService;

    public AdminViewImpl(AdminService adminService) {
        this.adminService = adminService;
    }

    @Override
    public void menuPrincipal() {
        int choix;
        do {
            System.out.println("\n===== MENU ADMIN =====");
            System.out.println("1 - Créer un service");
            System.out.println("2 - Lister les services (Manager + Nombre d'employés)");
            System.out.println("3 - Créer un employé (Manager / Développeur)");
            System.out.println("4 - Lister les employés (Tous | Par Service)");
            System.out.println("5 - Quitter (retour)");
            choix = ConsoleIO.saisieInt("Faites un choix");

            switch (choix) {
                case 1 -> creerService();
                case 2 -> listerServicesWithStats();
                case 3 -> creerEmploye();
                case 4 -> listerEmployes();
                case 5 -> System.out.println("\nRetour.");
                default -> System.out.println("\nChoix invalide.");
            }
        } while (choix != 5);
    }

    private void creerService() {
        String libelle = ConsoleIO.saisieTexte("Libellé du service");
        try {
            Service service = adminService.addService(new Service(libelle));
            System.out.println("\nService créé : " + service.getLibelle());
        } catch (DuplicateTelephoneException ex) {
            System.out.println("\nCe service existe déjà.");
        } catch (RuntimeException ex) {
            System.out.println("\nCréation impossible.");
        }
        ConsoleIO.pause();
    }

    private void listerServicesWithStats() {
        List<Service> services = adminService.getAllServices();
        if (services.isEmpty()) {
            System.out.println("\nAucun service.");
            ConsoleIO.pause();
            return;
        }
        System.out.println("\n=== Services ===");
        for (Service service : services) {
            Optional<Manager> mOpt = adminService.getManagerByService(service.getId());
            long count = adminService.countEmployeesByService(service.getId());
            String managerInfo = mOpt.map(m -> "Manager : " + m.getNom()).orElse("Pas de manager");
            System.out.printf("[%d] %s — %s — %d employé(s)\n",
                    service.getId(), service.getLibelle(), managerInfo, count);
        }
        ConsoleIO.pause();
    }

    private void creerEmploye() {
        System.out.println("\n=== Création d’employé ===");
        System.out.println("1 - Manager");
        System.out.println("2 - Développeur");
        int type = ConsoleIO.saisieInt("Choisissez");

        String nom = ConsoleIO.saisieTexte("Nom");
        String tel = ConsoleIO.saisieTexte("Téléphone");
        double salaire = ConsoleIO.saisieDoublePositif("Salaire");

        try {
            if (type == 1) creerManager(nom, tel, salaire);
            else if (type == 2) creerDeveloper(nom, tel, salaire);
            else System.out.println("\nChoix invalide.");
        } catch (DuplicateTelephoneException e) {
            System.out.println("\nCe numéro de téléphone existe déjà.");
        } catch (ServiceAlreadyHasManagerException e) {
            System.out.println("\nCe service a déjà un manager.");
        } catch (NegativeSalaryException e) {
            System.out.println("\nLe salaire ne peut pas être négatif.");
        } catch (NegativePrimeException e) {
            System.out.println("\nLa prime ne peut pas être négative.");
        } catch (RuntimeException e) {
            System.out.println("\nCréation impossible.");
        }
        ConsoleIO.pause();
    }

    private void creerManager(String nom, String tel, double salaire) {
        List<Service> libres = adminService.getServicesSansManager(0, 100);
        if (libres.isEmpty()) {
            System.out.println("\nAucun service sans manager.");
            return;
        }
        libres.forEach(s -> System.out.printf("[%d] %s\n", s.getId(), s.getLibelle()));
        long sid = ConsoleIO.saisieLong("ID du service");
        Optional<Service> so = libres.stream().filter(s -> s.getId().equals(sid)).findFirst();
        if (so.isEmpty()) {
            System.out.println("\nService invalide.");
            return;
        }
        double prime = ConsoleIO.saisieDoublePositif("Prime");
        Manager manager = new Manager(nom, tel, java.math.BigDecimal.valueOf(salaire),
                                      java.math.BigDecimal.valueOf(prime), so.get());
        adminService.addEmployeInService(manager, so.get());
        System.out.println("\nManager créé avec succès.");
    }

    private void creerDeveloper(String nom, String tel, double salaire) {
        List<Service> services = adminService.getAllServices();
        if (services.isEmpty()) {
            System.out.println("\nAucun service disponible.");
            return;
        }
        services.forEach(s -> System.out.printf("[%d] %s\n", s.getId(), s.getLibelle()));
        long sid = ConsoleIO.saisieLong("ID du service");
        Optional<Service> so = services.stream().filter(s -> s.getId().equals(sid)).findFirst();
        if (so.isEmpty()) {
            System.out.println("\nService invalide.");
            return;
        }
        System.out.println("\nSpécialité : 1-FULLSTACK  2-FRONTEND  3-BACKEND");
        int ch = ConsoleIO.saisieInt("Choix");
        Specialite sp = switch (ch) {
            case 1 -> Specialite.FULLSTACK;
            case 2 -> Specialite.FRONTEND;
            case 3 -> Specialite.BACKEND;
            default -> Specialite.FULLSTACK;
        };
        Developer dev = new Developer(nom, tel, java.math.BigDecimal.valueOf(salaire), sp, so.get());
        adminService.addEmployeInService(dev, so.get());
        System.out.println("\nDéveloppeur créé avec succès.");
    }

    private void listerEmployes() {
        System.out.println("\n1 - Tous les employés");
        System.out.println("2 - Par service");
        int ch = ConsoleIO.saisieInt("Choix");

        if (ch == 1) {
            long total = adminService.countAllEmployees();
            int page = 0, size = (int) Math.min(50, Math.max(1, total));
            if (total > 10) {
                page = Math.max(0, ConsoleIO.saisieInt("Page (0..n)"));
                size = Math.max(1, ConsoleIO.saisieInt("Taille de page (>0)"));
            }
            var list = adminService.getAllEmployees(page, size);
            DisplayUtils.printEmployees(list);
        } else if (ch == 2) {
            long sid = ConsoleIO.saisieLong("ID du service");
            long total = adminService.countEmployeesByService(sid);
            int page = 0, size = (int) Math.min(50, Math.max(1, total));
            if (total > 10) {
                page = Math.max(0, ConsoleIO.saisieInt("Page (0..n)"));
                size = Math.max(1, ConsoleIO.saisieInt("Taille de page (>0)"));
            }
            var list = adminService.getEmployeesByService(sid, page, size);
            DisplayUtils.printEmployees(list);
        } else {
            System.out.println("\nChoix invalide.");
        }
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
