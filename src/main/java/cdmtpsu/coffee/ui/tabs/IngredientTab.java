package cdmtpsu.coffee.ui.tabs;

import cdmtpsu.coffee.data.Database;
import cdmtpsu.coffee.data.Ingredient;
import cdmtpsu.coffee.data.User;
import cdmtpsu.coffee.util.CenterLayout;
import cdmtpsu.coffee.util.TextFieldCellEditor;

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

public final class IngredientTab extends Tab<Ingredient> {
    public IngredientTab(User user) {
        super(
                new String[]{
                        "Название",
                        "Единицы измерения"
                },
                new Class[]{
                        String.class,
                        String.class
                },
                Database.getInstance().getIngredients(),
                user.getRole() == User.Role.ADMINISTRATOR,
                user.getRole() == User.Role.ADMINISTRATOR,
                user.getRole() == User.Role.ADMINISTRATOR);
        setCellEditor("Название", new TextFieldCellEditor("[\\s\\S]+"));
        setCellEditor("Единицы измерения", new TextFieldCellEditor("[\\s\\S]+"));
    }

    @Override
    public Ingredient createItem(Window owner) {
        AddDialog dialog = new AddDialog();
        dialog.setVisible(true);
        return dialog.getResult();
    }

    private static final class AddDialog extends JDialog {
        /* ui components */
        private final JLabel nameLabel;
        private final JTextField nameTextField;
        private final JLabel unitLabel;
        private final JTextField unitTextField;
        private final JButton okButton;
        private final JButton cancelButton;
        private final JPanel buttonPanel;
        private final JPanel contentPane;

        private Ingredient result;

        AddDialog() {
            /* init components */
            nameLabel = new JLabel();
            nameTextField = new JTextField();
            unitLabel = new JLabel();
            unitTextField = new JTextField();
            okButton = new JButton();
            cancelButton = new JButton();
            buttonPanel = new JPanel();
            contentPane = new JPanel();

            /* nameLabel */
            nameLabel.setText("Название");

            /* nameTextField */
            nameTextField.setPreferredSize(new Dimension(250, 20));

            /* unitLabel */
            unitLabel.setText("Единицы измерения");

            /* unitTextField */
            unitTextField.setPreferredSize(new Dimension(250, 20));

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
            contentPane.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
            contentPane.setLayout(new CenterLayout());
            contentPane.add(nameLabel);
            contentPane.add(nameTextField);
            contentPane.add(Box.createRigidArea(new Dimension(0, 10)));
            contentPane.add(unitLabel);
            contentPane.add(unitTextField);
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
            String unit = unitTextField.getText();

            result = new Ingredient();
            result.setName(name);
            result.setUnit(unit);

            dispose();
        }

        private void cancelButtonClicked(ActionEvent event) {
            dispose();
        }

        Ingredient getResult() {
            return result;
        }
    }
}
