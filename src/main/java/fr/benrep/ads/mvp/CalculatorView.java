package fr.benrep.ads.mvp;

import fr.benrep.ads.common.ViewConstants;

import javax.swing.*;
import java.awt.*;

/**
 * MVP - VIEW (implémentation Swing)
 *
 * Responsabilités :
 * - Implémenter CalculatorViewContract
 * - Construire les composants Swing
 * - Déléguer les actions au Presenter via des listeners
 * - Ne contient AUCUNE logique : ni métier, ni présentation
 *
 * Différence clé avec MVC :
 * La View ne connaît PAS le Model. Elle expose uniquement des méthodes
 * simples (getFieldA(), setResultLabel()…). Le Presenter est le seul
 * à faire le lien entre Model et View.
 * La View ne s'observe jamais elle-même.
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

    /** Injection du Presenter après construction (évite la dépendance circulaire). */
    public void setPresenter(CalculatorPresenter presenter) {
        this.presenter = presenter;
        // Binding des actions vers le Presenter
        calculateBtn.addActionListener(e -> presenter.onCalculate());
        resetBtn.addActionListener(e -> presenter.onReset());
    }

    private void buildUI() {
        setTitle(ViewConstants.TITLE + " [MVP]");
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

    // -------------------------------------------------------------------------
    // Implémentation du contrat CalculatorViewContract
    // -------------------------------------------------------------------------

    @Override public String getFieldA()             { return fieldA.getText().trim(); }
    @Override public String getFieldB()             { return fieldB.getText().trim(); }
    @Override public String getSelectedOperation()  { return (String) operationBox.getSelectedItem(); }

    @Override public void setResultLabel(String v)  { resultLabel.setText(v); }
    @Override public void setStatusLabel(String m)  { statusLabel.setText(m); }
    @Override public void setFieldA(String v)       { fieldA.setText(v); }
    @Override public void setFieldB(String v)       { fieldB.setText(v); }
    @Override public void setOperation(String op)   { operationBox.setSelectedItem(op); }
}