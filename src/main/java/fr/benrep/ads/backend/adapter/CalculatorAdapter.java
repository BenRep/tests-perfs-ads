package fr.benrep.ads.backend.adapter;

import fr.benrep.ads.backend.domain.port.CalculatorUseCase;
import fr.benrep.ads.backend.domain.service.CalculatorService;

/**
 * ADAPTER de l'architecture hexagonale.
 * Point d'entrée unique exposé au frontend, quel que soit son modèle de conception.
 * <p>
 * Rôle de l'adapter :
 * <ul>
 *     <li>Instancier et wirer les dépendances du domaine (ici CalculatorService)</li>
 *     <li>Exposer une API stable vers le frontend via l'interface CalculatorUseCase</li>
 *     <li>Permettre de brancher facilement d'autres implémentations (mock, stub, etc.) sans modifier le code frontend</li>
 * </ul>
 * <p>
 * Dans une architecture hexagonale complète, cet adapter serait un "driving adapter"
 * (côté gauche de l'hexagone), gérant par exemple REST, gRPC, ou IPC.
 * Ici, il est simplifié en appel direct en mémoire pour isoler les mesures frontend.
 */
public class CalculatorAdapter {

    private final CalculatorUseCase useCase;

    /**
     * Constructeur avec injection de dépendance.
     * Le frontend peut injecter n'importe quelle implémentation de CalculatorUseCase.
     */
    public CalculatorAdapter(CalculatorUseCase useCase) {
        this.useCase = useCase;
    }

    /**
     * Factory method pour créer un adapter avec l'implémentation par défaut.
     * Simplifie l'instanciation côté frontend.
     */
    public static CalculatorAdapter createDefault() {
        return new CalculatorAdapter(new CalculatorService());
    }

    /**
     * Délègue l'addition au domaine.
     */
    public int add(int a, int b) {
        return useCase.add(a, b);
    }

    /**
     * Délègue la soustraction au domaine.
     */
    public int subtract(int a, int b) {
        return useCase.subtract(a, b);
    }

    /**
     * Délègue la multiplication au domaine.
     */
    public int multiply(int a, int b) {
        return useCase.multiply(a, b);
    }
}