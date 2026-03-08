package fr.benrep.ads.mvp;

/**
 * MVP - VIEW INTERFACE (contrat)
 *
 * Responsabilités :
 * - Définir le contrat que la View concrète doit respecter
 * - Permettre au Presenter de piloter la View sans la connaître concrètement
 *
 * Différence clé avec MVC :
 * Le Presenter n'a jamais de référence sur JTextField, JLabel, etc.
 * Il communique uniquement via cette interface, ce qui rend le Presenter
 * 100% testable sans Swing (on peut mocker cette interface).
 */
public interface CalculatorViewContract {

    // --- Getters : le Presenter lit les saisies utilisateur ---
    String getFieldA();
    String getFieldB();
    String getSelectedOperation();

    // --- Setters : le Presenter met à jour l'affichage ---
    void setResultLabel(String value);
    void setStatusLabel(String message);
    void setFieldA(String value);
    void setFieldB(String value);
    void setOperation(String op);

    /** Affiche la fenêtre. */
    void show();
}