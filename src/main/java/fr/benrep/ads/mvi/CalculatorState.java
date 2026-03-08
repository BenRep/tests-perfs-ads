package fr.benrep.ads.mvi;

/**
 * MVI - STATE
 *
 * Représente l'état complet et immuable de l'UI à un instant T.
 * La View n'affiche que ce que le State contient.
 *
 * Propriétés clés :
 * - IMMUABLE : une fois créé, le State ne change jamais. Toute modification
 *   produit un NOUVEL objet State (via copy() ou constructeur).
 * - COMPLET : la View peut se reconstruire intégralement depuis un State seul.
 * - SOURCE DE VÉRITÉ UNIQUE : il n'y a qu'un seul State courant à tout moment.
 *
 * Différences avec MVC/MVP/MVVM :
 * - Pas de setters sur un Model mutable
 * - Chaque action produit un nouveau State (comme Redux en JS)
 * - La View ne stocke aucun état local
 */
public final class CalculatorState {

    public final int operandA;
    public final int operandB;
    public final String operation;
    public final int result;
    public final String statusMessage;
    public final boolean isError;

    public CalculatorState(int operandA,
            int operandB,
            String operation,
            int result,
            String statusMessage,
            boolean isError) {
        this.operandA      = operandA;
        this.operandB      = operandB;
        this.operation     = operation;
        this.result        = result;
        this.statusMessage = statusMessage;
        this.isError       = isError;
    }

    /** État initial de l'application. */
    public static CalculatorState initial() {
        return new CalculatorState(0, 0, "+", 0, "Prêt", false);
    }

    /** État réinitialisé. */
    public static CalculatorState reset() {
        return new CalculatorState(0, 0, "+", 0, "Réinitialisé", false);
    }

    /** Produit un nouvel état avec un résultat calculé. */
    public CalculatorState withResult(int a, int b, String op, int result) {
        return new CalculatorState(a, b, op, result,
                "Calcul effectué : " + a + " " + op + " " + b + " = " + result,
                false);
    }

    /** Produit un nouvel état d'erreur. */
    public CalculatorState withError(String errorMessage) {
        return new CalculatorState(operandA, operandB, operation, result,
                "Erreur : " + errorMessage, true);
    }

    @Override
    public String toString() {
        return "CalculatorState{" + operandA + " " + operation + " " + operandB +
                " = " + result + ", status='" + statusMessage + "'}";
    }
}