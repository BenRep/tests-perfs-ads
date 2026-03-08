package fr.benrep.ads.mvi;

/**
 * MVI - INTENT
 *
 * Représente toutes les intentions (actions) que l'utilisateur peut exprimer.
 * Dans MVI, le flux est strictement unidirectionnel :
 *   Intent → Model (State) → View
 *
 * Chaque Intent est un objet immuable décrivant "ce que l'utilisateur veut faire",
 * jamais "comment le faire". C'est le Store/Reducer qui interprète l'Intent.
 *
 * Pattern sealed class simulé avec une classe abstraite + classes statiques internes
 * (Java 11 ne supporte pas encore les sealed classes, introduites en Java 17).
 */
public abstract class CalculatorIntent {

    private CalculatorIntent() {} // Empêche l'héritage externe

    /** L'utilisateur demande un calcul avec les valeurs saisies. */
    public static final class Calculate extends CalculatorIntent {
        public final String rawA;
        public final String rawB;
        public final String operation;

        public Calculate(String rawA, String rawB, String operation) {
            this.rawA = rawA;
            this.rawB = rawB;
            this.operation = operation;
        }
    }

    /** L'utilisateur demande la réinitialisation du formulaire. */
    public static final class Reset extends CalculatorIntent {}
}