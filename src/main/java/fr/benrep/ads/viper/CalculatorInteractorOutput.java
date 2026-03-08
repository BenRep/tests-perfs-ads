package fr.benrep.ads.viper;

/**
 * VIPER - INTERACTOR OUTPUT PORT
 *
 * Interface de communication entre l'Interactor et le Presenter.
 * L'Interactor appelle ces méthodes une fois les use cases terminés.
 * Le Presenter implémente ce port.
 *
 * Ce port inverse la dépendance (Dependency Inversion Principle) :
 * l'Interactor ne connaît pas le Presenter concret, seulement ce contrat.
 * Cela permet de tester l'Interactor avec un mock de ce port.
 */
public interface CalculatorInteractorOutput {

    /**
     * Appelé par l'Interactor quand le calcul est réussi.
     * @param entity l'Entity mise à jour avec le résultat
     */
    void onCalculationSuccess(CalculatorEntity entity);

    /**
     * Appelé par l'Interactor en cas d'erreur de calcul.
     * @param errorMessage message d'erreur localisé
     */
    void onCalculationError(String errorMessage);

    /**
     * Appelé par l'Interactor une fois le reset effectué.
     * @param entity l'Entity réinitialisée
     */
    void onResetDone(CalculatorEntity entity);
}