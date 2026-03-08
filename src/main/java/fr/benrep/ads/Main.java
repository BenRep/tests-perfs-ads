package fr.benrep.ads;

import fr.benrep.ads.backend.adapter.CalculatorAdapter;
import fr.benrep.ads.mvc.CalculatorController;
import fr.benrep.ads.mvi.CalculatorStore;
import fr.benrep.ads.mvvm.CalculatorModel;
import fr.benrep.ads.mvvm.CalculatorView;
import fr.benrep.ads.mvvm.CalculatorViewModel;
import fr.benrep.ads.viper.CalculatorPresenter;
import fr.benrep.ads.viper.CalculatorRouter;

import javax.swing.*;

/**
 * POINT D'ENTRÉE PRINCIPAL
 *
 * Permet de choisir le modèle de conception à lancer en modifiant
 * la variable {@code designPattern}.
 *
 * Valeurs supportées :
 *   "MVC"   → Model-View-Controller
 *   "MVP"   → Model-View-Presenter
 *   "MVVM"  → Model-View-ViewModel
 *   "MVI"   → Model-View-Intent
 *   "VIPER" → View-Interactor-Presenter-Entity-Router
 *
 * Le backend hexagonal (CalculatorAdapter) est instancié une seule fois
 * et injecté dans le pattern sélectionné. Tous les patterns utilisent
 * exactement le même backend, ce qui garantit l'équité des mesures.
 */
public class Main {

    // ==========================================================================
    // CHOISIR LE MODÈLE DE CONCEPTION ICI
    // ==========================================================================
    private static final String designPattern = "MVC";
    // ==========================================================================

    public static void main(String[] args) {
        // Instanciation unique du backend hexagonal
        CalculatorAdapter backend = CalculatorAdapter.createDefault();

        System.out.println("=================================================");
        System.out.println("  POC Modèles de Conception Frontend - Java Swing");
        System.out.println("  Pattern sélectionné : " + designPattern);
        System.out.println("=================================================");

        SwingUtilities.invokeLater(() -> launch(designPattern, backend));
    }

    private static void launch(String pattern, CalculatorAdapter backend) {
        switch (pattern.toUpperCase()) {

            // ------------------------------------------------------------------
            // MVC
            // ------------------------------------------------------------------
            case "MVC": {
                fr.benrep.ads.mvc.CalculatorModel model      = new fr.benrep.ads.mvc.CalculatorModel();
                fr.benrep.ads.mvc.CalculatorView view       = new fr.benrep.ads.mvc.CalculatorView(model);
                CalculatorController controller = new CalculatorController(view, model, backend);
                controller.start();
                break;
            }

            // ------------------------------------------------------------------
            // MVP
            // ------------------------------------------------------------------
            case "MVP": {
                fr.benrep.ads.mvp.CalculatorModel model     = new fr.benrep.ads.mvp.CalculatorModel();
                fr.benrep.ads.mvp.CalculatorView view      = new fr.benrep.ads.mvp.CalculatorView();
                fr.benrep.ads.mvp.CalculatorPresenter presenter = new fr.benrep.ads.mvp.CalculatorPresenter(view, model, backend);
                view.setPresenter(presenter);
                presenter.start();
                break;
            }

            // ------------------------------------------------------------------
            // MVVM
            // ------------------------------------------------------------------
            case "MVVM": {
                CalculatorModel model     = new CalculatorModel();
                CalculatorViewModel viewModel = new CalculatorViewModel(model, backend);
                CalculatorView view      = new CalculatorView();
                view.bindViewModel(viewModel);
                view.setVisible(true);
                break;
            }

            // ------------------------------------------------------------------
            // MVI
            // ------------------------------------------------------------------
            case "MVI": {
                CalculatorStore store = new CalculatorStore(backend);
                fr.benrep.ads.mvi.CalculatorView view  = new fr.benrep.ads.mvi.CalculatorView();
                view.connectStore(store);
                view.setVisible(true);
                break;
            }

            // ------------------------------------------------------------------
            // VIPER
            // ------------------------------------------------------------------
            case "VIPER": {
                CalculatorPresenter presenter =
                        CalculatorRouter.build(backend);
                presenter.start();
                break;
            }

            // ------------------------------------------------------------------
            // Valeur inconnue
            // ------------------------------------------------------------------
            default:
                System.err.println("[ERREUR] Pattern inconnu : \"" + pattern + "\"");
                System.err.println("Valeurs acceptées : MVC, MVP, MVVM, MVI, VIPER");
                System.exit(1);
        }
    }
}