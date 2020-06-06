package cdmtpsu.coffee.ui.recipeitem;

import cdmtpsu.coffee.data.Database;
import cdmtpsu.coffee.data.Ingredient;
import cdmtpsu.coffee.data.RecipeItem;
import cdmtpsu.coffee.util.Refreshable;
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

public final class ReadOnlyRecipeItemPanel extends JPanel implements Refreshable {
    private final Window owner;

    private final ArrayList<RecipeItem> recipeItems;
    private final TableModel tableModel;

    private final JTable table;
    private final JScrollPane scrollPane;

    public ReadOnlyRecipeItemPanel(Window owner) {
        this.owner = owner;

        recipeItems = new ArrayList<>();
        tableModel = new TableModel(recipeItems);
        scrollPane = new JScrollPane();

        /* UI */
        table = new JTable();

        /* table */
        table.setModel(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        /* scrollPane */
        scrollPane.setViewportView(table);

        /* this */
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        /**/

        refresh();
    }

    public ArrayList<RecipeItem> getRecipeItems() {
        return recipeItems;
    }

    public void refresh() {
        tableModel.fireTableDataChanged();
    }

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
