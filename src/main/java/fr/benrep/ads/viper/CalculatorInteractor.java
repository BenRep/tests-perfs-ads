package fr.benrep.ads.viper;

import fr.benrep.ads.backend.adapter.CalculatorAdapter;

/**
 * VIPER - INTERACTOR
 *
 * Contient toute la logique métier de la fonctionnalité.
 * Orchestre les Entities et communique avec le backend.
 *
 * Responsabilités :
 * - Manipuler les Entities (lire, modifier, valider)
 * - Appeler le backend (use cases)
 * - Retourner le résultat au Presenter via le OutputPort
 *
 * Différences clés avec les autres patterns :
 * - Dans MVC/MVP, c'est le Controller/Presenter qui appelle le backend.
 *   Dans VIPER, cette responsabilité est déléguée à l'Interactor.
 * - L'Interactor ne connaît ni la View ni le Presenter (seulement le OutputPort).
 * - VIPER applique strictement le Single Responsibility Principle.
 */
public class CalculatorInteractor {

    private final CalculatorAdapter backend;
    private final CalculatorEntity entity;
    private CalculatorInteractorOutput outputPort;

    public CalculatorInteractor(CalculatorAdapter backend) {
        this.backend = backend;
        this.entity  = CalculatorEntity.empty();
    }

    public void setOutputPort(CalculatorInteractorOutput outputPort) {
        this.outputPort = outputPort;
    }

    // -------------------------------------------------------------------------
    // Use cases
    // -------------------------------------------------------------------------

    /**
     * Use case "calculer" :
     * Parse les entrées, met à jour l'Entity, appelle le backend,
     * retourne le résultat via le outputPort.
     */
    public void calculate(String rawA, String rawB, String operation) {
        try {
            int a  = Integer.parseInt(rawA);
            int b  = Integer.parseInt(rawB);

            entity.setOperandA(a);
            entity.setOperandB(b);
            entity.setOperation(operation);

            final int result;
            switch (operation) {
                case "+":
                    result = backend.add(a, b);
                    break;
                case "-":
                    result = backend.subtract(a, b);
                    break;
                case "*":
                    result = backend.multiply(a, b);
                    break;
                default:
                    throw new IllegalArgumentException("Opération inconnue : " + operation);
            }

            entity.setResult(result);
            outputPort.onCalculationSuccess(entity);

        } catch (NumberFormatException ex) {
            outputPort.onCalculationError("saisie invalide (entiers requis)");
        } catch (IllegalArgumentException ex) {
            outputPort.onCalculationError(ex.getMessage());
        }
    }

    /**
     * Use case "reset" :
     * Réinitialise l'Entity et notifie le outputPort.
     */
    public void reset() {
        entity.setOperandA(0);
        entity.setOperandB(0);
        entity.setOperation("+");
        entity.setResult(0);
        outputPort.onResetDone(entity);
    }
}