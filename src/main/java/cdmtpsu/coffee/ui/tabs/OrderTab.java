package cdmtpsu.coffee.ui.tabs;

import cdmtpsu.coffee.data.Database;
import cdmtpsu.coffee.data.Order;
import cdmtpsu.coffee.data.User;
import cdmtpsu.coffee.util.DaoCellEditor;
import cdmtpsu.coffee.util.TextFieldCellEditor;

import java.awt.Window;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class OrderTab extends Tab<Order> {
    private final User user;

    public OrderTab(User user) {
        super(
                new String[]{
                        "Номер",
                        "Пользователь",
                        "Дата"
                },
                new Class[]{
                        Void.class, /* crutch */
                        User.class,
                        String.class
                },
                Database.getInstance().getOrders(),
                true,
                user.getRole() == User.Role.ADMINISTRATOR,
                user.getRole() == User.Role.ADMINISTRATOR);
        setCellEditor("Пользователь", new DaoCellEditor<>(Database.getInstance().getUsers()));
        setCellEditor("Дата", new TextFieldCellEditor("([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))"));
        this.user = user;
    }

    @Override
    public Order createItem(Window owner) {
        Order order = new Order();
        order.setUser(user);
        order.setDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        return order;
    }
}
