package cdmtpsu.coffee.ui;

import cdmtpsu.coffee.data.Database;
import cdmtpsu.coffee.data.User;
import cdmtpsu.coffee.util.CenterLayout;
import cdmtpsu.coffee.util.SwingUtils;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.sql.SQLException;

public final class StartupWindow {
    /* ui components */
    private final JTabbedPane tabbedPane;
    private final JPanel contentPane;
    private final JFrame frame;

    public StartupWindow() {
        /* init components */
        tabbedPane = new JTabbedPane();
        contentPane = new JPanel();
        frame = new JFrame();

        /* tabbedPane */
        tabbedPane.addTab("Войти", new LogInPanel());
        tabbedPane.addTab("Создать учетную запись", new SignUpPanel());

        /* contentPane */
        contentPane.setPreferredSize(new Dimension(320, 240));
        contentPane.setLayout(new BorderLayout());
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        /* frame */
        frame.setTitle("CoffeeStation");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setContentPane(contentPane);
        frame.pack();
        frame.setLocationRelativeTo(null);
    }

    public void create() {
        frame.setVisible(true);
    }

    private static final class LogInPanel extends JPanel {
        /* ui components */
        private final JLabel usernameLabel;
        private final JTextField usernameTextField;
        private final JLabel passwordLabel;
        private final JPasswordField passwordField;
        private final JButton logInButton;

        LogInPanel() {
            /* init components */
            usernameLabel = new JLabel();
            usernameTextField = new JTextField();
            passwordLabel = new JLabel();
            passwordField = new JPasswordField();
            logInButton = new JButton();

            /* usernameLabel */
            usernameLabel.setText("Имя пользователя");

            /* usernameTextField */
            usernameTextField.setPreferredSize(new Dimension(150, 20));

            /* passwordLabel */
            passwordLabel.setText("Пароль");

            /* passwordField */
            passwordField.setPreferredSize(new Dimension(150, 20));

            /* logInButton */
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
                /*JOptionPane.showMessageDialog(this,
                        e.getMessage(),
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);*/
            }
            if (user != null && Database.authenticate(user, password)) {
                System.out.println("SUCCESS!!");
            } else {
                JOptionPane.showMessageDialog(this,
                        "Учетная запись с указанными данными не существует",
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static final class SignUpPanel extends JPanel {
        /* ui components */
        private final JLabel usernameLabel;
        private final JTextField usernameTextField;
        private final JLabel passwordLabel;
        private final JPasswordField passwordField;
        private final JLabel repeatPasswordLabel;
        private final JPasswordField repeatPasswordField;
        private final JButton signUpButton;

        SignUpPanel() {
            /* init components */
            usernameLabel = new JLabel();
            usernameTextField = new JTextField();
            passwordLabel = new JLabel();
            passwordField = new JPasswordField();
            repeatPasswordLabel = new JLabel();
            repeatPasswordField = new JPasswordField();
            signUpButton = new JButton();

            /* usernameLabel */
            usernameLabel.setText("Имя пользователя (?)");
            usernameLabel.setToolTipText("Имя пользователя должно содержать от 4 до 15 символов, а также состоять" +
                    "лишь из символов латинского алфавита, цифр или символов нижнего подчеркивания.");

            /* usernameTextField */
            usernameTextField.setPreferredSize(new Dimension(150, 20));
            SwingUtils.onValueChanged(usernameTextField, this::fieldValueChanged);

            /* passwordLabel */
            passwordLabel.setText("Пароль (?)");
            passwordLabel.setToolTipText("Пароль должен содержать от 4 до 15 символов.");

            /* passwordTextField */
            passwordField.setPreferredSize(new Dimension(150, 20));
            SwingUtils.onValueChanged(passwordField, this::fieldValueChanged);

            /* repeatPasswordLabel */
            repeatPasswordLabel.setText("Повторите пароль");

            /* repeatPasswordTextField */
            repeatPasswordField.setPreferredSize(new Dimension(150, 20));
            SwingUtils.onValueChanged(repeatPasswordField, this::fieldValueChanged);

            /* signUpButton */
            signUpButton.setText("Создать учетную запись");
            signUpButton.addActionListener(this::signUpButtonClicked);

            /* this */
            setLayout(new CenterLayout());
            add(usernameLabel);
            add(usernameTextField);
            add(Box.createRigidArea(new Dimension(0, 10)));
            add(passwordLabel);
            add(passwordField);
            add(Box.createRigidArea(new Dimension(0, 10)));
            add(repeatPasswordLabel);
            add(repeatPasswordField);
            add(Box.createRigidArea(new Dimension(0, 10)));
            add(signUpButton);

            /* for the first time - manually */
            fieldValueChanged();
        }

        private void fieldValueChanged() {
            String username = usernameTextField.getText();
            String password = passwordField.getText();
            String passwordConfirmation = repeatPasswordField.getText();
            signUpButton.setEnabled(Database.isValidUsername(username) && Database.isValidPassword(password) &&
                    passwordConfirmation.equals(password));
        }

        private void signUpButtonClicked(ActionEvent event) {
            String username = usernameTextField.getText();
            String hash = Database.hashPassword(passwordField.getText());
            User.Role role = User.Role.USER;

            User user = new User();
            user.setUsername(username);
            user.setHash(hash);
            user.setRole(role);

            if (!Database.getInstance().usernameExist(username)) {
                try {
                    Database.getInstance().getUsers().create(user);
                    JOptionPane.showMessageDialog(this,
                            "Учетная запись успешно создана",
                            "Сообщение",
                            JOptionPane.PLAIN_MESSAGE);
                    /* clear all text fields */
                    usernameTextField.setText(null);
                    passwordField.setText(null);
                    repeatPasswordField.setText(null);
                } catch (SQLException e) {
                    e.printStackTrace();
                    /*JOptionPane.showMessageDialog(this,
                            e.getMessage(),
                            "Ошибка",
                            JOptionPane.ERROR_MESSAGE);*/
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "Учетная запись с указанным именем уже существует",
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
