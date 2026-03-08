package fr.benrep.ads.viper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * VIPER - PRESENTER
 *
 * Responsabilités :
 * - Recevoir les événements de la View et les déléguer à l'Interactor
 * - Implémenter CalculatorInteractorOutput pour recevoir les résultats
 * - Formater les données (Entity → données affichables) et mettre à jour la View
 * - Logger les temps de réponse
 *
 * Différences clés avec MVP Presenter :
 * - Le Presenter VIPER ne contient pas de logique métier (délégué à l'Interactor)
 * - Il est un "thin presenter" : reçoit les résultats, formate, affiche
 * - Il implémente un output port → découplage strict avec l'Interactor
 * - Le Router est une responsabilité séparée (navigation)
 *
 * MESURE DE PERFORMANCE :
 * Chaque action loggue : [VIPER][ACTION] Temps de réponse : X ms
 * Le temps inclut Presenter → Interactor → backend → Presenter → View.
 */
public class CalculatorPresenter implements CalculatorInteractorOutput {

    private static final Logger log = LoggerFactory.getLogger(CalculatorPresenter.class);

    private final CalculatorViewContract view;
    private final CalculatorInteractor interactor;

    // Pour mesurer le temps de bout en bout
    private long actionStartTime = 0;
    private String currentAction = "";

    public CalculatorPresenter(CalculatorViewContract view,
            CalculatorInteractor interactor) {
        this.view       = view;
        this.interactor = interactor;
        interactor.setOutputPort(this);
    }

    // -------------------------------------------------------------------------
    // Méthodes appelées par la View
    // -------------------------------------------------------------------------

    /** La View signale que l'utilisateur a cliqué "Calculer". */
    public void onCalculateRequested() {
        actionStartTime = System.nanoTime();
        currentAction   = "CALCULATE";
        interactor.calculate(view.getFieldA(), view.getFieldB(), view.getSelectedOperation());
    }

    /** La View signale que l'utilisateur a cliqué "Reset". */
    public void onResetRequested() {
        actionStartTime = System.nanoTime();
        currentAction   = "RESET";
        interactor.reset();
    }

    // -------------------------------------------------------------------------
    // Implémentation de CalculatorInteractorOutput (callbacks de l'Interactor)
    // -------------------------------------------------------------------------

    @Override
    public void onCalculationSuccess(CalculatorEntity entity) {
        view.displayFieldA(String.valueOf(entity.getOperandA()));
        view.displayFieldB(String.valueOf(entity.getOperandB()));
        view.displayOperation(entity.getOperation());
        view.displayResult(String.valueOf(entity.getResult()));
        view.displayStatus("Calcul effectué : "
                + entity.getOperandA() + " "
                + entity.getOperation() + " "
                + entity.getOperandB() + " = "
                + entity.getResult());
        logElapsed();
    }

    @Override
    public void onCalculationError(String errorMessage) {
        view.displayStatus("Erreur : " + errorMessage);
        log.warn("[VIPER][{}] Erreur : {}", currentAction, errorMessage);
        logElapsed();
    }

    @Override
    public void onResetDone(CalculatorEntity entity) {
        view.displayFieldA(String.valueOf(entity.getOperandA()));
        view.displayFieldB(String.valueOf(entity.getOperandB()));
        view.displayOperation(entity.getOperation());
        view.displayResult(String.valueOf(entity.getResult()));
        view.displayStatus("Réinitialisé");
        logElapsed();
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private void logElapsed() {
        long elapsed = (System.nanoTime() - actionStartTime) / 1_000;
        log.info("[VIPER][{}] Temps de réponse : {} µs", currentAction, elapsed);
    }

    /** Lance la fenêtre. */
    public void start() {
        view.show();
    }
}