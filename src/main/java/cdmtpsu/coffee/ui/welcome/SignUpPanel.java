package cdmtpsu.coffee.ui.welcome;

import cdmtpsu.coffee.data.Database;
import cdmtpsu.coffee.data.User;
import cdmtpsu.coffee.util.CenterLayout;
import cdmtpsu.coffee.util.SwingUtils;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.sql.SQLException;

public final class SignUpPanel extends JPanel {
    private final Window owner;

    private final JLabel usernameLabel;
    private final JTextField usernameTextField;
    private final JLabel passwordLabel;
    private final JPasswordField passwordField;
    private final JLabel repeatPasswordLabel;
    private final JPasswordField repeatPasswordField;
    private final JLabel nameLabel;
    private final JTextField nameTextField;
    private final JButton signUpButton;

    public SignUpPanel(Window owner) {
        this.owner = owner;

        usernameLabel = new JLabel();
        usernameTextField = new JTextField();
        passwordLabel = new JLabel();
        passwordField = new JPasswordField();
        repeatPasswordLabel = new JLabel();
        repeatPasswordField = new JPasswordField();
        nameLabel = new JLabel();
        nameTextField = new JTextField();
        signUpButton = new JButton();

        /* usernameLabel */
        usernameLabel.setText("Имя пользователя (?)");
        usernameLabel.setToolTipText("Имя пользователя должно содержать от 4 до 15 символов, а также состоять " +
                "лишь из символов латинского алфавита, цифр или символов нижнего подчеркивания.");

        /* usernameTextField */
        usernameTextField.setPreferredSize(new Dimension(200, 24));
        SwingUtils.onValueChanged(usernameTextField, this::fieldValueChanged);

        /* passwordLabel */
        passwordLabel.setText("Пароль (?)");
        passwordLabel.setToolTipText("Пароль должен содержать от 4 до 15 символов.");

        /* passwordTextField */
        passwordField.setPreferredSize(new Dimension(200, 24));
        SwingUtils.onValueChanged(passwordField, this::fieldValueChanged);

        /* repeatPasswordLabel */
        repeatPasswordLabel.setText("Повторите пароль");

        /* repeatPasswordTextField */
        repeatPasswordField.setPreferredSize(new Dimension(200, 24));
        SwingUtils.onValueChanged(repeatPasswordField, this::fieldValueChanged);

        /* nameLabel */
        nameLabel.setText("ФИО");

        /* nameTextField */
        nameTextField.setPreferredSize(new Dimension(200, 24));
        SwingUtils.onValueChanged(nameTextField, this::fieldValueChanged);

        /* signUpButton */
        signUpButton.setText("Создать учетную запись");
        signUpButton.addActionListener(this::signUpButtonClicked);

        /* this */
        setLayout(new CenterLayout());
        add(usernameLabel);
        add(usernameTextField);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(nameLabel);
        add(nameTextField);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(passwordLabel);
        add(passwordField);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(repeatPasswordLabel);
        add(repeatPasswordField);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(signUpButton);

        fieldValueChanged();
    }

    private void fieldValueChanged() {
        String username = usernameTextField.getText();
        String password = passwordField.getText();
        String passwordConfirmation = repeatPasswordField.getText();
        String name = nameTextField.getText();

        signUpButton.setEnabled(username.matches(User.USERNAME_PATTERN) &&
                password.matches(User.PASSWORD_PATTERN) &&
                name.matches(User.NAME_PATTERN) &&
                password.equals(passwordConfirmation));
    }

    private void signUpButtonClicked(ActionEvent event) {
        String username = usernameTextField.getText();
        String name = nameTextField.getText();
        String hash = Database.hashPassword(passwordField.getText());
        User.Role role = User.Role.USER;

        User user = new User();
        user.setUsername(username);
        user.setName(name);
        user.setHash(hash);
        user.setRole(role);

        if (!Database.getInstance().usernameExists(username)) {
            try {
                Database.getInstance().getUsers().create(user);
                showSuccessMessageDialog();
                /* Очищаем все поля. */
                usernameTextField.setText(null);
                nameTextField.setText(null);
                passwordField.setText(null);
                repeatPasswordField.setText(null);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            showUsernameAlreadyExistsMessageDialog();
        }
    }

    private void showSuccessMessageDialog() {
        JOptionPane.showMessageDialog(this,
                "Учетная запись успешно создана.", "Сообщение",
                JOptionPane.PLAIN_MESSAGE);
    }

    private void showUsernameAlreadyExistsMessageDialog() {
        JOptionPane.showMessageDialog(this,
                "Учетная запись с указанным именем уже существует.", "Ошибка",
                JOptionPane.ERROR_MESSAGE);
    }
}