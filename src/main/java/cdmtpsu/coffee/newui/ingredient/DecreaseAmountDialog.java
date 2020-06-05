package cdmtpsu.coffee.newui.ingredient;

import cdmtpsu.coffee.data.Ingredient;
import cdmtpsu.coffee.util.CenterLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;

public final class DecreaseAmountDialog extends JDialog {
    private final Ingredient initial;
    private Ingredient result;

    private final JLabel amountLabel;
    private final JSpinner amountSpinner;
    private final JButton okButton;
    private final JButton cancelButton;
    private final JPanel buttonPanel;
    private final JPanel contentPane;

    public DecreaseAmountDialog(Window owner, Ingredient initial) {
        super(owner);

        this.initial = initial;

        /* UI */
        amountLabel = new JLabel();
        amountSpinner = new JSpinner();
        okButton = new JButton();
        cancelButton = new JButton();
        buttonPanel = new JPanel();
        contentPane = new JPanel();

        /* amountLabel */
        amountLabel.setText("Уменшить количество на");

        /* amountSpinner */
        amountSpinner.setPreferredSize(new Dimension(200, 24));
        amountSpinner.setModel(new SpinnerNumberModel(0, 0, initial.getAmount(), 1));

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
        contentPane.add(amountLabel);
        contentPane.add(amountSpinner);
        contentPane.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPane.add(buttonPanel);

        /* this */
        setTitle("Уменьшить количество");
        setContentPane(contentPane);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setModal(true);
        pack();
        setLocationRelativeTo(owner);
    }

    private void okButtonClicked(ActionEvent event) {
        int amount = (int) amountSpinner.getValue();

        result = initial;
        result.setAmount(initial.getAmount() - amount);

        dispose();
    }

    private void cancelButtonClicked(ActionEvent event) {
        dispose();
    }

    public Ingredient getResult() {
        return result;
    }
}