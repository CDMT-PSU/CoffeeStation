package cdmtpsu.coffee.ui;

import cdmtpsu.coffee.util.CenterLayout;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

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
        private final JTextField passwordTextField;
        private final JButton logInButton;

        LogInPanel() {
            /* init components */
            usernameLabel = new JLabel();
            usernameTextField = new JTextField();
            passwordLabel = new JLabel();
            passwordTextField = new JTextField();
            logInButton = new JButton();

            /* usernameLabel */
            usernameLabel.setText("Имя пользователя");

            /* usernameTextField */
            usernameTextField.setPreferredSize(new Dimension(150, 20));

            /* passwordLabel */
            passwordLabel.setText("Пароль");

            /* passwordTextField */
            passwordTextField.setPreferredSize(new Dimension(150, 20));

            /* logInButton */
            logInButton.setText("Войти");
            logInButton.addActionListener(this::logInButtonClicked);

            /* this */
            setLayout(new CenterLayout());
            add(usernameLabel);
            add(usernameTextField);
            add(Box.createRigidArea(new Dimension(0, 10)));
            add(passwordLabel);
            add(passwordTextField);
            add(Box.createRigidArea(new Dimension(0, 10)));
            add(logInButton);
        }

        private void logInButtonClicked(ActionEvent event) {
            /* TODO: log in */
        }
    }

    private static final class SignUpPanel extends JPanel {
        /* ui components */
        private final JLabel usernameLabel;
        private final JTextField usernameTextField;
        private final JLabel passwordLabel;
        private final JTextField passwordTextField;
        private final JLabel repeatPasswordLabel;
        private final JTextField repeatPasswordTextField;
        private final JButton signUpButton;

        SignUpPanel() {
            /* init components */
            usernameLabel = new JLabel();
            usernameTextField = new JTextField();
            passwordLabel = new JLabel();
            passwordTextField = new JTextField();
            repeatPasswordLabel = new JLabel();
            repeatPasswordTextField = new JTextField();
            signUpButton = new JButton();

            /* usernameLabel */
            usernameLabel.setText("Имя пользователя (?)");
            usernameLabel.setToolTipText("Имя пользователя должно содержать от 4 до 15 символов.");

            /* usernameTextField */
            usernameTextField.setPreferredSize(new Dimension(150, 20));

            /* passwordLabel */
            passwordLabel.setText("Пароль (?)");
            passwordLabel.setToolTipText("Пароль должен содержать от 4 до 15 символов.");

            /* passwordTextField */
            passwordTextField.setPreferredSize(new Dimension(150, 20));

            /* repeatPasswordLabel */
            repeatPasswordLabel.setText("Повторите пароль");

            /* repeatPasswordTextField */
            repeatPasswordTextField.setPreferredSize(new Dimension(150, 20));

            /* signUpButton */
            signUpButton.setText("Создать учетную запись");
            signUpButton.addActionListener(this::signUpButtonClicked);

            /* this */
            setLayout(new CenterLayout());
            add(usernameLabel);
            add(usernameTextField);
            add(Box.createRigidArea(new Dimension(0, 10)));
            add(passwordLabel);
            add(passwordTextField);
            add(Box.createRigidArea(new Dimension(0, 10)));
            add(repeatPasswordLabel);
            add(repeatPasswordTextField);
            add(Box.createRigidArea(new Dimension(0, 10)));
            add(signUpButton);
        }

        private void signUpButtonClicked(ActionEvent event) {
            /* TODO: sign up */
        }
    }
}
