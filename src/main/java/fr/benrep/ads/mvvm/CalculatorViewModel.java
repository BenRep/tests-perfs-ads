package fr.benrep.ads.mvvm;

import fr.benrep.ads.backend.adapter.CalculatorAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MVVM - VIEWMODEL
 *
 * Responsabilités :
 * - Exposer l'état de l'UI via des ObservableProperty (data binding)
 * - Exposer des commandes (méthodes) que la View peut appeler
 * - Orchestrer Model + Backend sans connaître la View
 * - Logger les temps de réponse
 *
 * Différences clés avec MVP Presenter :
 * - Le ViewModel ne connaît PAS la View (pas de référence, pas d'interface)
 * - La View s'abonne aux propriétés observables : c'est le data binding
 * - Plusieurs views pourraient s'abonner au même ViewModel
 * - Encore plus testable qu'un Presenter MVP (zéro couplage avec la View)
 *
 * MESURE DE PERFORMANCE :
 * Chaque commande loggue : [MVVM][COMMAND] Temps de réponse : X ms
 */
public class CalculatorViewModel {

    private static final Logger log = LoggerFactory.getLogger(CalculatorViewModel.class);

    // --- Propriétés observables exposées à la View ---
    public final ObservableProperty<String> displayA       = new ObservableProperty<>("0");
    public final ObservableProperty<String> displayB       = new ObservableProperty<>("0");
    public final ObservableProperty<String> displayOp      = new ObservableProperty<>("+");
    public final ObservableProperty<String> displayResult  = new ObservableProperty<>("0");
    public final ObservableProperty<String> statusMessage  = new ObservableProperty<>("Prêt");

    private final CalculatorModel model;
    private final CalculatorAdapter backend;

    public CalculatorViewModel(CalculatorModel model, CalculatorAdapter backend) {
        this.model   = model;
        this.backend = backend;
    }

    // -------------------------------------------------------------------------
    // Commandes (appelées par la View, sans que le VM connaisse la View)
    // -------------------------------------------------------------------------

    /**
     * Commande "Calculer" : lit les propriétés observables,
     * met à jour le modèle, appelle le backend, met à jour les propriétés.
     */
    public void commandCalculate(String rawA, String rawB, String op) {
        long start = System.nanoTime();

        try {
            int a = Integer.parseInt(rawA);
            int b = Integer.parseInt(rawB);

            model.setOperandA(a);
            model.setOperandB(b);
            model.setOperation(op);

            final int result;
            switch (op) {
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
                    throw new IllegalArgumentException("Opération inconnue : " + op);
            }

            model.setResult(result);

            // Mise à jour des propriétés → la View se met à jour automatiquement via binding
            displayA.set(String.valueOf(a));
            displayB.set(String.valueOf(b));
            displayOp.set(op);
            displayResult.set(String.valueOf(result));
            statusMessage.set("Calcul effectué : " + a + " " + op + " " + b + " = " + result);

        } catch (NumberFormatException ex) {
            statusMessage.set("Erreur : saisie invalide (entiers requis)");
            log.warn("[MVVM][CALCULATE] Saisie invalide : {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            statusMessage.set("Erreur : " + ex.getMessage());
            log.warn("[MVVM][CALCULATE] {}", ex.getMessage());
        } finally {
            long elapsed = (System.nanoTime() - start) / 1_000;
            log.info("[MVVM][CALCULATE] Temps de réponse : {} µs", elapsed);
        }
    }

    /**
     * Commande "Reset" : réinitialise le modèle et met à jour toutes les propriétés.
     */
    public void commandReset() {
        long start = System.nanoTime();

        model.reset();

        displayA.set(String.valueOf(model.getOperandA()));
        displayB.set(String.valueOf(model.getOperandB()));
        displayOp.set(model.getOperation());
        displayResult.set(String.valueOf(model.getResult()));
        statusMessage.set("Réinitialisé");

        long elapsed = (System.nanoTime() - start) / 1_000;
        log.info("[MVVM][RESET] Temps de réponse : {} µs", elapsed);
    }
}