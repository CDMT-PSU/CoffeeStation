package cdmtpsu.coffee.ui.user;

import cdmtpsu.coffee.data.Database;
import cdmtpsu.coffee.data.User;
import cdmtpsu.coffee.util.CenterLayout;
import cdmtpsu.coffee.util.SwingUtils;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.util.EnumSet;

public final class AddUserDialog extends JDialog {
    private User result;

    private final JLabel usernameLabel;
    private final JTextField usernameTextField;
    private final JLabel passwordLabel;
    private final JPasswordField passwordField;
    private final JLabel nameLabel;
    private final JTextField nameTextField;
    private final JLabel roleLabel;
    private final JComboBox<User.Role> roleComboBox;
    private final JButton okButton;
    private final JButton cancelButton;
    private final JPanel buttonPanel;
    private final JPanel contentPane;

    public AddUserDialog(Window owner) {
        super(owner);

        /* UI */
        usernameLabel = new JLabel();
        usernameTextField = new JTextField();
        passwordLabel = new JLabel();
        passwordField = new JPasswordField();
        nameLabel = new JLabel();
        nameTextField = new JTextField();
        roleLabel = new JLabel();
        roleComboBox = new JComboBox<>();
        okButton = new JButton();
        cancelButton = new JButton();
        buttonPanel = new JPanel();
        contentPane = new JPanel();

        /* usernameLabel */
        usernameLabel.setText("Имя пользователя");

        /* usernameTextField */
        usernameTextField.setPreferredSize(new Dimension(200, 24));
        SwingUtils.onValueChanged(usernameTextField, this::fieldValueChanged);

        /* passwordLabel */
        passwordLabel.setText("Пароль");

        /* passwordField */
        passwordField.setPreferredSize(new Dimension(200, 24));
        SwingUtils.onValueChanged(passwordField, this::fieldValueChanged);

        /* nameLabel */
        nameLabel.setText("ФИО");

        /* nameTextField */
        nameTextField.setPreferredSize(new Dimension(200, 24));
        SwingUtils.onValueChanged(nameTextField, this::fieldValueChanged);

        /* roleLabel */
        roleLabel.setText("Роль");

        /* roleComboBox */
        roleComboBox.setPreferredSize(new Dimension(200, 24));
        EnumSet.allOf(User.Role.class).forEach(roleComboBox::addItem);

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
        contentPane.add(usernameLabel);
        contentPane.add(usernameTextField);
        contentPane.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPane.add(passwordLabel);
        contentPane.add(passwordField);
        contentPane.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPane.add(nameLabel);
        contentPane.add(nameTextField);
        contentPane.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPane.add(roleLabel);
        contentPane.add(roleComboBox);
        contentPane.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPane.add(buttonPanel);

        /* this */
        setTitle("Добавить пользователя");
        setContentPane(contentPane);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setModal(true);
        pack();
        setLocationRelativeTo(owner);

        fieldValueChanged();
    }

    private void fieldValueChanged() {
        String username = usernameTextField.getText();
        String password = passwordField.getText();
        String name = nameTextField.getText();

        okButton.setEnabled(username.matches(User.USERNAME_PATTERN) &&
                password.matches(User.PASSWORD_PATTERN) &&
                name.matches(User.NAME_PATTERN));
    }

    private void okButtonClicked(ActionEvent event) {
        String username = usernameTextField.getText();
        String hash = Database.hashPassword(passwordField.getText());
        String name = nameTextField.getText();
        User.Role role = (User.Role) roleComboBox.getSelectedItem();

        if (Database.getInstance().usernameExists(username)) {
            showUsernameAlreadyExistsMessageDialog();
            return;
        }

        result = new User();
        result.setUsername(username);
        result.setHash(hash);
        result.setName(name);
        result.setRole(role);

        dispose();
    }

    private void cancelButtonClicked(ActionEvent event) {
        dispose();
    }

    public User getResult() {
        return result;
    }

    private void showUsernameAlreadyExistsMessageDialog() {
        JOptionPane.showMessageDialog(getOwner(),
                "Указанное имя пользователя уже занято.", "Внимание",
                JOptionPane.WARNING_MESSAGE);
    }
}