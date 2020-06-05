package cdmtpsu.coffee.newui.recipeitem;

import cdmtpsu.coffee.data.Database;
import cdmtpsu.coffee.data.Ingredient;
import cdmtpsu.coffee.data.RecipeItem;
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
import java.util.ArrayList;

public final class RecipeItemPanel extends JPanel {
    private final Window owner;

    private final Dao<RecipeItem, Integer> dao;
    private final ArrayList<RecipeItem> recipeItems;
    private final TableModel tableModel;

    private final JButton addButton;
    private final JButton editButton;
    private final JButton removeButton;
    private final JToolBar toolBar;
    private final JTable table;
    private final JScrollPane scrollPane;

    public RecipeItemPanel(Window owner) {
        this.owner = owner;

        dao = Database.getInstance().getRecipeItems();
        recipeItems = new ArrayList<>();
        tableModel = new TableModel(recipeItems);
        scrollPane = new JScrollPane();

        /* UI */
        addButton = new JButton();
        editButton = new JButton();
        removeButton = new JButton();
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

        /* toolBar */
        toolBar.setFloatable(false);
        toolBar.add(addButton);
        toolBar.add(editButton);
        toolBar.add(removeButton);

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
        AddRecipeItemDialog dialog = new AddRecipeItemDialog(owner);
        dialog.setVisible(true);
        RecipeItem result = dialog.getResult();
        if (result != null) {
            recipeItems.add(result);
            refresh();
        }
    }

    private void editButtonClicked(ActionEvent event) {
        RecipeItem recipeItem = getSelectedRecipeItem();
        if (recipeItem == null) {
            showRecipeItemNotSelectedMessageDialog();
            return;
        }
        EditRecipeItemDialog dialog = new EditRecipeItemDialog(owner, recipeItem);
        dialog.setVisible(true);
        RecipeItem result = dialog.getResult();
        if (result != null) {
            refresh();
        }
    }

    private void removeButtonClicked(ActionEvent event) {
        RecipeItem recipeItem = getSelectedRecipeItem();
        if (recipeItem == null) {
            showRecipeItemNotSelectedMessageDialog();
            return;
        }
        recipeItems.remove(recipeItem);
        refresh();
    }
    /**/

    /* Logic */
    public ArrayList<RecipeItem> getRecipeItems() {
        return recipeItems;
    }

    private void refresh() {
        tableModel.fireTableDataChanged();
    }

    private RecipeItem getSelectedRecipeItem() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            return null;
        }
        return recipeItems.get(selectedRow);
    }

    private void showRecipeItemNotSelectedMessageDialog() {
        JOptionPane.showMessageDialog(owner,
                "Укажите позицию рецепта в таблице.", "Внимание",
                JOptionPane.WARNING_MESSAGE);
    }

    private int showRecipeItemDeletionConfirmationDialog() {
        return JOptionPane.showConfirmDialog(owner,
                "Вы действительно хотите удалить указанную позицию рецепта?", "Подтверждение",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
    }
    /**/

    private static final class TableModel extends AbstractTableModel {
        private static final int COLUMN_COUNT = 2;
        private static final String[] COLUMN_NAMES = {"Ингредиент", "Количество"};

        private final ArrayList<RecipeItem> recipeItems;

        TableModel(ArrayList<RecipeItem> recipeItems) {
            this.recipeItems = recipeItems;
        }

        @Override
        public int getRowCount() {
            return recipeItems.size();
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
            RecipeItem recipeItem = recipeItems.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    Ingredient ingredient = recipeItem.getIngredient();
                    return ingredient.getName() + ", " + ingredient.getUnit();
                case 1:
                    return recipeItem.getAmount();
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
