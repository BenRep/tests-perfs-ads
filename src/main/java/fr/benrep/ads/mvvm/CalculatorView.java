package fr.benrep.ads.mvvm;

import fr.benrep.ads.common.ViewConstants;

import javax.swing.*;
import java.awt.*;

/**
 * MVVM - VIEW
 *
 * Responsabilités :
 * - Construire les composants Swing
 * - S'abonner aux ObservableProperty du ViewModel (data binding)
 * - Appeler les commandes du ViewModel en réponse aux actions utilisateur
 * - Ne contient AUCUNE logique : ni métier, ni présentation
 *
 * Différence clé avec MVP View :
 * - La View ne connaît pas d'interface "Presenter" : elle appelle directement
 *   des méthodes du ViewModel (commandCalculate, commandReset)
 * - La mise à jour de l'UI se fait via les bindings (ObservableProperty.bind())
 *   et non via des appels manuels du Presenter.
 * - La View est "déclarative" : elle déclare ses bindings une fois dans bindViewModel().
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
     * Établit les bindings bidirectionnels avec le ViewModel.
     * - ViewModel → View : bind() sur chaque ObservableProperty
     * - View → ViewModel : listeners sur les boutons
     *
     * @param vm le ViewModel à connecter
     */
    public void bindViewModel(CalculatorViewModel vm) {
        // Bindings ViewModel → View (one-way)
        vm.displayA.bind(v      -> fieldA.setText(v));
        vm.displayB.bind(v      -> fieldB.setText(v));
        vm.displayOp.bind(v     -> operationBox.setSelectedItem(v));
        vm.displayResult.bind(v -> resultLabel.setText(v));
        vm.statusMessage.bind(v -> statusLabel.setText(v));

        // Commandes View → ViewModel
        calculateBtn.addActionListener(e ->
                vm.commandCalculate(fieldA.getText().trim(),
                        fieldB.getText().trim(),
                        (String) operationBox.getSelectedItem()));
        resetBtn.addActionListener(e -> vm.commandReset());
    }

    private void buildUI() {
        setTitle(ViewConstants.TITLE + " [MVVM]");
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
}