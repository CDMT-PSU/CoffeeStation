package cdmtpsu.coffee.ui.tabs;

import cdmtpsu.coffee.data.Database;
import cdmtpsu.coffee.data.MenuItem;
import cdmtpsu.coffee.data.Order;
import cdmtpsu.coffee.data.OrderItem;
import cdmtpsu.coffee.data.User;
import cdmtpsu.coffee.util.DaoCellEditor;
import cdmtpsu.coffee.util.SpinnerCellEditor;

import javax.swing.JOptionPane;
import java.awt.Window;
import java.sql.SQLException;

public final class OrderItemTab extends Tab<OrderItem> {
    public OrderItemTab(User user) {
        super(
                new String[]{
                        "Заказ",
                        "Позиция меню",
                        "Количество"
                },
                new Class[]{
                        Order.class,
                        MenuItem.class,
                        Integer.class
                },
                Database.getInstance().getOrderItems(),
                user.getRole() == User.Role.USER,
                user.getRole() == User.Role.ADMINISTRATOR,
                user.getRole() == User.Role.ADMINISTRATOR);
        setCellEditor("Заказ", new DaoCellEditor<>(Database.getInstance().getOrders()));
        setCellEditor("Позиция меню", new DaoCellEditor<>(Database.getInstance().getMenuItems()));
        setCellEditor("Количество", new SpinnerCellEditor(1, 999999));
    }

    @Override
    public OrderItem createItem(Window owner) {
        Order order = null;
        MenuItem menuItem = null;
        try {
            order = Database.getInstance().getOrders().queryBuilder().queryForFirst();
            menuItem = Database.getInstance().getMenuItems().queryBuilder().queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (order == null) {
            JOptionPane.showMessageDialog(
                    owner,
                    "Нет доступных заказов",
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE
            );
            return null;
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

        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setAmount(1);
        return orderItem;
    }
}
