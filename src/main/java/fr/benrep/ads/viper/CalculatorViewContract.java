package fr.benrep.ads.viper;

/**
 * VIPER - VIEW INTERFACE (contrat)
 *
 * Contrat que la View concrète doit respecter.
 * Le Presenter pilote la View uniquement via cette interface.
 *
 * Similaire au contrat MVP, mais dans VIPER la View est encore plus passive :
 * elle n'a aucune responsabilité de logique, même pas de conversion de types.
 * Tout le formatage est fait par le Presenter.
 */
public interface CalculatorViewContract {

    String getFieldA();
    String getFieldB();
    String getSelectedOperation();

    void displayResult(String result);
    void displayStatus(String status);
    void displayFieldA(String value);
    void displayFieldB(String value);
    void displayOperation(String op);

    void show();
}