package cdmtpsu.coffee.ui.menuitem;

import cdmtpsu.coffee.data.Database;
import cdmtpsu.coffee.data.MenuItem;
import cdmtpsu.coffee.data.RecipeItem;
import cdmtpsu.coffee.ui.recipeitem.RecipeItemPanel;
import cdmtpsu.coffee.util.CenterLayout;
import cdmtpsu.coffee.util.SwingUtils;
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
import java.sql.SQLException;
import java.util.ArrayList;

public final class EditMenuItemDialog extends JDialog {
    private final MenuItem initial;
    private Result result;

    private final JLabel nameLabel;
    private final JTextField nameTextField;
    private final JPanel namePanel;
    private final JLabel priceLabel;
    private final JSpinner priceSpinner;
    private final JPanel pricePanel;
    private final JPanel fieldPanel;
    private final RecipeItemPanel recipeItemPanel;
    private final JButton okButton;
    private final JButton cancelButton;
    private final JPanel buttonPanel;
    private final JPanel contentPane;

    public EditMenuItemDialog(Window owner, MenuItem initial) {
        super(owner);

        this.initial = initial;

        /* UI */
        nameLabel = new JLabel();
        nameTextField = new JTextField();
        namePanel = new JPanel();
        priceLabel = new JLabel();
        priceSpinner = new JSpinner();
        pricePanel = new JPanel();
        fieldPanel = new JPanel();
        recipeItemPanel = new RecipeItemPanel(owner);
        okButton = new JButton();
        cancelButton = new JButton();
        buttonPanel = new JPanel();
        contentPane = new JPanel();

        /* nameLabel */
        nameLabel.setText("Название");

        /* nameTextField */
        nameTextField.setPreferredSize(new Dimension(200, 24));
        SwingUtils.onValueChanged(nameTextField, this::fieldValueChanged);
        nameTextField.setText(initial.getName());

        /* namePanel */
        namePanel.setLayout(new CenterLayout());
        namePanel.add(nameLabel);
        namePanel.add(nameTextField);

        /* priceLabel */
        priceLabel.setText("Цена");

        /* priceSpinner */
        priceSpinner.setPreferredSize(new Dimension(200, 24));
        priceSpinner.setModel(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
        priceSpinner.setValue(initial.getPrice());

        /* pricePanel */
        pricePanel.setLayout(new CenterLayout());
        pricePanel.add(priceLabel);
        pricePanel.add(priceSpinner);

        /* fieldPanel */
        fieldPanel.add(namePanel);
        fieldPanel.add(pricePanel);

        /* recipeItemPanel */
        recipeItemPanel.setPreferredSize(new Dimension(500, 300));
        try {
            Database.getInstance().getRecipeItems().queryBuilder()
                    .where()
                    .eq(RecipeItem.MENU_ITEM_FIELD_NAME, initial.getId())
                    .query()
                    .forEach(it -> recipeItemPanel.getRecipeItems().add(it));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        recipeItemPanel.refresh();

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
        contentPane.add(fieldPanel);
        contentPane.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPane.add(recipeItemPanel);
        contentPane.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPane.add(buttonPanel);

        /* this */
        setTitle("Редактировать позицию меню");
        setContentPane(contentPane);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setModal(true);
        pack();
        setLocationRelativeTo(owner);

        fieldValueChanged();
    }

    private void fieldValueChanged() {
        String name = nameTextField.getText();

        okButton.setEnabled(name.length() > 0);
    }

    private void okButtonClicked(ActionEvent event) {
        String name = nameTextField.getText();
        int price = (int) priceSpinner.getValue();

        MenuItem menuItem = initial;
        menuItem.setName(name);
        menuItem.setPrice(price);

        ArrayList<RecipeItem> recipeItems = recipeItemPanel.getRecipeItems();
        for (RecipeItem recipeItem : recipeItems) {
            if (recipeItem.getMenuItem() == null) {
                recipeItem.setMenuItem(menuItem);
            }
        }

        result = new Result(menuItem, recipeItems);

        dispose();
    }

    private void cancelButtonClicked(ActionEvent event) {
        dispose();
    }

    public Result getResult() {
        return result;
    }

    public static final class Result {
        private final MenuItem menuItem;
        private final ArrayList<RecipeItem> recipeItems;

        private Result(MenuItem menuItem, ArrayList<RecipeItem> recipeItems) {
            this.menuItem = menuItem;
            this.recipeItems = recipeItems;
        }

        public MenuItem getMenuItem() {
            return menuItem;
        }

        public ArrayList<RecipeItem> getRecipeItems() {
            return recipeItems;
        }
    }
}