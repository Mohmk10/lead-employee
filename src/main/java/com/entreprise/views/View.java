package com.entreprise.views;

import java.util.List;

public interface View<T> {
    
    int saisieInt(String message);
    long saisieLong(String message);

    double saisieDoublePositif(String message);

    String saisieTexte(String message);
    
    void afficherListe(List<T> items);
    void afficherDetail(T item);
}
