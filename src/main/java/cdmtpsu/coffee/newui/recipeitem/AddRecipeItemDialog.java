package cdmtpsu.coffee.newui.recipeitem;

import cdmtpsu.coffee.data.Database;
import cdmtpsu.coffee.data.Ingredient;
import cdmtpsu.coffee.data.RecipeItem;
import cdmtpsu.coffee.util.CenterLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;

public final class AddRecipeItemDialog extends JDialog {
    private RecipeItem result;

    private final JLabel ingredientLabel;
    private final JComboBox<Ingredient> ingredientComboBox;
    private final JLabel amountLabel;
    private final JSpinner amountSpinner;
    private final JButton okButton;
    private final JButton cancelButton;
    private final JPanel buttonPanel;
    private final JPanel contentPane;

    public AddRecipeItemDialog(Window owner) {
        super(owner);

        /* UI */
        ingredientLabel = new JLabel();
        ingredientComboBox = new JComboBox<>();
        amountLabel = new JLabel();
        amountSpinner = new JSpinner();
        okButton = new JButton();
        cancelButton = new JButton();
        buttonPanel = new JPanel();
        contentPane = new JPanel();

        /* ingredientLabel */
        ingredientLabel.setText("Ингредиент");

        /* ingredientComboBox */
        ingredientComboBox.setPreferredSize(new Dimension(200, 24));
        Database.getInstance().getIngredients().forEach(ingredientComboBox::addItem);
        ingredientComboBox.setRenderer(new ListCellRenderer());

        /* amountLabel */
        amountLabel.setText("Количетсво");

        /* priceSpinner */
        amountSpinner.setPreferredSize(new Dimension(200, 24));
        amountSpinner.setModel(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));

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
        contentPane.add(ingredientLabel);
        contentPane.add(ingredientComboBox);
        contentPane.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPane.add(amountLabel);
        contentPane.add(amountSpinner);
        contentPane.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPane.add(buttonPanel);

        /* this */
        setTitle("Добавить позицию рецепта");
        setContentPane(contentPane);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setModal(true);
        pack();
        setLocationRelativeTo(owner);
    }

    private void okButtonClicked(ActionEvent event) {
        Ingredient ingredient = (Ingredient) ingredientComboBox.getSelectedItem();
        int amount = (int) amountSpinner.getValue();

        result = new RecipeItem();
        result.setIngredient(ingredient);
        result.setAmount(amount);

        dispose();
    }

    private void cancelButtonClicked(ActionEvent event) {
        dispose();
    }

    public RecipeItem getResult() {
        return result;
    }

    public static final class ListCellRenderer extends DefaultListCellRenderer {
        ListCellRenderer() {
        }

        @Override
        public Component getListCellRendererComponent(JList<?> list,
                                                      Object value,
                                                      int index,
                                                      boolean isSelected,
                                                      boolean cellHasFocus) {
            if (value instanceof Ingredient) {
                Ingredient ingredient = (Ingredient) value;
                value = ingredient.getName() + ", " + ingredient.getUnit();
            }
            return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        }
    }
}