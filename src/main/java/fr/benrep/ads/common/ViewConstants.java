package fr.benrep.ads.common;

import java.awt.*;
import java.awt.GridBagConstraints;
import java.awt.Insets;

/**
 * CONSTANTES COMMUNES à toutes les vues Swing.
 *
 * Garantit que toutes les implémentations ont :
 * - Les mêmes dimensions de fenêtre
 * - Les mêmes textes de boutons et libellés
 * - Le même layout (GridBagConstraints)
 * - La même police pour le résultat
 *
 * Toute modification ici s'applique uniformément à tous les patterns.
 * Cela permet de comparer les patterns à interface strictement identique.
 */
public final class ViewConstants {

    private ViewConstants() {} // Classe utilitaire, non instanciable

    // -------------------------------------------------------------------------
    // Fenêtre
    // -------------------------------------------------------------------------
    public static final String TITLE         = "Calculator POC";
    public static final int    WINDOW_WIDTH  = 420;
    public static final int    WINDOW_HEIGHT = 280;
    public static final int    PADDING       = 20;

    // -------------------------------------------------------------------------
    // Champs
    // -------------------------------------------------------------------------
    public static final int      FIELD_COLUMNS = 10;
    public static final String[] OPERATIONS    = new String[] { "+", "-", "*" };

    // -------------------------------------------------------------------------
    // Valeurs par défaut affichées
    // -------------------------------------------------------------------------
    public static final String RESULT_DEFAULT = "0";
    public static final String STATUS_DEFAULT = "Prêt";

    // -------------------------------------------------------------------------
    // Boutons
    // -------------------------------------------------------------------------
    public static final String BTN_CALCULATE = "Calculer";
    public static final String BTN_RESET     = "Reset";
    public static final int    BTN_GAP       = 10;

    // -------------------------------------------------------------------------
    // Style
    // -------------------------------------------------------------------------
    public static final Font  RESULT_FONT  = new Font("SansSerif", Font.BOLD, 18);
    public static final Color STATUS_COLOR = new Color(60, 120, 60);

    // -------------------------------------------------------------------------
    // Layout helper
    // -------------------------------------------------------------------------

    /**
     * Retourne un GridBagConstraints avec les valeurs communes par défaut.
     * Chaque vue peut le modifier localement sans polluer les autres.
     */
    public static GridBagConstraints defaultGbc() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets     = new Insets(6, 8, 6, 8);
        gbc.fill       = GridBagConstraints.HORIZONTAL;
        gbc.anchor     = GridBagConstraints.WEST;
        gbc.gridwidth  = 1;
        gbc.weightx    = 1.0;
        return gbc;
    }
}