package cdmtpsu.coffee.ui.ingredient;

import cdmtpsu.coffee.data.Ingredient;
import cdmtpsu.coffee.util.CenterLayout;
import cdmtpsu.coffee.util.SwingUtils;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;

public final class AddIngredientDialog extends JDialog {
    private Ingredient result;

    private final JLabel nameLabel;
    private final JTextField nameTextField;
    private final JLabel unitLabel;
    private final JTextField unitTextField;
    private final JLabel amountLabel;
    private final JSpinner amountSpinner;
    private final JLabel warnAmountLabel;
    private final JSpinner warnAmountSpinner;
    private final JButton okButton;
    private final JButton cancelButton;
    private final JPanel buttonPanel;
    private final JPanel contentPane;

    public AddIngredientDialog(Window owner) {
        super(owner);

        /* UI */
        nameLabel = new JLabel();
        nameTextField = new JTextField();
        unitLabel = new JLabel();
        unitTextField = new JTextField();
        amountLabel = new JLabel();
        amountSpinner = new JSpinner();
        warnAmountLabel = new JLabel();
        warnAmountSpinner = new JSpinner();
        okButton = new JButton();
        cancelButton = new JButton();
        buttonPanel = new JPanel();
        contentPane = new JPanel();

        /* nameLabel */
        nameLabel.setText("Название");

        /* nameTextField */
        nameTextField.setPreferredSize(new Dimension(200, 24));
        SwingUtils.onValueChanged(nameTextField, this::fieldValueChanged);

        /* unitLabel */
        unitLabel.setText("Единицы измерения");

        /* unitTextField */
        unitTextField.setPreferredSize(new Dimension(200, 24));
        SwingUtils.onValueChanged(unitTextField, this::fieldValueChanged);

        /* amountLabel */
        amountLabel.setText("Количество");

        /* amountSpinner */
        amountSpinner.setPreferredSize(new Dimension(200, 24));
        amountSpinner.setModel(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));

        /* warnAmountLabel */
        warnAmountLabel.setText("Уведомлять если меньше");

        /* warnAmountSpinner */
        warnAmountSpinner.setPreferredSize(new Dimension(200, 24));
        warnAmountSpinner.setModel(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));

        /* okButton */
        okButton.setPreferredSize(new Dimension(70, 24));
        okButton.setText("ОК");
        okButton.addActionListener(this::okButtonClicked);

        /* cancelButton */
        cancelButton.setPreferredSize(new Dimension(70, 24));
        cancelButton.setText("Отмена");
        cancelButton.addActionListener(this::cancelButtonClicked);

        /* buttonPanel */
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        /* contentPane */
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(new CenterLayout());
        contentPane.add(nameLabel);
        contentPane.add(nameTextField);
        contentPane.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPane.add(unitLabel);
        contentPane.add(unitTextField);
        contentPane.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPane.add(amountLabel);
        contentPane.add(amountSpinner);
        contentPane.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPane.add(warnAmountLabel);
        contentPane.add(warnAmountSpinner);
        contentPane.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPane.add(buttonPanel);

        /* this */
        setTitle("Добавить ингредиент");
        setContentPane(contentPane);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setModal(true);
        pack();
        setLocationRelativeTo(owner);

        fieldValueChanged();
    }

    private void fieldValueChanged() {
        String name = nameTextField.getText();
        String unit = unitTextField.getText();

        okButton.setEnabled(name.length() > 0 && unit.length() > 0);
    }

    private void okButtonClicked(ActionEvent event) {
        String name = nameTextField.getText();
        String unit = unitTextField.getText();
        int amount = (int) amountSpinner.getValue();
        int warnAmount = (int) warnAmountSpinner.getValue();

        result = new Ingredient();
        result.setName(name);
        result.setUnit(unit);
        result.setAmount(amount);
        result.setWarnAmount(warnAmount);

        dispose();
    }

    private void cancelButtonClicked(ActionEvent event) {
        dispose();
    }

    public Ingredient getResult() {
        return result;
    }
}