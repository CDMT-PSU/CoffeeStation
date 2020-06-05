package cdmtpsu.coffee.ui.welcome;

import cdmtpsu.coffee.data.Database;
import cdmtpsu.coffee.data.User;
import cdmtpsu.coffee.ui.main.MainFrame;
import cdmtpsu.coffee.util.CenterLayout;
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

public final class LogInPanel extends JPanel {
    private final Window owner;

    private final JLabel usernameLabel;
    private final JTextField usernameTextField;
    private final JLabel passwordLabel;
    private final JPasswordField passwordField;
    private final JButton logInButton;

    public LogInPanel(Window owner) {
        this.owner = owner;

        usernameLabel = new JLabel();
        usernameTextField = new JTextField();
        passwordLabel = new JLabel();
        passwordField = new JPasswordField();
        logInButton = new JButton();

        /* usernameLabel */
        usernameLabel.setText("Имя пользователя");

        /* usernameTextField */
        usernameTextField.setPreferredSize(new Dimension(200, 24));

        /* passwordLabel */
        passwordLabel.setText("Пароль");

        /* passwordField */
        passwordField.setPreferredSize(new Dimension(200, 24));

        /* logInButton */
        logInButton.setPreferredSize(new Dimension(70, 24));
        logInButton.setText("Войти");
        logInButton.addActionListener(this::logInButtonClicked);

        /* this */
        setLayout(new CenterLayout());
        add(usernameLabel);
        add(usernameTextField);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(passwordLabel);
        add(passwordField);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(logInButton);
    }

    private void logInButtonClicked(ActionEvent event) {
        String username = usernameTextField.getText();
        String password = passwordField.getText();

        User user = null;
        try {
            user = Database.getInstance().getUsers().queryBuilder()
                    .where().eq(User.USERNAME_FIELD_NAME, username)
                    .queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (user != null && Database.authenticate(user, password)) {
            owner.dispose();
            new MainFrame(user).setVisible(true);
        } else {
            showWrongCredentialsMessageDialog();
        }
    }

    private void showWrongCredentialsMessageDialog() {
        JOptionPane.showMessageDialog(this,
                "Учетная запись с указанными данными не существует.", "Ошибка",
                JOptionPane.ERROR_MESSAGE);
    }
}