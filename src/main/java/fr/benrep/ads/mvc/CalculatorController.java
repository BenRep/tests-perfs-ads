package fr.benrep.ads.mvc;

import fr.benrep.ads.backend.adapter.CalculatorAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MVC - CONTROLLER
 * <p>
 * Responsabilités :
 * - Recevoir les événements utilisateur depuis la View
 * - Interpréter ces événements et orchestrer Model + Backend
 * - Mettre à jour le Model en conséquence (la View se rafraîchit automatiquement)
 * - Logger les temps de réponse pour les mesures de performance
 * <p>
 * Le Controller connaît la View (pour lire les inputs) et le Model (pour écrire).
 * La View observe le Model directement → le Controller ne pilote pas la Vue manuellement.
 * <p>
 * MESURE DE PERFORMANCE :
 * Chaque action loggue : [MVC][ACTION] Temps de réponse : X ms
 * Ces logs permettent de comparer les architectures à action équivalente.
 */
public class CalculatorController {

    private static final Logger log = LoggerFactory.getLogger(CalculatorController.class);

    private final CalculatorView view;
    private final CalculatorModel model;
    private final CalculatorAdapter backend;

    public CalculatorController(CalculatorView view,
            CalculatorModel model,
            CalculatorAdapter backend) {
        this.view    = view;
        this.model   = model;
        this.backend = backend;
        bindActions();
    }

    // -------------------------------------------------------------------------
    // Binding des actions
    // -------------------------------------------------------------------------

    private void bindActions() {
        view.getCalculateButton().addActionListener(e -> onCalculate());
        view.getResetButton().addActionListener(e -> onReset());
    }

    // -------------------------------------------------------------------------
    // Handlers d'actions
    // -------------------------------------------------------------------------

    /**
     * Déclenche un appel au backend, met à jour le Model avec le résultat.
     * Log le temps total de traitement de l'action (parsing + appel backend + écriture modèle).
     */
    private void onCalculate() {
        long start = System.nanoTime();

        try {
            int a = Integer.parseInt(view.getFieldA());
            int b = Integer.parseInt(view.getFieldB());
            String op = view.getSelectedOperation();

            // Mise à jour du modèle avec les valeurs saisies
            model.setOperandA(a);
            model.setOperandB(b);
            model.setOperation(op);

            // Appel backend via l'adapter hexagonal
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
            model.setStatusMessage("Calcul effectué : " + a + " " + op + " " + b + " = " + result);

        } catch (NumberFormatException ex) {
            model.setStatusMessage("Erreur : saisie invalide (entiers requis)");
            log.warn("[MVC][CALCULATE] Saisie invalide : {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            model.setStatusMessage("Erreur : " + ex.getMessage());
            log.warn("[MVC][CALCULATE] {}", ex.getMessage());
        } finally {
            long elapsed = (System.nanoTime() - start) / 1_000;
            log.info("[MVC][CALCULATE] Temps de réponse : {} µs", elapsed);
        }
    }

    /**
     * Réinitialise le modèle (modification locale, sans appel backend).
     * Log le temps de traitement pour comparer avec onCalculate().
     */
    private void onReset() {
        long start = System.nanoTime();

        model.reset();

        long elapsed = (System.nanoTime() - start) / 1_000;
        log.info("[MVC][RESET] Temps de réponse : {} µs", elapsed);
    }

    /** Lance la fenêtre. */
    public void start() {
        view.setVisible(true);
    }
}