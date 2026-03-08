package fr.benrep.ads.mvc;

import java.util.ArrayList;
import java.util.List;

/**
 * MVC - MODEL
 * <p>
 * Responsabilités :
 * - Détenir l'état de l'application (operandes, résultat, opération, historique)
 * - Notifier les observateurs (Views/Controllers) lors de tout changement d'état
 * - Ne contient aucune logique de présentation ni de logique métier complexe
 * <p>
 * Pattern Observer intégré : le Model maintient une liste de Runnable listeners
 * que la View enregistre pour se mettre à jour automatiquement.
 */
public class CalculatorModel {

    private int operandA = 0;
    private int operandB = 0;
    private int result = 0;
    private String operation = "+";
    private String statusMessage = "Prêt";
    private final List<Runnable> listeners = new ArrayList<>();

    // -------------------------------------------------------------------------
    // Observer pattern
    // -------------------------------------------------------------------------

    public void addChangeListener(Runnable listener) {
        listeners.add(listener);
    }

    private void notifyListeners() {
        listeners.forEach(Runnable::run);
    }

    // -------------------------------------------------------------------------
    // Setters métier (chaque setter notifie la vue)
    // -------------------------------------------------------------------------

    public void setOperandA(int operandA) {
        this.operandA = operandA;
        notifyListeners();
    }

    public void setOperandB(int operandB) {
        this.operandB = operandB;
        notifyListeners();
    }

    public void setOperation(String operation) {
        this.operation = operation;
        notifyListeners();
    }

    public void setResult(int result) {
        this.result = result;
        notifyListeners();
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
        notifyListeners();
    }

    /** Réinitialise tous les champs à leur valeur par défaut. */
    public void reset() {
        this.operandA = 0;
        this.operandB = 0;
        this.result = 0;
        this.operation = "+";
        this.statusMessage = "Réinitialisé";
        notifyListeners();
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    public int getOperandA()       { return operandA; }
    public int getOperandB()       { return operandB; }
    public int getResult()         { return result; }
    public String getOperation()   { return operation; }
    public String getStatusMessage() { return statusMessage; }
}