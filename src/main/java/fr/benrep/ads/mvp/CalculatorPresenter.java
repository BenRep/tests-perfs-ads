package fr.benrep.ads.mvp;

import fr.benrep.ads.backend.adapter.CalculatorAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MVP - PRESENTER
 *
 * Responsabilités :
 * - Recevoir les événements de la View via des méthodes publiques
 * - Lire la View (inputs) et mettre à jour le Model
 * - Lire le Model et mettre à jour la View (manuellement, pas d'Observer)
 * - Appeler le backend via l'adapter hexagonal
 * - Logger les temps de réponse
 *
 * Différence clé avec MVC Controller :
 * - Le Presenter NE connaît PAS les composants Swing (JTextField, etc.)
 *   Il passe uniquement par l'interface CalculatorViewContract.
 * - C'est le Presenter (et non la View) qui orchestre la mise à jour de l'affichage.
 * - Le Presenter est donc 100% testable en isolation (sans Swing).
 *
 * MESURE DE PERFORMANCE :
 * Chaque action loggue : [MVP][ACTION] Temps de réponse : X ms
 */
public class CalculatorPresenter {

    private static final Logger log = LoggerFactory.getLogger(CalculatorPresenter.class);

    private final CalculatorViewContract view;
    private final CalculatorModel model;
    private final CalculatorAdapter backend;

    public CalculatorPresenter(CalculatorViewContract view,
            CalculatorModel model,
            CalculatorAdapter backend) {
        this.view    = view;
        this.model   = model;
        this.backend = backend;
    }

    // -------------------------------------------------------------------------
    // Handlers d'événements (appelés par la View)
    // -------------------------------------------------------------------------

    /**
     * Traite le clic sur "Calculer".
     * Lit la View → met à jour le Model → appelle le backend → met à jour la View.
     */
    public void onCalculate() {
        long start = System.nanoTime();

        try {
            int a  = Integer.parseInt(view.getFieldA());
            int b  = Integer.parseInt(view.getFieldB());
            String op = view.getSelectedOperation();

            // Mise à jour du modèle
            model.setOperandA(a);
            model.setOperandB(b);
            model.setOperation(op);

            // Appel backend
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
            String status = "Calcul effectué : " + a + " " + op + " " + b + " = " + result;
            model.setStatusMessage(status);

            // Le Presenter met à jour la View MANUELLEMENT (pas d'Observer)
            view.setResultLabel(String.valueOf(result));
            view.setStatusLabel(status);

        } catch (NumberFormatException ex) {
            String msg = "Erreur : saisie invalide (entiers requis)";
            view.setStatusLabel(msg);
            log.warn("[MVP][CALCULATE] Saisie invalide : {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            view.setStatusLabel("Erreur : " + ex.getMessage());
            log.warn("[MVP][CALCULATE] {}", ex.getMessage());
        } finally {
            long elapsed = (System.nanoTime() - start) / 1_000;
            log.info("[MVP][CALCULATE] Temps de réponse : {} µs", elapsed);
        }
    }

    /**
     * Traite le clic sur "Reset".
     * Réinitialise le modèle puis met à jour la View manuellement.
     */
    public void onReset() {
        long start = System.nanoTime();

        model.reset();

        // Mise à jour manuelle de la View depuis le modèle réinitialisé
        view.setFieldA(String.valueOf(model.getOperandA()));
        view.setFieldB(String.valueOf(model.getOperandB()));
        view.setOperation(model.getOperation());
        view.setResultLabel(String.valueOf(model.getResult()));
        view.setStatusLabel(model.getStatusMessage());

        long elapsed = (System.nanoTime() - start) / 1_000;
        log.info("[MVP][RESET] Temps de réponse : {} µs", elapsed);
    }

    /** Lance la fenêtre. */
    public void start() {
        view.show();
    }
}