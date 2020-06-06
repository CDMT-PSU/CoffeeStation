package cdmtpsu.coffee.ui.order;

import cdmtpsu.coffee.data.Database;
import cdmtpsu.coffee.data.Order;
import cdmtpsu.coffee.data.OrderItem;
import cdmtpsu.coffee.data.User;
import cdmtpsu.coffee.ui.main.MainFrame;
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
import java.util.List;

public final class OrderPanel extends JPanel implements Refreshable {
    private final Window owner;

    private final Dao<Order, Integer> dao;
    private final ArrayList<Order> orders;
    private final TableModel tableModel;

    private final JButton addButton;
    private final JButton editButton;
    private final JButton removeButton;
    private final JButton viewButton;
    private final JToolBar toolBar;
    private final JTable table;
    private final JScrollPane scrollPane;

    public OrderPanel(Window owner) {
        this.owner = owner;

        User sessionUser = ((MainFrame) owner).getUser();

        dao = Database.getInstance().getOrders();
        orders = new ArrayList<>();
        tableModel = new TableModel(orders);
        scrollPane = new JScrollPane();

        /* UI */
        addButton = new JButton();
        editButton = new JButton();
        removeButton = new JButton();
        viewButton = new JButton();
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

        /* viewButton */
        viewButton.setText("Просмотреть заказ");
        viewButton.addActionListener(this::viewButtonClicked);

        /* toolBar */
        toolBar.setFloatable(false);
        toolBar.add(addButton);
        /* Только Администратор может удалять заказы. */
        /* Только Администратор может редактировать существующие заказы. */
        if (sessionUser.getRole() == User.Role.ADMINISTRATOR) {
            toolBar.add(editButton);
            toolBar.add(removeButton);
        }
        toolBar.addSeparator();
        toolBar.add(viewButton);

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
        AddOrderDialog dialog = new AddOrderDialog(owner);
        dialog.setVisible(true);
        AddOrderDialog.Result result = dialog.getResult();
        if (result != null) {
            Order resultOrder = result.getOrder();
            try {
                dao.create(resultOrder);
                ArrayList<OrderItem> resultOrderItems = result.getOrderItems();
                resultOrderItems.forEach(orderItem -> {
                    try {
                        Database.getInstance().getOrderItems().create(orderItem);
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
        Order order = getSelectedOrder();
        if (order == null) {
            showOrderNotSelectedMessageDialog();
            return;
        }
        EditOrderDialog dialog = new EditOrderDialog(owner, order);
        dialog.setVisible(true);
        EditOrderDialog.Result result = dialog.getResult();
        if (result != null) {
            Order resultOrder = result.getOrder();
            try {
                dao.update(resultOrder);
                // Удаляем все `order_item` для данного `order`.
                Dao<OrderItem, Integer> dao1 = Database.getInstance().getOrderItems();
                dao1.queryBuilder()
                        .where()
                        .eq(OrderItem.ORDER_FIELD_NAME, resultOrder.getId())
                        .query()
                        .forEach(it -> {
                            try {
                                dao1.delete(it);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        });
                // Записываем новые.
                ArrayList<OrderItem> resultOrderItems = result.getOrderItems();
                resultOrderItems.forEach(orderItem -> {
                    try {
                        dao1.create(orderItem);
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
        Order order = getSelectedOrder();
        if (order == null) {
            showOrderNotSelectedMessageDialog();
            return;
        }
        int confirmed = showOrderDeletionConfirmationDialog();
        if (confirmed != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            dao.delete(order);
            refresh();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void viewButtonClicked(ActionEvent event) {
        Order order = getSelectedOrder();
        if (order == null) {
            showOrderNotSelectedMessageDialog();
            return;
        }
        new ViewOrderDialog(owner, order).setVisible(true);
    }
    /**/

    /* Logic */
    public void refresh() {
        User sessionUser = ((MainFrame) owner).getUser();

        orders.clear();

        /* Для обычного пользователя будем грузить заказы лишь на текущую дату, админу - все. */
        if (sessionUser.getRole() == User.Role.ADMINISTRATOR) {
            dao.forEach(orders::add);
        } else {

            try {
                List<Order> todayOrders = dao.queryBuilder()
                        .where().eq(Order.DATE_FIELD_NAME, Database.getCurrentDate()).query();
                orders.addAll(todayOrders);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        tableModel.fireTableDataChanged();
    }

    private Order getSelectedOrder() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            return null;
        }
        return orders.get(selectedRow);
    }

    private void showOrderNotSelectedMessageDialog() {
        JOptionPane.showMessageDialog(owner,
                "Укажите заказ в таблице.", "Внимание",
                JOptionPane.WARNING_MESSAGE);
    }

    private int showOrderDeletionConfirmationDialog() {
        return JOptionPane.showConfirmDialog(owner,
                "Вы действительно хотите удалить указанный заказ?", "Подтверждение",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
    }
    /**/

    private static final class TableModel extends AbstractTableModel {
        private static final int COLUMN_COUNT = 3;
        private static final String[] COLUMN_NAMES = {"Номер заказа", "Пользователь", "Дата"};

        private final ArrayList<Order> orders;

        TableModel(ArrayList<Order> orders) {
            this.orders = orders;
        }

        @Override
        public int getRowCount() {
            return orders.size();
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
            Order order = orders.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return order.getId();
                case 1:
                    return order.getUser().getName();
                case 2:
                    return order.getDate();
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
