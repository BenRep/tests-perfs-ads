package fr.benrep.ads.mvi;

import fr.benrep.ads.common.ViewConstants;

import javax.swing.*;
import java.awt.*;

/**
 * MVI - VIEW
 *
 * Responsabilités :
 * - Construire les composants Swing
 * - Convertir les actions utilisateur en Intents et les dispatcher au Store
 * - S'abonner au Store pour re-rendre l'UI à chaque nouveau State
 * - Ne contient AUCUNE logique ni état local
 *
 * Différences clés avec MVVM View :
 * - La View ne lit pas des ObservableProperty séparées :
 *   elle reçoit un State COMPLET à chaque changement (render(state))
 * - La View ne "modifie" rien : elle dispatche des Intents au Store
 * - C'est le flux le plus unidirectionnel : Intent → Store → State → View
 *
 * Analogie React : chaque appel render(state) est comme un re-render React
 * piloté par le Store (comme Redux + React).
 */
public class CalculatorView extends JFrame {

    private final JTextField fieldA    = new JTextField(ViewConstants.FIELD_COLUMNS);
    private final JTextField fieldB    = new JTextField(ViewConstants.FIELD_COLUMNS);
    private final JComboBox<String> operationBox =
            new JComboBox<>(ViewConstants.OPERATIONS);
    private final JLabel resultLabel   = new JLabel(ViewConstants.RESULT_DEFAULT);
    private final JLabel statusLabel   = new JLabel(ViewConstants.STATUS_DEFAULT);
    private final JButton calculateBtn = new JButton(ViewConstants.BTN_CALCULATE);
    private final JButton resetBtn     = new JButton(ViewConstants.BTN_RESET);

    public CalculatorView() {
        buildUI();
    }

    /**
     * Connecte la View au Store :
     * - Actions → Intents → Store.dispatch()
     * - Store.subscribe() → render(state)
     */
    public void connectStore(CalculatorStore store) {
        // View → Store : les actions deviennent des Intents
        calculateBtn.addActionListener(e -> store.dispatch(
                new CalculatorIntent.Calculate(
                        fieldA.getText().trim(),
                        fieldB.getText().trim(),
                        (String) operationBox.getSelectedItem())));

        resetBtn.addActionListener(e -> store.dispatch(new CalculatorIntent.Reset()));

        // Store → View : abonnement au flux de States
        store.subscribe(this::render);
    }

    /**
     * Re-rend l'UI complètement depuis un State immuable.
     * Méthode pure : même State → même affichage, toujours.
     */
    private void render(CalculatorState state) {
        fieldA.setText(String.valueOf(state.operandA));
        fieldB.setText(String.valueOf(state.operandB));
        operationBox.setSelectedItem(state.operation);
        resultLabel.setText(String.valueOf(state.result));
        statusLabel.setText(state.statusMessage);
        statusLabel.setForeground(state.isError
                ? java.awt.Color.RED
                : ViewConstants.STATUS_COLOR);
    }

    private void buildUI() {
        setTitle(ViewConstants.TITLE + " [MVI]");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(ViewConstants.WINDOW_WIDTH, ViewConstants.WINDOW_HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);

        JPanel main = new JPanel(new GridBagLayout());
        main.setBorder(BorderFactory.createEmptyBorder(
                ViewConstants.PADDING, ViewConstants.PADDING,
                ViewConstants.PADDING, ViewConstants.PADDING));
        GridBagConstraints gbc = ViewConstants.defaultGbc();

        gbc.gridx = 0; gbc.gridy = 0;
        main.add(new JLabel("Opérande A :"), gbc);
        gbc.gridx = 1;
        main.add(fieldA, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        main.add(new JLabel("Opération :"), gbc);
        gbc.gridx = 1;
        main.add(operationBox, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        main.add(new JLabel("Opérande B :"), gbc);
        gbc.gridx = 1;
        main.add(fieldB, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        main.add(new JLabel("Résultat :"), gbc);
        gbc.gridx = 1;
        resultLabel.setFont(ViewConstants.RESULT_FONT);
        main.add(resultLabel, gbc);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, ViewConstants.BTN_GAP, 0));
        btnPanel.add(calculateBtn);
        btnPanel.add(resetBtn);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        main.add(btnPanel, gbc);

        gbc.gridy = 5; gbc.gridwidth = 2;
        statusLabel.setForeground(ViewConstants.STATUS_COLOR);
        main.add(statusLabel, gbc);

        add(main);
    }
}