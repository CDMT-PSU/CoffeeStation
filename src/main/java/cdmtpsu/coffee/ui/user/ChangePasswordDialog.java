package cdmtpsu.coffee.ui.user;

import cdmtpsu.coffee.data.Database;
import cdmtpsu.coffee.data.User;
import cdmtpsu.coffee.util.CenterLayout;
import cdmtpsu.coffee.util.SwingUtils;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;

public final class ChangePasswordDialog extends JDialog {
    private final User initial;
    private User result;

    private final JLabel passwordLabel;
    private final JPasswordField passwordField;
    private final JButton okButton;
    private final JButton cancelButton;
    private final JPanel buttonPanel;
    private final JPanel contentPane;

    public ChangePasswordDialog(Window owner, User initial) {
        super(owner);

        this.initial = initial;

        /* UI */
        passwordLabel = new JLabel();
        passwordField = new JPasswordField();
        okButton = new JButton();
        cancelButton = new JButton();
        buttonPanel = new JPanel();
        contentPane = new JPanel();

        /* passwordLabel */
        passwordLabel.setText("Пароль");

        /* passwordField */
        passwordField.setPreferredSize(new Dimension(200, 24));
        SwingUtils.onValueChanged(passwordField, this::fieldValueChanged);

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
        contentPane.add(passwordLabel);
        contentPane.add(passwordField);
        contentPane.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPane.add(buttonPanel);

        /* this */
        setTitle("Изменить пароль");
        setContentPane(contentPane);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setModal(true);
        pack();
        setLocationRelativeTo(owner);

        fieldValueChanged();
    }

    private void fieldValueChanged() {
        String password = passwordField.getText();

        okButton.setEnabled(password.matches(User.PASSWORD_PATTERN));
    }

    private void okButtonClicked(ActionEvent event) {
        String hash = Database.hashPassword(passwordField.getText());

        result = initial;
        result.setHash(hash);

        dispose();
    }

    private void cancelButtonClicked(ActionEvent event) {
        dispose();
    }

    public User getResult() {
        return result;
    }
}