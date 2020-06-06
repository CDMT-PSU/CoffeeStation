package cdmtpsu.coffee.ui.order;

import cdmtpsu.coffee.data.Database;
import cdmtpsu.coffee.data.Order;
import cdmtpsu.coffee.data.OrderItem;
import cdmtpsu.coffee.data.User;
import cdmtpsu.coffee.ui.orderitem.OrderItemPanel;
import cdmtpsu.coffee.ui.orderitem.ReadOnlyOrderItemPanel;
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

public final class ViewOrderDialog extends JDialog {
    private final ReadOnlyOrderItemPanel orderItemPanel;
    private final JPanel contentPane;

    public ViewOrderDialog(Window owner, Order order) {
        super(owner);

        /* UI */
        orderItemPanel = new ReadOnlyOrderItemPanel(owner);
        contentPane = new JPanel();

        /* orderItemPanel */
        orderItemPanel.setPreferredSize(new Dimension(500, 300));
        try {
            Database.getInstance().getOrderItems().queryBuilder()
                    .where()
                    .eq(OrderItem.ORDER_FIELD_NAME, order.getId())
                    .query()
                    .forEach(it -> orderItemPanel.getOrderItems().add(it));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        orderItemPanel.refresh();

        /* contentPane */
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(new CenterLayout());
        contentPane.add(orderItemPanel);

        /* this */
        setTitle("Просмотреть заказ");
        setContentPane(contentPane);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setModal(true);
        pack();
        setLocationRelativeTo(owner);
    }
}