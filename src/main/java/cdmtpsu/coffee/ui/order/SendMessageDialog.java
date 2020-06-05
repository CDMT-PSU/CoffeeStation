package cdmtpsu.coffee.ui.order;

import cdmtpsu.coffee.util.CenterLayout;
import cdmtpsu.coffee.util.SwingUtils;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;

public final class SendMessageDialog extends JDialog {
    private final JLabel addressLabel;
    private final JTextField addressTextField;
    private final JButton okButton;
    private final JButton cancelButton;
    private final JPanel buttonPanel;
    private final JPanel contentPane;

    public SendMessageDialog(Window owner) {
        super(owner);

        /* UI */
        addressLabel = new JLabel();
        addressTextField = new JTextField();
        okButton = new JButton();
        cancelButton = new JButton();
        buttonPanel = new JPanel();
        contentPane = new JPanel();

        /* addressLabel */
        addressLabel.setText("Адрес почтового ящика");

        /* addressTextField */
        addressTextField.setPreferredSize(new Dimension(200, 24));
        SwingUtils.onValueChanged(addressTextField, this::fieldValueChanged);

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
        contentPane.add(addressLabel);
        contentPane.add(addressTextField);
        contentPane.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPane.add(buttonPanel);

        /* this */
        setTitle("Сообщить администратору");
        setContentPane(contentPane);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setModal(true);
        pack();
        setLocationRelativeTo(owner);

        fieldValueChanged();
    }

    private void fieldValueChanged() {
        String address = addressTextField.getText();

        okButton.setEnabled(address.length() > 0);
    }

    private void okButtonClicked(ActionEvent event) {
        /* Ничего не делаем :3 */
        dispose();
    }

    private void cancelButtonClicked(ActionEvent event) {
        dispose();
    }
}