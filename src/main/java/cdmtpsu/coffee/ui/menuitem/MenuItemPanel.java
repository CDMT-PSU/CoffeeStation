package cdmtpsu.coffee.ui.menuitem;

import cdmtpsu.coffee.data.Database;
import cdmtpsu.coffee.data.MenuItem;
import cdmtpsu.coffee.data.RecipeItem;
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

public final class MenuItemPanel extends JPanel implements Refreshable {
    private final Window owner;

    private final Dao<MenuItem, Integer> dao;
    private final ArrayList<MenuItem> menuItems;
    private final TableModel tableModel;

    private final JButton addButton;
    private final JButton editButton;
    private final JButton removeButton;
    private final JToolBar toolBar;
    private final JTable table;
    private final JScrollPane scrollPane;

    public MenuItemPanel(Window owner) {
        this.owner = owner;

        dao = Database.getInstance().getMenuItems();
        menuItems = new ArrayList<>();
        tableModel = new TableModel(menuItems);
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
        AddMenuItemDialog dialog = new AddMenuItemDialog(owner);
        dialog.setVisible(true);
        AddMenuItemDialog.Result result = dialog.getResult();
        if (result != null) {
            MenuItem resultMenuItem = result.getMenuItem();
            try {
                dao.create(resultMenuItem);
                ArrayList<RecipeItem> resultRecipeItems = result.getRecipeItems();
                resultRecipeItems.forEach(recipeItem -> {
                    try {
                        Database.getInstance().getRecipeItems().create(recipeItem);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
                refresh();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void editButtonClicked(ActionEvent event) {
        MenuItem menuItem = getSelectedMenuItem();
        if (menuItem == null) {
            showMenuItemNotSelectedMessageDialog();
            return;
        }
        EditMenuItemDialog dialog = new EditMenuItemDialog(owner, menuItem);
        dialog.setVisible(true);
        EditMenuItemDialog.Result result = dialog.getResult();
        if (result != null) {
            MenuItem resultMenuItem = result.getMenuItem();
            try {
                dao.update(resultMenuItem);
                /* Удаляем все `recipe_item` для данного `menu_item`. */
                Dao<RecipeItem, Integer> dao1 = Database.getInstance().getRecipeItems();
                dao1.queryBuilder()
                        .where()
                        .eq(RecipeItem.MENU_ITEM_FIELD_NAME, resultMenuItem.getId())
                        .query()
                        .forEach(it -> {
                            try {
                                dao1.delete(it);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        });
                /* Записываем новые. */
                ArrayList<RecipeItem> resultRecipeItems = result.getRecipeItems();
                resultRecipeItems.forEach(recipeItem -> {
                    try {
                        dao1.create(recipeItem);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
                refresh();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void removeButtonClicked(ActionEvent event) {
        MenuItem menuItem = getSelectedMenuItem();
        if (menuItem == null) {
            showMenuItemNotSelectedMessageDialog();
            return;
        }
        int confirmed = showMenuItemDeletionConfirmationDialog();
        if (confirmed != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            dao.delete(menuItem);
            refresh();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**/

    /* Logic */
    public void refresh() {
        menuItems.clear();
        dao.forEach(menuItems::add);
        tableModel.fireTableDataChanged();
    }

    private MenuItem getSelectedMenuItem() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            return null;
        }
        return menuItems.get(selectedRow);
    }

    private void showMenuItemNotSelectedMessageDialog() {
        JOptionPane.showMessageDialog(owner,
                "Укажите позицию меню в таблице.", "Внимание",
                JOptionPane.WARNING_MESSAGE);
    }

    private int showMenuItemDeletionConfirmationDialog() {
        return JOptionPane.showConfirmDialog(owner,
                "Вы действительно хотите удалить указанную позицию меню?", "Подтверждение",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
    }
    /**/

    private static final class TableModel extends AbstractTableModel {
        private static final int COLUMN_COUNT = 2;
        private static final String[] COLUMN_NAMES = {"Название", "Цена"};

        private final ArrayList<MenuItem> menuItems;

        TableModel(ArrayList<MenuItem> menuItems) {
            this.menuItems = menuItems;
        }

        @Override
        public int getRowCount() {
            return menuItems.size();
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
            MenuItem menuItem = menuItems.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return menuItem.getName();
                case 1:
                    return menuItem.getPrice();
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
