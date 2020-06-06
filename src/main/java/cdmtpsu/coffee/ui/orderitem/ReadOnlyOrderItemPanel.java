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

public final class ReadOnlyOrderItemPanel extends JPanel implements Refreshable {
    private final Window owner;

    private final ArrayList<OrderItem> orderItems;
    private final TableModel tableModel;

    private final JTable table;
    private final JScrollPane scrollPane;

    public ReadOnlyOrderItemPanel(Window owner) {
        this.owner = owner;

        orderItems = new ArrayList<>();
        tableModel = new TableModel(orderItems);
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
