package cdmtpsu.coffee.newui.user;

import cdmtpsu.coffee.data.User;
import cdmtpsu.coffee.util.CenterLayout;
import cdmtpsu.coffee.util.SwingUtils;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.util.EnumSet;

public final class EditUserDialog extends JDialog {
    private final User initial;
    private User result;

    private final JLabel nameLabel;
    private final JTextField nameTextField;
    private final JLabel roleLabel;
    private final JComboBox<User.Role> roleComboBox;
    private final JButton okButton;
    private final JButton cancelButton;
    private final JPanel buttonPanel;
    private final JPanel contentPane;

    public EditUserDialog(Window owner, User initial) {
        super(owner);

        this.initial = initial;

        /* UI */
        nameLabel = new JLabel();
        nameTextField = new JTextField();
        roleLabel = new JLabel();
        roleComboBox = new JComboBox<>();
        okButton = new JButton();
        cancelButton = new JButton();
        buttonPanel = new JPanel();
        contentPane = new JPanel();

        /* nameLabel */
        nameLabel.setText("ФИО");

        /* nameTextField */
        nameTextField.setPreferredSize(new Dimension(200, 24));
        SwingUtils.onValueChanged(nameTextField, this::fieldValueChanged);
        nameTextField.setText(initial.getName());

        /* roleLabel */
        roleLabel.setText("Роль");

        /* roleComboBox */
        roleComboBox.setPreferredSize(new Dimension(200, 24));
        EnumSet.allOf(User.Role.class).forEach(roleComboBox::addItem);
        roleComboBox.setSelectedItem(initial.getRole());

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
        contentPane.add(roleLabel);
        contentPane.add(roleComboBox);
        contentPane.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPane.add(buttonPanel);

        /* this */
        setTitle("Редактировать пользователя");
        setContentPane(contentPane);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setModal(true);
        pack();
        setLocationRelativeTo(owner);

        fieldValueChanged();
    }

    private void fieldValueChanged() {
        String name = nameTextField.getText();

        okButton.setEnabled(name.matches(User.NAME_PATTERN));
    }

    private void okButtonClicked(ActionEvent event) {
        String name = nameTextField.getText();
        User.Role role = (User.Role) roleComboBox.getSelectedItem();

        result = initial;
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
}