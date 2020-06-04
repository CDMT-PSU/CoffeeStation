package cdmtpsu.coffee.ui.tabs;

import cdmtpsu.coffee.data.Database;
import cdmtpsu.coffee.data.Ingredient;
import cdmtpsu.coffee.data.MenuItem;
import cdmtpsu.coffee.data.RecipeItem;
import cdmtpsu.coffee.data.User;
import cdmtpsu.coffee.util.CenterLayout;
import cdmtpsu.coffee.util.DaoCellEditor;
import cdmtpsu.coffee.util.SpinnerCellEditor;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.sql.SQLException;

public final class RecipeItemTab extends Tab<RecipeItem> {
    public RecipeItemTab(User user) {
        super(
                new String[]{
                        "Позиция меню",
                        "Ингредиент",
                        "Количество"
                },
                new Class[]{
                        MenuItem.class,
                        Ingredient.class,
                        Integer.class
                },
                Database.getInstance().getRecipeItems(),
                user.getRole() == User.Role.ADMINISTRATOR,
                user.getRole() == User.Role.ADMINISTRATOR,
                user.getRole() == User.Role.ADMINISTRATOR);
        setCellEditor("Позиция меню", new DaoCellEditor<>(Database.getInstance().getMenuItems()));
        setCellEditor("Ингредиент", new DaoCellEditor<>(Database.getInstance().getIngredients()));
        setCellEditor("Количество", new SpinnerCellEditor(1, 999999));
    }

    @Override
    public RecipeItem createItem(Window owner) {
        MenuItem menuItem = null;
        Ingredient ingredient = null;
        try {
            menuItem = Database.getInstance().getMenuItems().queryBuilder().queryForFirst();
            ingredient = Database.getInstance().getIngredients().queryBuilder().queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (menuItem == null) {
            JOptionPane.showMessageDialog(
                    owner,
                    "Нет доступных позиций меню",
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE
            );
            return null;
        }

        if (ingredient == null) {
            JOptionPane.showMessageDialog(
                    owner,
                    "Нет доступных ингредиентов",
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE
            );
            return null;
        }

        AddDialog dialog = new AddDialog();
        dialog.setVisible(true);
        return dialog.getResult();
    }

    private static final class AddDialog extends JDialog {
        /* ui components */
        private final JLabel menuItemLabel;
        private final JComboBox<MenuItem> menuItemComboBox;
        private final JLabel ingredientLabel;
        private final JComboBox<Ingredient> ingredientComboBox;
        private final JLabel amountLabel;
        private final JSpinner amountSpinner;
        private final JButton okButton;
        private final JButton cancelButton;
        private final JPanel buttonPanel;
        private final JPanel contentPane;

        private RecipeItem result;

        AddDialog() {
            /* init components */
            menuItemLabel = new JLabel();
            menuItemComboBox = new JComboBox<>();
            ingredientLabel = new JLabel();
            ingredientComboBox = new JComboBox<>();
            amountLabel = new JLabel();
            amountSpinner = new JSpinner();
            okButton = new JButton();
            cancelButton = new JButton();
            buttonPanel = new JPanel();
            contentPane = new JPanel();

            /* menuItemLabel */
            menuItemLabel.setText("Позиция меню");

            /* menuItemComboBox */
            menuItemComboBox.setPreferredSize(new Dimension(150, 20));
            Database.getInstance().getMenuItems().forEach(menuItemComboBox::addItem);

            /* ingredientLabel */
            ingredientLabel.setText("Ингредиент");

            /* ingredientComboBox */
            ingredientComboBox.setPreferredSize(new Dimension(150, 20));
            Database.getInstance().getIngredients().forEach(ingredientComboBox::addItem);

            /* amountLabel */
            amountLabel.setText("Количество");

            /* amountSpinner */
            amountSpinner.setPreferredSize(new Dimension(150, 20));
            amountSpinner.setModel(new SpinnerNumberModel(1, 1, 999999, 1));

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
            contentPane.add(menuItemLabel);
            contentPane.add(menuItemComboBox);
            contentPane.add(Box.createRigidArea(new Dimension(0, 10)));
            contentPane.add(ingredientLabel);
            contentPane.add(ingredientComboBox);
            contentPane.add(Box.createRigidArea(new Dimension(0, 10)));
            contentPane.add(amountLabel);
            contentPane.add(amountSpinner);
            contentPane.add(Box.createRigidArea(new Dimension(0, 10)));
            contentPane.add(buttonPanel);

            /* this */
            setTitle("Добавить позицию рецепта");
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            setContentPane(contentPane);
            setModal(true);
            pack();
            setLocationRelativeTo(null);
        }

        private void okButtonClicked(ActionEvent event) {
            MenuItem menuItem = (MenuItem) menuItemComboBox.getSelectedItem();
            Ingredient ingredient = (Ingredient) ingredientComboBox.getSelectedItem();
            int amount = (int) amountSpinner.getValue();

            result = new RecipeItem();
            result.setMenuItem(menuItem);
            result.setIngredient(ingredient);
            result.setAmount(amount);

            dispose();
        }

        private void cancelButtonClicked(ActionEvent event) {
            dispose();
        }

        RecipeItem getResult() {
            return result;
        }
    }
}
