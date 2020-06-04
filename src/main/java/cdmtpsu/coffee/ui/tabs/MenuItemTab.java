package cdmtpsu.coffee.ui.tabs;

import cdmtpsu.coffee.data.Database;
import cdmtpsu.coffee.data.MenuItem;
import cdmtpsu.coffee.data.User;
import cdmtpsu.coffee.util.CenterLayout;
import cdmtpsu.coffee.util.SpinnerCellEditor;
import cdmtpsu.coffee.util.TextFieldCellEditor;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;

public final class MenuItemTab extends Tab<MenuItem> {
    public MenuItemTab(User user) {
        super(
                new String[]{
                        "Название",
                        "Цена"
                },
                new Class[]{
                        String.class,
                        Integer.class
                },
                Database.getInstance().getMenuItems(),
                user.getRole() == User.Role.ADMINISTRATOR,
                user.getRole() == User.Role.ADMINISTRATOR,
                user.getRole() == User.Role.ADMINISTRATOR);
        setCellEditor("Название", new TextFieldCellEditor("[\\s\\S]+"));
        setCellEditor("Цена", new SpinnerCellEditor(0, 999999));
    }

    @Override
    public MenuItem createItem(Window owner) {
        AddDialog dialog = new AddDialog();
        dialog.setVisible(true);
        return dialog.getResult();
    }

    private static final class AddDialog extends JDialog {
        /* ui components */
        private final JLabel nameLabel;
        private final JTextField nameTextField;
        private final JLabel priceLabel;
        private final JSpinner priceSpinner;
        private final JButton okButton;
        private final JButton cancelButton;
        private final JPanel buttonPanel;
        private final JPanel contentPane;

        private MenuItem result;

        AddDialog() {
            /* init components */
            nameLabel = new JLabel();
            nameTextField = new JTextField();
            priceLabel = new JLabel();
            priceSpinner = new JSpinner();
            okButton = new JButton();
            cancelButton = new JButton();
            buttonPanel = new JPanel();
            contentPane = new JPanel();

            /* nameLabel */
            nameLabel.setText("Название");

            /* nameTextField */
            nameTextField.setPreferredSize(new Dimension(150, 20));
    
            /* priceLabel */
            priceLabel.setText("Цена");

            /* priceSpinner */
            priceSpinner.setModel(new SpinnerNumberModel(0, 0, 999999, 1));
            priceSpinner.setPreferredSize(new Dimension(150, 20));

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
            contentPane.add(nameLabel);
            contentPane.add(nameTextField);
            contentPane.add(Box.createRigidArea(new Dimension(0, 10)));
            contentPane.add(priceLabel);
            contentPane.add(priceSpinner);
            contentPane.add(Box.createRigidArea(new Dimension(0, 10)));
            contentPane.add(buttonPanel);

            /* this */
            setTitle("Добавить ингредиент");
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            setContentPane(contentPane);
            setModal(true);
            pack();
            setLocationRelativeTo(null);
        }

        private void okButtonClicked(ActionEvent event) {
            String name = nameTextField.getText();
            int price = (int) priceSpinner.getValue();

            result = new MenuItem();
            result.setName(name);
            result.setPrice(price);

            dispose();
        }

        private void cancelButtonClicked(ActionEvent event) {
            dispose();
        }

        MenuItem getResult() {
            return result;
        }
    }
}
