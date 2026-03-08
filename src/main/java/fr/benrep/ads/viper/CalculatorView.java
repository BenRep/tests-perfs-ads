package fr.benrep.ads.viper;

import fr.benrep.ads.common.ViewConstants;

import javax.swing.*;
import java.awt.*;

/**
 * VIPER - VIEW (implémentation Swing)
 *
 * Responsabilités :
 * - Implémenter CalculatorViewContract
 * - Construire les composants Swing
 * - Déléguer les actions au Presenter
 * - Ne contient AUCUNE logique (encore moins que dans MVP)
 *
 * Différences avec MVP View :
 * - La View VIPER n'interagit avec aucun Model ni ViewModel
 * - Elle délègue systématiquement au Presenter même les actions les plus simples
 * - C'est le Presenter qui formate toutes les chaînes affichées
 */
public class CalculatorView extends JFrame implements CalculatorViewContract {

    private final JTextField fieldA    = new JTextField(ViewConstants.FIELD_COLUMNS);
    private final JTextField fieldB    = new JTextField(ViewConstants.FIELD_COLUMNS);
    private final JComboBox<String> operationBox =
            new JComboBox<>(ViewConstants.OPERATIONS);
    private final JLabel resultLabel   = new JLabel(ViewConstants.RESULT_DEFAULT);
    private final JLabel statusLabel   = new JLabel(ViewConstants.STATUS_DEFAULT);
    private final JButton calculateBtn = new JButton(ViewConstants.BTN_CALCULATE);
    private final JButton resetBtn     = new JButton(ViewConstants.BTN_RESET);

    private CalculatorPresenter presenter;

    public CalculatorView() {
        buildUI();
    }

    /** Injection du Presenter par le Router. */
    public void setPresenter(CalculatorPresenter presenter) {
        this.presenter = presenter;
        calculateBtn.addActionListener(e -> presenter.onCalculateRequested());
        resetBtn.addActionListener(e -> presenter.onResetRequested());
    }

    private void buildUI() {
        setTitle(ViewConstants.TITLE + " [VIPER]");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

    // -------------------------------------------------------------------------
    // Implémentation du contrat CalculatorViewContract
    // -------------------------------------------------------------------------

    @Override public String getFieldA()             { return fieldA.getText().trim(); }
    @Override public String getFieldB()             { return fieldB.getText().trim(); }
    @Override public String getSelectedOperation()  { return (String) operationBox.getSelectedItem(); }

    @Override public void displayResult(String v)   { resultLabel.setText(v); }
    @Override public void displayStatus(String m)   { statusLabel.setText(m); }
    @Override public void displayFieldA(String v)   { fieldA.setText(v); }
    @Override public void displayFieldB(String v)   { fieldB.setText(v); }
    @Override public void displayOperation(String op) { operationBox.setSelectedItem(op); }
}