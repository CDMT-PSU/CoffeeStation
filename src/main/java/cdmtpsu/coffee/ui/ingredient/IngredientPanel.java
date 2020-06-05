package cdmtpsu.coffee.ui.ingredient;

import cdmtpsu.coffee.data.Database;
import cdmtpsu.coffee.data.Ingredient;
import cdmtpsu.coffee.util.Refreshable;
import com.j256.ormlite.dao.Dao;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import java.awt.BorderLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.ArrayList;

public final class IngredientPanel extends JPanel implements Refreshable {
    private final Window owner;

    private final Dao<Ingredient, Integer> dao;
    private final ArrayList<Ingredient> ingredients;
    private final TableModel tableModel;

    private final JButton addButton;
    private final JButton editButton;
    private final JButton removeButton;
    private final JButton decreaseButton;
    private final JButton increaseButton;
    private final JToolBar toolBar;
    private final JTable table;
    private final JScrollPane scrollPane;

    public IngredientPanel(Window owner) {
        this.owner = owner;

        dao = Database.getInstance().getIngredients();
        ingredients = new ArrayList<>();
        tableModel = new TableModel(ingredients);
        scrollPane = new JScrollPane();

        /* UI */
        addButton = new JButton();
        editButton = new JButton();
        removeButton = new JButton();
        decreaseButton = new JButton();
        increaseButton = new JButton();
        toolBar = new JToolBar();
        table = new JTable();

        /* addButton */
        addButton.setText("Добавить");
        addButton.addActionListener(this::addButtonClicked);

        /* editButton */
        editButton.setText("Редактировать");
        editButton.addActionListener(this::editButtonClicked);

        /* removeButton */
        removeButton.setText("Удалить");
        removeButton.addActionListener(this::removeButtonClicked);

        /* decreaseButton */
        decreaseButton.setText("Уменьшить");
        decreaseButton.addActionListener(this::decreaseButtonClicked);

        /* increaseButton */
        increaseButton.setText("Увеличить");
        increaseButton.addActionListener(this::increaseButtonClicked);

        /* toolBar */
        toolBar.setFloatable(false);
        toolBar.add(addButton);
        toolBar.add(editButton);
        toolBar.add(removeButton);
        toolBar.addSeparator();
        toolBar.add(decreaseButton);
        toolBar.add(increaseButton);

        /* table */
        table.setModel(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        /* scrollPane */
        scrollPane.setViewportView(table);

        /* this */
        setLayout(new BorderLayout());
        add(toolBar, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        /**/

        refresh();
    }

    /* Event handlers */
    private void addButtonClicked(ActionEvent event) {
        AddIngredientDialog dialog = new AddIngredientDialog(owner);
        dialog.setVisible(true);
        Ingredient result = dialog.getResult();
        if (result != null) {
            try {
                dao.create(result);
                refresh();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void editButtonClicked(ActionEvent event) {
        Ingredient ingredient = getSelectedIngredient();
        if (ingredient == null) {
            showIngredientNotSelectedMessageDialog();
            return;
        }
        EditIngredientDialog dialog = new EditIngredientDialog(owner, ingredient);
        dialog.setVisible(true);
        Ingredient result = dialog.getResult();
        if (result != null) {
            try {
                dao.update(result);
                refresh();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void removeButtonClicked(ActionEvent event) {
        Ingredient ingredient = getSelectedIngredient();
        if (ingredient == null) {
            showIngredientNotSelectedMessageDialog();
            return;
        }
        int confirmed = showIngredientDeletionConfirmationDialog();
        if (confirmed != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            dao.delete(ingredient);
            refresh();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void decreaseButtonClicked(ActionEvent event) {
        Ingredient ingredient = getSelectedIngredient();
        if (ingredient == null) {
            showIngredientNotSelectedMessageDialog();
            return;
        }
        DecreaseAmountDialog dialog = new DecreaseAmountDialog(owner, ingredient);
        dialog.setVisible(true);
        Ingredient result = dialog.getResult();
        if (result != null) {
            try {
                dao.update(result);
                refresh();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void increaseButtonClicked(ActionEvent event) {
        Ingredient ingredient = getSelectedIngredient();
        if (ingredient == null) {
            showIngredientNotSelectedMessageDialog();
            return;
        }
        IncreaseAmountDialog dialog = new IncreaseAmountDialog(owner, ingredient);
        dialog.setVisible(true);
        Ingredient result = dialog.getResult();
        if (result != null) {
            try {
                dao.update(result);
                refresh();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    /**/

    /* Logic */
    public void refresh() {
        ingredients.clear();
        dao.forEach(ingredients::add);
        tableModel.fireTableDataChanged();
    }

    private Ingredient getSelectedIngredient() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            return null;
        }
        return ingredients.get(selectedRow);
    }

    private void showIngredientNotSelectedMessageDialog() {
        JOptionPane.showMessageDialog(owner,
                "Укажите ингредиент в таблице.", "Внимание",
                JOptionPane.WARNING_MESSAGE);
    }

    private int showIngredientDeletionConfirmationDialog() {
        return JOptionPane.showConfirmDialog(owner,
                "Вы действительно хотите удалить указанный ингредиент?", "Подтверждение",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
    }
    /**/

    private static final class TableModel extends AbstractTableModel {
        private static final int COLUMN_COUNT = 3;
        private static final String[] COLUMN_NAMES = {"Название", "Единицы измерения", "Количество"};

        private final ArrayList<Ingredient> ingredients;

        TableModel(ArrayList<Ingredient> ingredients) {
            this.ingredients = ingredients;
        }

        @Override
        public int getRowCount() {
            return ingredients.size();
        }

        @Override
        public int getColumnCount() {
            return COLUMN_COUNT;
        }

        @Override
        public String getColumnName(int columnIndex) {
            return COLUMN_NAMES[columnIndex];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Ingredient ingredient = ingredients.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return ingredient.getName();
                case 1:
                    return ingredient.getUnit();
                case 2:
                    return ingredient.getAmount();
            }
            return null;
        }

        @Override
        public void setValueAt(Object value, int rowIndex, int columnIndex) {
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }
    }
}
