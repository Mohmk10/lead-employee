package com.entreprise.views;

import java.util.Scanner;

public final class ConsoleIO {
    private static final Scanner SC = new Scanner(System.in);

    private ConsoleIO() {}

    public static int saisieInt(String message) {
        while (true) {
            System.out.print("\n" + message + " : ");
            try { return Integer.parseInt(SC.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.println("Veuillez saisir un entier."); }
        }
    }

    public static long saisieLong(String message) {
        while (true) {
            System.out.print("\n" + message + " : ");
            try { return Long.parseLong(SC.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.println("Veuillez saisir un entier."); }
        }
    }

    public static double saisieDoublePositif(String message) {
        while (true) {
            System.out.print("\n" + message + " : ");
            try {
                double valeur = Double.parseDouble(SC.nextLine().trim());
                if (valeur < 0) { System.out.println("La valeur doit être ≥ 0."); continue; }
                return valeur;
            } catch (NumberFormatException e) { System.out.println("Veuillez saisir un nombre."); }
        }
    }

    public static String saisieTexte(String message) {
        while (true) {
            System.out.print("\n" + message + " : ");
            String string = SC.nextLine().trim();
            if (string.isEmpty()) { System.out.println("La valeur ne peut pas être vide."); continue; }
            return string;
        }
    }

    public static void pause() {
        System.out.print("\n(Entrée pour continuer) ");
        SC.nextLine();
    }
}
