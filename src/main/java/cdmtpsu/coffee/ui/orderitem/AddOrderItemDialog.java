package cdmtpsu.coffee.ui.orderitem;

import cdmtpsu.coffee.data.Database;
import cdmtpsu.coffee.data.MenuItem;
import cdmtpsu.coffee.data.OrderItem;
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
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;

public final class AddOrderItemDialog extends JDialog {
    private OrderItem result;

    private final JLabel menuItemLabel;
    private final JComboBox<MenuItem> menuItemComboBox;
    private final JLabel amountLabel;
    private final JSpinner amountSpinner;
    private final JButton okButton;
    private final JButton cancelButton;
    private final JPanel buttonPanel;
    private final JPanel contentPane;

    public AddOrderItemDialog(Window owner) {
        super(owner);

        /* UI */
        menuItemLabel = new JLabel();
        menuItemComboBox = new JComboBox<>();
        amountLabel = new JLabel();
        amountSpinner = new JSpinner();
        okButton = new JButton();
        cancelButton = new JButton();
        buttonPanel = new JPanel();
        contentPane = new JPanel();

        /* ingredientLabel */
        menuItemLabel.setText("Позиция меню");

        /* ingredientComboBox */
        menuItemComboBox.setPreferredSize(new Dimension(200, 24));
        Database.getInstance().getMenuItems().forEach(menuItemComboBox::addItem);
        menuItemComboBox.setRenderer(new ListCellRenderer());

        /* amountLabel */
        amountLabel.setText("Количетсво");

        /* priceSpinner */
        amountSpinner.setPreferredSize(new Dimension(200, 24));
        amountSpinner.setModel(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));

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
        contentPane.add(menuItemLabel);
        contentPane.add(menuItemComboBox);
        contentPane.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPane.add(amountLabel);
        contentPane.add(amountSpinner);
        contentPane.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPane.add(buttonPanel);

        /* this */
        setTitle("Добавить позицию заказа");
        setContentPane(contentPane);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setModal(true);
        pack();
        setLocationRelativeTo(owner);
    }

    private void okButtonClicked(ActionEvent event) {
        MenuItem menuItem = (MenuItem) menuItemComboBox.getSelectedItem();
        int amount = (int) amountSpinner.getValue();

        result = new OrderItem();
        result.setMenuItem(menuItem);
        result.setAmount(amount);

        dispose();
    }

    private void cancelButtonClicked(ActionEvent event) {
        dispose();
    }

    public OrderItem getResult() {
        return result;
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
            if (value instanceof MenuItem) {
                MenuItem menuItem = (MenuItem) value;
                value = menuItem.getName() + ", " + menuItem.getPrice() + " руб.";
            }
            return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        }
    }
}