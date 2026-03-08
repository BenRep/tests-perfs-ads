package fr.benrep.ads.mvi;

import fr.benrep.ads.backend.adapter.CalculatorAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * MVI - STORE
 *
 * Cœur du pattern MVI. Le Store :
 * 1. Détient le State courant (source de vérité unique)
 * 2. Reçoit les Intents via dispatch()
 * 3. Applique le reducer pour produire un nouveau State
 * 4. Notifie les observers (la View) du nouveau State
 *
 * Analogie avec Redux (JavaScript) :
 * - dispatch()  → store.dispatch(action)
 * - reduce()    → reducer(state, action) => newState
 * - subscribe() → store.subscribe(listener)
 *
 * MESURE DE PERFORMANCE :
 * Chaque dispatch loggue : [MVI][INTENT] Temps de réponse : X µs
 */
public class CalculatorStore {

    private static final Logger log = LoggerFactory.getLogger(CalculatorStore.class);

    private CalculatorState currentState = CalculatorState.initial();
    private final CalculatorAdapter backend;
    private final List<Consumer<CalculatorState>> stateObservers = new ArrayList<>();

    public CalculatorStore(CalculatorAdapter backend) {
        this.backend = backend;
    }

    // -------------------------------------------------------------------------
    // API publique
    // -------------------------------------------------------------------------

    /**
     * Dispatche un Intent : réduit l'état, notifie les observers.
     * Point d'entrée unique pour toute modification d'état.
     */
    public void dispatch(CalculatorIntent intent) {
        long start = System.nanoTime();

        CalculatorState newState = reduce(currentState, intent);
        currentState = newState;
        notifyObservers(newState);

        String intentName = intent.getClass().getSimpleName().toUpperCase();
        long elapsed = (System.nanoTime() - start) / 1_000;
        log.info("[MVI][{}] Temps de réponse : {} µs", intentName, elapsed);
    }

    /** Abonne un observateur au State. Appelé immédiatement avec l'état courant. */
    public void subscribe(Consumer<CalculatorState> observer) {
        stateObservers.add(observer);
        observer.accept(currentState); // notification initiale
    }

    public CalculatorState getState() {
        return currentState;
    }

    // -------------------------------------------------------------------------
    // Reducer (fonction pure : (State, Intent) → State)
    // -------------------------------------------------------------------------

    /**
     * Fonction de réduction pure : produit un nouveau State depuis l'ancien State
     * et un Intent. Ne modifie jamais l'ancien State.
     */
    private CalculatorState reduce(CalculatorState state, CalculatorIntent intent) {
        if (intent instanceof CalculatorIntent.Calculate) {
            return reduceCalculate(state, (CalculatorIntent.Calculate) intent);
        } else if (intent instanceof CalculatorIntent.Reset) {
            return CalculatorState.reset();
        }
        log.warn("[MVI] Intent non géré : {}", intent.getClass().getName());
        return state;
    }

    private CalculatorState reduceCalculate(CalculatorState state,
            CalculatorIntent.Calculate intent) {
        try {
            int a  = Integer.parseInt(intent.rawA);
            int b  = Integer.parseInt(intent.rawB);
            String op = intent.operation;

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

            return state.withResult(a, b, op, result);

        } catch (NumberFormatException ex) {
            log.warn("[MVI][CALCULATE] Saisie invalide : {}", ex.getMessage());
            return state.withError("saisie invalide (entiers requis)");
        } catch (IllegalArgumentException ex) {
            log.warn("[MVI][CALCULATE] {}", ex.getMessage());
            return state.withError(ex.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // Notification
    // -------------------------------------------------------------------------

    private void notifyObservers(CalculatorState state) {
        stateObservers.forEach(obs -> obs.accept(state));
    }
}