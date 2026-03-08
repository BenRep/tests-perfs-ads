package fr.benrep.ads.mvc;

import fr.benrep.ads.common.ViewConstants;

import javax.swing.*;
import java.awt.*;

/**
 * MVC - VIEW
 *
 * Responsabilités :
 * - Afficher l'état du Model (lecture directe du Model via référence)
 * - Déclarer les composants Swing
 * - Déléguer TOUTES les actions utilisateur au Controller via des listeners
 * - Ne contient aucune logique métier
 *
 * Dans MVC classique (Smalltalk), la View observe le Model directement.
 * Ici la View s'enregistre comme listener du Model pour se rafraîchir.
 */
public class CalculatorView extends JFrame {

    // Champs de saisie
    private final JTextField fieldA   = new JTextField(ViewConstants.FIELD_COLUMNS);
    private final JTextField fieldB   = new JTextField(ViewConstants.FIELD_COLUMNS);

    // Sélecteur d'opération
    private final JComboBox<String> operationBox =
            new JComboBox<>(ViewConstants.OPERATIONS);

    // Affichage du résultat et du statut
    private final JLabel resultLabel  = new JLabel(ViewConstants.RESULT_DEFAULT);
    private final JLabel statusLabel  = new JLabel(ViewConstants.STATUS_DEFAULT);

    // Boutons
    private final JButton calculateBtn = new JButton(ViewConstants.BTN_CALCULATE);
    private final JButton resetBtn     = new JButton(ViewConstants.BTN_RESET);

    private final CalculatorModel model;

    public CalculatorView(CalculatorModel model) {
        this.model = model;
        buildUI();
        // La View s'abonne au Model pour se rafraîchir automatiquement
        model.addChangeListener(this::refresh);
    }

    // -------------------------------------------------------------------------
    // Construction de l'interface
    // -------------------------------------------------------------------------

    private void buildUI() {
        setTitle(ViewConstants.TITLE + " [MVC]");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(ViewConstants.WINDOW_WIDTH, ViewConstants.WINDOW_HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);

        JPanel main = new JPanel(new GridBagLayout());
        main.setBorder(BorderFactory.createEmptyBorder(
                ViewConstants.PADDING, ViewConstants.PADDING,
                ViewConstants.PADDING, ViewConstants.PADDING));
        GridBagConstraints gbc = ViewConstants.defaultGbc();

        // --- Ligne 1 : Opérande A ---
        gbc.gridx = 0; gbc.gridy = 0;
        main.add(new JLabel("Opérande A :"), gbc);
        gbc.gridx = 1;
        main.add(fieldA, gbc);

        // --- Ligne 2 : Opération ---
        gbc.gridx = 0; gbc.gridy = 1;
        main.add(new JLabel("Opération :"), gbc);
        gbc.gridx = 1;
        main.add(operationBox, gbc);

        // --- Ligne 3 : Opérande B ---
        gbc.gridx = 0; gbc.gridy = 2;
        main.add(new JLabel("Opérande B :"), gbc);
        gbc.gridx = 1;
        main.add(fieldB, gbc);

        // --- Ligne 4 : Résultat ---
        gbc.gridx = 0; gbc.gridy = 3;
        main.add(new JLabel("Résultat :"), gbc);
        gbc.gridx = 1;
        resultLabel.setFont(ViewConstants.RESULT_FONT);
        main.add(resultLabel, gbc);

        // --- Ligne 5 : Boutons ---
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, ViewConstants.BTN_GAP, 0));
        btnPanel.add(calculateBtn);
        btnPanel.add(resetBtn);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        main.add(btnPanel, gbc);

        // --- Ligne 6 : Statut ---
        gbc.gridy = 5; gbc.gridwidth = 2;
        statusLabel.setForeground(ViewConstants.STATUS_COLOR);
        main.add(statusLabel, gbc);

        add(main);
    }

    // -------------------------------------------------------------------------
    // Rafraîchissement depuis le Model
    // -------------------------------------------------------------------------

    /** Appelé par le Model via le listener ; relit l'état et met à jour l'affichage. */
    private void refresh() {
        fieldA.setText(String.valueOf(model.getOperandA()));
        fieldB.setText(String.valueOf(model.getOperandB()));
        operationBox.setSelectedItem(model.getOperation());
        resultLabel.setText(String.valueOf(model.getResult()));
        statusLabel.setText(model.getStatusMessage());
    }

    // -------------------------------------------------------------------------
    // Accesseurs pour le Controller
    // -------------------------------------------------------------------------

    public String getFieldA()          { return fieldA.getText().trim(); }
    public String getFieldB()          { return fieldB.getText().trim(); }
    public String getSelectedOperation() { return (String) operationBox.getSelectedItem(); }

    public JButton getCalculateButton() { return calculateBtn; }
    public JButton getResetButton()     { return resetBtn; }
}