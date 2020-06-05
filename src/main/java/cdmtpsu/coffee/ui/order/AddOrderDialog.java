package cdmtpsu.coffee.ui.order;

import cdmtpsu.coffee.data.Database;
import cdmtpsu.coffee.data.Ingredient;
import cdmtpsu.coffee.data.MenuItem;
import cdmtpsu.coffee.data.Order;
import cdmtpsu.coffee.data.OrderItem;
import cdmtpsu.coffee.data.RecipeItem;
import cdmtpsu.coffee.data.User;
import cdmtpsu.coffee.ui.main.MainFrame;
import cdmtpsu.coffee.ui.orderitem.OrderItemPanel;
import cdmtpsu.coffee.util.CenterLayout;
import cdmtpsu.coffee.util.SwingUtils;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class AddOrderDialog extends JDialog {
    private Result result;

    private final JLabel userLabel;
    private final JComboBox<User> userComboBox;
    private final JPanel userPanel;
    private final JLabel dateLabel;
    private final JTextField dateTextField;
    private final JPanel datePanel;
    private final JPanel fieldPanel;
    private final OrderItemPanel orderItemPanel;
    private final JButton okButton;
    private final JButton cancelButton;
    private final JPanel buttonPanel;
    private final JPanel contentPane;

    public AddOrderDialog(Window owner) {
        super(owner);

        /* Пользователь данной сессии. */
        User sessionUser = ((MainFrame) owner).getUser();

        /* UI */
        userLabel = new JLabel();
        userComboBox = new JComboBox<>();
        userPanel = new JPanel();
        dateLabel = new JLabel();
        dateTextField = new JTextField();
        datePanel = new JPanel();
        fieldPanel = new JPanel();
        orderItemPanel = new OrderItemPanel(owner);
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
        userComboBox.setSelectedItem(sessionUser);
        // todo: set enanbled

        /* userPanel */
        userPanel.setLayout(new CenterLayout());
        userPanel.add(userLabel);
        userPanel.add(userComboBox);

        /* dateLabel */
        dateLabel.setText("Дата");

        /* dateTextField */
        dateTextField.setPreferredSize(new Dimension(200, 24));
        SwingUtils.onValueChanged(dateTextField, this::fieldValueChanged);
        dateTextField.setText(Database.getCurrentDate());
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
        /* Даем изменять пользователя и дату только админу. */
        if (sessionUser.getRole() == User.Role.ADMINISTRATOR) {
            contentPane.add(fieldPanel);
            contentPane.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        contentPane.add(orderItemPanel);
        contentPane.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPane.add(buttonPanel);

        /* this */
        setTitle("Добавить заказ");
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

        Order order = new Order();
        order.setUser(user);
        order.setDate(date);

        ArrayList<OrderItem> orderItems = orderItemPanel.getOrderItems();

        /* Проверяем есть ли у нас достаточное количество ингредиентов для того чтобы приготовить указанные
         * позиции меню в указанном количестве. */

        /* Те ингридиенты, количество которых подошло к концу. */
        ArrayList<Ingredient> overIngredients = new ArrayList<>();
        /* Те ингридиенты, количество которых почти подошло к концу. */
        ArrayList<Ingredient> almostOverIngredients = new ArrayList<>();

        for (OrderItem orderItem : orderItems) {
            MenuItem menuItem = orderItem.getMenuItem();
            try {
                List<RecipeItem> recipeItems = Database.getInstance().getRecipeItems().queryBuilder()
                        .where()
                        .eq(RecipeItem.MENU_ITEM_FIELD_NAME, menuItem.getId())
                        .query();
                for (RecipeItem recipeItem : recipeItems) {
                    Ingredient ingredient = recipeItem.getIngredient();
                    /* Требуемое количество: сколько нужно единиц для приготовления одного * кол-во. */
                    int required = recipeItem.getAmount() * orderItem.getAmount();
                    /* Сколько осталось всего единиц. */
                    int remaining = ingredient.getAmount();
                    if (required > remaining) {
                        overIngredients.add(ingredient);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        /* Выводим сообщение о нехватке некоторых ингредиентов. */
        if (overIngredients.size() > 0) {
            showNotEnoughIngredientsMessageDialog(overIngredients);
            return;
        }

        /* Если хватает, то уменьшаем количество ингредиентов. */
        for (OrderItem orderItem : orderItems) {
            MenuItem menuItem = orderItem.getMenuItem();
            try {
                List<RecipeItem> recipeItems = Database.getInstance().getRecipeItems().queryBuilder()
                        .where()
                        .eq(RecipeItem.MENU_ITEM_FIELD_NAME, menuItem.getId())
                        .query();
                for (RecipeItem recipeItem : recipeItems) {
                    Ingredient ingredient = recipeItem.getIngredient();
                    /* Требуемое количество: сколько нужно единиц для приготовления одного * кол-во. */
                    int required = recipeItem.getAmount() * orderItem.getAmount();
                    /* Сколько осталось всего единиц. */
                    int remaining = ingredient.getAmount();
                    /* Уменьшаем, обновляем в базе. */
                    int left = remaining - required;
                    ingredient.setAmount(left);
                    /* Доабвляем в список "почти закончившихся ингредиентов" если их кол-во меньше указанного порога
                     * (`warn_amount`). */
                    if (left < ingredient.getWarnAmount()) {
                        almostOverIngredients.add(ingredient);
                    }
                    Database.getInstance().getIngredients().update(ingredient);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        /* Выводим сообщение о тех ингредиентах, которые почти закончились. */
        if (almostOverIngredients.size() > 0) {
            int confirmed = showIngredientsAreAlmostOverConfirmationDialog(almostOverIngredients);
            if (confirmed == JOptionPane.YES_OPTION) {
                new SendMessageDialog(getOwner()).setVisible(true);
            }
        }

        for (OrderItem orderItem : orderItems) {
            orderItem.setOrder(order);
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

    private void showNotEnoughIngredientsMessageDialog(ArrayList<Ingredient> ingredients) {
        /* Составляем список ингридиентов, количества которых не хватает. */
        StringBuilder builder = new StringBuilder();
        for (Ingredient ingredient : ingredients) {
            builder.append(" * ");
            builder.append(ingredient.getName());
            builder.append(" - осталось ");
            builder.append(ingredient.getAmount());
            builder.append(" ");
            builder.append(ingredient.getUnit());
            builder.append("\n");
        }
        JOptionPane.showMessageDialog(getOwner(),
                "Недостаточно ингредиентов для приготовления:\n" + builder.toString(), "Внимание",
                JOptionPane.WARNING_MESSAGE);
    }

    private int showIngredientsAreAlmostOverConfirmationDialog(ArrayList<Ingredient> ingredients) {
        /* Составляем список ингридиентов, которые почти иссякли. */
        StringBuilder builder = new StringBuilder();
        for (Ingredient ingredient : ingredients) {
            builder.append(" * ");
            builder.append(ingredient.getName());
            builder.append(" - осталось ");
            builder.append(ingredient.getAmount());
            builder.append(" ");
            builder.append(ingredient.getUnit());
            builder.append("\n");
        }
        return JOptionPane.showConfirmDialog(getOwner(),
                "Некоторые из ингридиентов почти закончились:\n" +
                        builder.toString() +
                        "\n" +
                        "Сообщить администратору?", "Внимание",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
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