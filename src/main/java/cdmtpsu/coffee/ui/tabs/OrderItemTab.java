package cdmtpsu.coffee.ui.tabs;

import cdmtpsu.coffee.data.Database;
import cdmtpsu.coffee.data.MenuItem;
import cdmtpsu.coffee.data.Order;
import cdmtpsu.coffee.data.OrderItem;
import cdmtpsu.coffee.data.User;
import cdmtpsu.coffee.util.CenterLayout;
import cdmtpsu.coffee.util.DaoCellEditor;
import cdmtpsu.coffee.util.SpinnerCellEditor;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public final class OrderItemTab extends Tab<OrderItem> {
    private final User user;

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
                true,
                user.getRole() == User.Role.ADMINISTRATOR,
                user.getRole() == User.Role.ADMINISTRATOR);
        setCellEditor("Заказ", new DaoCellEditor<>(Database.getInstance().getOrders()));
        setCellEditor("Позиция меню", new DaoCellEditor<>(Database.getInstance().getMenuItems()));
        setCellEditor("Количество", new SpinnerCellEditor(1, 999999));
        this.user = user;
    }

    @Override
    public OrderItem createItem(Window owner) {
        List<Order> orders = queryOrders();
        MenuItem menuItem = null;
        try {
            menuItem = Database.getInstance().getMenuItems().queryBuilder().queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (orders.size() == 0) {
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

        AddDialog dialog = new AddDialog();
        dialog.setVisible(true);
        return dialog.getResult();
    }

    private List<Order> queryOrders() {
        try {
            return Database.getInstance().getOrders().queryBuilder()
                    .where()
                    .eq(Order.USER_FIELD_NAME, user.getId())
                    .and()
                    .eq(Order.DATE_FIELD_NAME, new SimpleDateFormat("yyyy-MM-dd").format(new Date())).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return List.of();
    }

    private /*static*/ final class AddDialog extends JDialog {
        /* ui components */
        private final JLabel orderLabel;
        private final JComboBox<Order> orderComboBox;
        private final JLabel menuItemLabel;
        private final JComboBox<MenuItem> menuItemComboBox;
        private final JLabel amountLabel;
        private final JSpinner amountSpinner;
        private final JButton okButton;
        private final JButton cancelButton;
        private final JPanel buttonPanel;
        private final JPanel contentPane;

        private OrderItem result;

        AddDialog() {
            /* init components */
            orderLabel = new JLabel();
            orderComboBox = new JComboBox<>();
            menuItemLabel = new JLabel();
            menuItemComboBox = new JComboBox<>();
            amountLabel = new JLabel();
            amountSpinner = new JSpinner();
            okButton = new JButton();
            cancelButton = new JButton();
            buttonPanel = new JPanel();
            contentPane = new JPanel();

            /* orderLabel */
            orderLabel.setText("Заказ");

            /* orderComboBox */
            orderComboBox.setPreferredSize(new Dimension(250, 20));
            queryOrders().forEach(orderComboBox::addItem);

            /* menuItemLabel */
            menuItemLabel.setText("Позиция меню");

            /* menuItemComboBox */
            menuItemComboBox.setPreferredSize(new Dimension(250, 20));
            Database.getInstance().getMenuItems().forEach(menuItemComboBox::addItem);

            /* amountLabel */
            amountLabel.setText("Количество");

            /* amountSpinner */
            amountSpinner.setModel(new SpinnerNumberModel(1, 1, 999999, 1));
            amountSpinner.setPreferredSize(new Dimension(250, 20));

            /* okButton */
            okButton.setText("ОК");
            okButton.addActionListener(this::okButtonClicked);

            /* cancelButton */
            cancelButton.setText("Отмена");
            cancelButton.addActionListener(this::cancelButtonClicked);

            /* buttonPanel */
            buttonPanel.add(okButton);
            buttonPanel.add(cancelButton);

            /* contentPane */
            contentPane.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
            contentPane.setLayout(new CenterLayout());
            contentPane.add(orderLabel);
            contentPane.add(orderComboBox);
            contentPane.add(Box.createRigidArea(new Dimension(0, 10)));
            contentPane.add(menuItemLabel);
            contentPane.add(menuItemComboBox);
            contentPane.add(Box.createRigidArea(new Dimension(0, 10)));
            contentPane.add(amountLabel);
            contentPane.add(amountSpinner);
            contentPane.add(Box.createRigidArea(new Dimension(0, 10)));
            contentPane.add(buttonPanel);

            /* this */
            setTitle("Добавить позицию заказа");
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            setContentPane(contentPane);
            setModal(true);
            pack();
            setLocationRelativeTo(null);
        }

        private void okButtonClicked(ActionEvent event) {
            Order order = (Order) orderComboBox.getSelectedItem();
            MenuItem menuItem = (MenuItem) menuItemComboBox.getSelectedItem();
            int amount = (int) amountSpinner.getValue();

            result = new OrderItem();
            result.setOrder(order);
            result.setMenuItem(menuItem);
            result.setAmount(amount);

            dispose();
        }

        private void cancelButtonClicked(ActionEvent event) {
            dispose();
        }

        OrderItem getResult() {
            return result;
        }
    }
}
