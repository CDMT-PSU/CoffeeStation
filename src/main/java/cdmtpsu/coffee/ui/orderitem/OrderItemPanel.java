package cdmtpsu.coffee.ui.orderitem;

import cdmtpsu.coffee.data.Database;
import cdmtpsu.coffee.data.MenuItem;
import cdmtpsu.coffee.data.OrderItem;
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

public final class OrderItemPanel extends JPanel implements Refreshable {
    private final Window owner;

    private final ArrayList<OrderItem> orderItems;
    private final TableModel tableModel;

    private final JButton addButton;
    private final JButton editButton;
    private final JButton removeButton;
    private final JToolBar toolBar;
    private final JTable table;
    private final JScrollPane scrollPane;

    public OrderItemPanel(Window owner) {
        this.owner = owner;

        orderItems = new ArrayList<>();
        tableModel = new TableModel(orderItems);
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
        /* Проверяем есть ли у нас хотя бы одна позиция в меню. */
        try {
            if (Database.getInstance().getIngredients().countOf() == 0) {
                showNoMenuItemsMessageDialog();
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        AddOrderItemDialog dialog = new AddOrderItemDialog(owner);
        dialog.setVisible(true);
        OrderItem result = dialog.getResult();
        if (result != null) {
            orderItems.add(result);
            refresh();
        }
    }

    private void editButtonClicked(ActionEvent event) {
        OrderItem orderItem = getSelectedOrderItem();
        if (orderItem == null) {
            showOrderItemNotSelectedMessageDialog();
            return;
        }
        EditOrderItemDialog dialog = new EditOrderItemDialog(owner, orderItem);
        dialog.setVisible(true);
        OrderItem result = dialog.getResult();
        if (result != null) {
            refresh();
        }
    }

    private void removeButtonClicked(ActionEvent event) {
        OrderItem orderItem = getSelectedOrderItem();
        if (orderItem == null) {
            showOrderItemNotSelectedMessageDialog();
            return;
        }
        int confirmed = showRecipeItemDeletionConfirmationDialog();
        if (confirmed != JOptionPane.YES_OPTION) {
            return;
        }
        orderItems.remove(orderItem);
        refresh();
    }
    /**/

    /* Logic */
    public ArrayList<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void refresh() {
        tableModel.fireTableDataChanged();
    }

    private OrderItem getSelectedOrderItem() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            return null;
        }
        return orderItems.get(selectedRow);
    }

    private void showOrderItemNotSelectedMessageDialog() {
        JOptionPane.showMessageDialog(owner,
                "Укажите позицию заказа в таблице.", "Внимание",
                JOptionPane.WARNING_MESSAGE);
    }

    private void showNoMenuItemsMessageDialog() {
        JOptionPane.showMessageDialog(owner,
                "Вы не добавили ни одной позиции меню.\nПерейдите во вкладку 'Меню'.", "Внимание",
                JOptionPane.WARNING_MESSAGE);
    }

    private int showRecipeItemDeletionConfirmationDialog() {
        return JOptionPane.showConfirmDialog(owner,
                "Вы действительно хотите удалить указанную позицию заказа?", "Подтверждение",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
    }
    /**/

    private static final class TableModel extends AbstractTableModel {
        private static final int COLUMN_COUNT = 2;
        private static final String[] COLUMN_NAMES = {"Позиция меню", "Количество"};

        private final ArrayList<OrderItem> orderItems;

        TableModel(ArrayList<OrderItem> orderItems) {
            this.orderItems = orderItems;
        }

        @Override
        public int getRowCount() {
            return orderItems.size();
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
            OrderItem orderItem = orderItems.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    MenuItem menuItem = orderItem.getMenuItem();
                    return menuItem.getName() + ", " + menuItem.getPrice() + " руб.";
                case 1:
                    return orderItem.getAmount();
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
