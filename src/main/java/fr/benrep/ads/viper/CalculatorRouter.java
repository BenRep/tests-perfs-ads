package fr.benrep.ads.viper;

import fr.benrep.ads.backend.adapter.CalculatorAdapter;

/**
 * VIPER - ROUTER
 *
 * Responsabilités :
 * - Assembler (wirer) tous les composants VIPER du module
 * - Gérer la navigation entre modules (ici trivial : une seule vue)
 *
 * C'est la responsabilité qui distingue le plus VIPER des autres patterns.
 * Dans MVC/MVP/MVVM, le wiring est souvent fait dans le Main ou dans la View.
 * Dans VIPER, c'est une responsabilité EXPLICITE d'une classe dédiée.
 *
 * Le Router est le seul endroit qui connaît toutes les couches :
 * View, Presenter, Interactor, Entity, Backend.
 * Il garantit que les autres couches ne se connaissent qu'à travers
 * leurs interfaces respectives.
 */
public class CalculatorRouter {

    /**
     * Crée et câble tous les composants du module Calculator.
     * @return le Presenter prêt à démarrer
     */
    public static CalculatorPresenter build(CalculatorAdapter backend) {
        // 1. Créer la View concrète
        CalculatorView view = new CalculatorView();

        // 2. Créer l'Interactor avec le backend
        CalculatorInteractor interactor = new CalculatorInteractor(backend);

        // 3. Créer le Presenter (injecte View + Interactor)
        CalculatorPresenter presenter = new CalculatorPresenter(view, interactor);

        // 4. Injecter le Presenter dans la View (pour le binding des actions)
        view.setPresenter(presenter);

        return presenter;
    }
}