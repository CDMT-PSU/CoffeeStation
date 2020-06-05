package cdmtpsu.coffee.ui.order;

import cdmtpsu.coffee.data.Database;
import cdmtpsu.coffee.data.Order;
import cdmtpsu.coffee.data.OrderItem;
import cdmtpsu.coffee.data.User;
import cdmtpsu.coffee.ui.orderitem.OrderItemPanel;
import cdmtpsu.coffee.util.CenterLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.ArrayList;

public final class EditOrderDialog extends JDialog {
    private final Order initial;
    private Result result;

    private final JLabel userLabel;
    private final JComboBox<User> userComboBox;

    private final JPanel userPanel;
    private final JLabel dateLabel;
    private final JTextField dateTextField;
    private final JPanel datePanel;
    private final JPanel fieldPanel;
    private final OrderItemPanel orderItemPanel;
    private final JLabel infoLabel;
    private final JButton okButton;
    private final JButton cancelButton;
    private final JPanel buttonPanel;
    private final JPanel contentPane;

    public EditOrderDialog(Window owner, Order initial) {
        super(owner);

        this.initial = initial;

        /* UI */
        userLabel = new JLabel();
        userComboBox = new JComboBox<>();
        userPanel = new JPanel();
        dateLabel = new JLabel();
        dateTextField = new JTextField();
        datePanel = new JPanel();
        fieldPanel = new JPanel();
        orderItemPanel = new OrderItemPanel(owner);
        infoLabel = new JLabel();
        okButton = new JButton();
        cancelButton = new JButton();
        buttonPanel = new JPanel();
        contentPane = new JPanel();

        /* userLabel */
        userLabel.setText("Пользователь");

        /* userComboBox */
        userComboBox.setPreferredSize(new Dimension(200, 24));
        Database.getInstance().getUsers().forEach(userComboBox::addItem);
        userComboBox.setRenderer(new ListCellRenderer());
        userComboBox.setSelectedItem(initial.getUser());
        // todo: set enanbled

        /* userPanel */
        userPanel.setLayout(new CenterLayout());
        userPanel.add(userLabel);
        userPanel.add(userComboBox);

        /* dateLabel */
        dateLabel.setText("Дата");

        /* dateTextField */
        dateTextField.setPreferredSize(new Dimension(200, 24));
        dateTextField.setText(initial.getDate());
        // todo: set enabled

        /* datePanel */
        datePanel.setLayout(new CenterLayout());
        datePanel.add(dateLabel);
        datePanel.add(dateTextField);

        /* fieldPanel */
        fieldPanel.add(userPanel);
        fieldPanel.add(datePanel);

        /* orderItemPanel */
        orderItemPanel.setPreferredSize(new Dimension(500, 300));
        try {
            Database.getInstance().getOrderItems().queryBuilder()
                    .where()
                    .eq(OrderItem.ORDER_FIELD_NAME, initial.getId())
                    .query()
                    .forEach(it -> orderItemPanel.getOrderItems().add(it));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // TODO: ПРОВЕРИТЬ ЕСТЬ ЛИ РЕФРЕШ!!!

        /* infoLabel */
        infoLabel.setText("Редактирование существующих заказов влияет на количество ингредиентов.");

        /* okButton */
        okButton.setPreferredSize(new Dimension(70, 24));
        okButton.setText("ОК");
        okButton.addActionListener(this::okButtonClicked);

        /* cancelButton */
        cancelButton.setPreferredSize(new Dimension(70, 24));
        cancelButton.setText("Отмена");
        cancelButton.addActionListener(this::cancelButtonClicked);

        /* buttonPanel */
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        /* contentPane */
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(new CenterLayout());
        contentPane.add(fieldPanel);
        contentPane.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPane.add(orderItemPanel);
        contentPane.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPane.add(infoLabel);
        contentPane.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPane.add(buttonPanel);

        /* this */
        setTitle("Редактировать заказ");
        setContentPane(contentPane);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setModal(true);
        pack();
        setLocationRelativeTo(owner);

        fieldValueChanged();
    }

    private void fieldValueChanged() {
        String date = dateTextField.getText();

        okButton.setEnabled(date.matches(Order.DATE_PATTERN));
    }

    private void okButtonClicked(ActionEvent event) {
        User user = (User) userComboBox.getSelectedItem();
        String date = dateTextField.getText();

        Order order = initial;
        order.setUser(user);
        order.setDate(date);

        ArrayList<OrderItem> orderItems = orderItemPanel.getOrderItems();
        for (OrderItem orderItem : orderItems) {
            if (orderItem.getOrder() == null) {
                orderItem.setOrder(order);
            }
        }

        result = new Result(order, orderItems);

        dispose();
    }

    private void cancelButtonClicked(ActionEvent event) {
        dispose();
    }

    public Result getResult() {
        return result;
    }

    public static final class Result {
        private final Order order;
        private final ArrayList<OrderItem> orderItems;

        private Result(Order order, ArrayList<OrderItem> orderItems) {
            this.order = order;
            this.orderItems = orderItems;
        }

        public Order getOrder() {
            return order;
        }

        public ArrayList<OrderItem> getOrderItems() {
            return orderItems;
        }
    }

    public static final class ListCellRenderer extends DefaultListCellRenderer {
        ListCellRenderer() {
        }

        @Override
        public Component getListCellRendererComponent(JList<?> list,
                                                      Object value,
                                                      int index,
                                                      boolean isSelected,
                                                      boolean cellHasFocus) {
            if (value instanceof User) {
                User user = (User) value;
                value = user.getName();
            }
            return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        }
    }
}