package cdmtpsu.coffee.ui.tabs;

import cdmtpsu.coffee.data.Database;
import cdmtpsu.coffee.data.User;
import cdmtpsu.coffee.util.CenterLayout;
import cdmtpsu.coffee.util.EnumCellEditor;
import cdmtpsu.coffee.util.SwingUtils;
import cdmtpsu.coffee.util.TextFieldCellEditor;

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

public final class UserTab extends Tab<User> {
    public UserTab() {
        super(
                new String[]{
                        "Имя пользователя",
                        "Хеш",
                        "Роль"
                },
                new Class[]{
                        String.class,
                        String.class,
                        User.Role.class
                },
                Database.getInstance().getUsers(),
                true, true, true);
        setCellEditor("Имя пользователя", new TextFieldCellEditor(Database.USERNAME_PATTERN));
        setCellEditor("Хеш", new TextFieldCellEditor(Database.PASSWORD_PATTERN, false));
        setCellEditor("Роль", new EnumCellEditor<>(User.Role.class));
    }

    @Override
    public User createItem(Window owner) {
        AddDialog dialog = new AddDialog();
        dialog.setVisible(true);
        return dialog.getResult();
    }

    private static final class AddDialog extends JDialog {
        /* ui components */
        private final JLabel usernameLabel;
        private final JTextField usernameTextField;
        private final JLabel passwordLabel;
        private final JPasswordField passwordField;
        private final JLabel roleLabel;
        private final JComboBox<User.Role> roleComboBox;
        private final JButton okButton;
        private final JButton cancelButton;
        private final JPanel buttonPanel;
        private final JPanel contentPane;

        private User result;

        AddDialog() {
            /* init components */
            usernameLabel = new JLabel();
            usernameTextField = new JTextField();
            passwordLabel = new JLabel();
            passwordField = new JPasswordField();
            roleLabel = new JLabel();
            roleComboBox = new JComboBox<>();
            okButton = new JButton();
            cancelButton = new JButton();
            buttonPanel = new JPanel();
            contentPane = new JPanel();

            /* usernameLabel */
            usernameLabel.setText("Имя пользователя");

            /* usernameTextField */
            usernameTextField.setPreferredSize(new Dimension(150, 20));
            SwingUtils.onValueChanged(usernameTextField, this::fieldValueChanged);

            /* passwordLabel */
            passwordLabel.setText("Пароль");

            /* passwordField */
            passwordField.setPreferredSize(new Dimension(150, 20));
            SwingUtils.onValueChanged(passwordField, this::fieldValueChanged);

            /* roleLabel */
            roleLabel.setText("Роль");

            /* roleComboBox */
            roleComboBox.setPreferredSize(new Dimension(150, 20));
            EnumSet.allOf(User.Role.class).forEach(roleComboBox::addItem);

            /* okButton */
            okButton.setText("ОК");
            okButton.addActionListener(this::okButtonClicked);

            /* cancelButton */
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
            contentPane.add(roleLabel);
            contentPane.add(roleComboBox);
            contentPane.add(Box.createRigidArea(new Dimension(0, 10)));
            contentPane.add(buttonPanel);

            /* this */
            setTitle("Добавить пользователя");
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            setContentPane(contentPane);
            setModal(true);
            pack();
            setLocationRelativeTo(null);

            fieldValueChanged();
        }

        private void fieldValueChanged() {
            String username = usernameTextField.getText();
            String password = passwordField.getText();
            okButton.setEnabled(Database.isValidUsername(username) && Database.isValidPassword(password));
        }

        private void okButtonClicked(ActionEvent event) {
            String username = usernameTextField.getText();
            String hash = Database.hashPassword(passwordField.getText());
            User.Role role = (User.Role) roleComboBox.getSelectedItem();

            if (Database.getInstance().usernameExist(username)) {
                JOptionPane.showMessageDialog(this,
                        "Пользователь с указанным именем уже существует",
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            result = new User();
            result.setUsername(username);
            result.setHash(hash);
            result.setRole(role);

            dispose();
        }

        private void cancelButtonClicked(ActionEvent event) {
            dispose();
        }

        User getResult() {
            return result;
        }
    }
}
