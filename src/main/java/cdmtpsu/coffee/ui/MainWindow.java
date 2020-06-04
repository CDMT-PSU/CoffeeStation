package cdmtpsu.coffee.ui;

import cdmtpsu.coffee.Main;
import cdmtpsu.coffee.data.Database;
import cdmtpsu.coffee.data.Order;
import cdmtpsu.coffee.data.User;
import cdmtpsu.coffee.ui.tabs.IngredientTab;
import cdmtpsu.coffee.ui.tabs.MenuItemTab;
import cdmtpsu.coffee.ui.tabs.OrderItemTab;
import cdmtpsu.coffee.ui.tabs.OrderTab;
import cdmtpsu.coffee.ui.tabs.RecipeItemTab;
import cdmtpsu.coffee.ui.tabs.Tab;
import cdmtpsu.coffee.ui.tabs.UserTab;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public final class MainWindow {
    /* ui components */
    private final JButton addButton;
    private final JButton removeButton;
    private final JButton saveReceiptButton;
    private final JToolBar toolBar;
    private final JTabbedPane tabbedPane;
    private final JPanel contentPane;
    private final JFrame frame;

    private final User user;

    public MainWindow(User user) {
        /* init components */
        addButton = new JButton();
        removeButton = new JButton();
        saveReceiptButton = new JButton();
        toolBar = new JToolBar();
        tabbedPane = new JTabbedPane();
        contentPane = new JPanel();
        frame = new JFrame();

        /* addButton */
        addButton.setText("Добавить");
        addButton.addActionListener(this::addButtonClicked);

        /* removeButton */
        removeButton.setText("Удалить");
        removeButton.addActionListener(this::removeButtonClicked);

        /* saveReceiptButton */
        saveReceiptButton.setText("Сохранить квитанцию");
        saveReceiptButton.addActionListener(this::saveReceiptButtonClicked);

        /* toolBar */
        toolBar.setFloatable(false);
        toolBar.add(addButton);
        toolBar.add(removeButton);
        toolBar.add(saveReceiptButton);

        /* tabbedPane */
        tabbedPane.addChangeListener(this::tabbedPaneTabChanged);
        tabbedPane.addTab("Заказы", Main.ORDER_TAB_ICON, new OrderTab(user));
        tabbedPane.addTab("Позиции заказов", Main.ORDER_ITEM_TAB_ICON, new OrderItemTab(user));
        tabbedPane.addTab("Позиции меню", Main.MENU_ITEM_TAB_ICON, new MenuItemTab(user));
        tabbedPane.addTab("Ингредиенты", Main.INGREDIENTS_TAB_ICON, new IngredientTab(user));
        tabbedPane.addTab("Позиции рецептов", Main.RECIPE_ITEM_TAB_ICON, new RecipeItemTab(user));
        if (user.getRole() == User.Role.ADMINISTRATOR) {
            tabbedPane.addTab("Пользователи", Main.USER_TAB_ICON, new UserTab());
        }

        /* contentPane */
        contentPane.setPreferredSize(new Dimension(800, 480));
        contentPane.setLayout(new BorderLayout());
        contentPane.add(toolBar, BorderLayout.NORTH);
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        /* frame */
        frame.setTitle("CoffeeStation");
        frame.setIconImages(Main.ICONS);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setContentPane(contentPane);
        frame.pack();
        frame.setLocationRelativeTo(null);

        this.user = user;
    }

    private void tabbedPaneTabChanged(ChangeEvent event) {
        Tab<?> tab = (Tab<?>) tabbedPane.getSelectedComponent();
        addButton.setEnabled(tab.userCanAdd());
        removeButton.setEnabled(tab.userCanRemove());
        tab.refresh();
        saveReceiptButton.setVisible(tab instanceof OrderTab);/* aaaa */
    }

    private void addButtonClicked(ActionEvent event) {
        ((Tab<?>) tabbedPane.getSelectedComponent()).add(frame);
    }

    private void removeButtonClicked(ActionEvent event) {
        Tab<?> tab = (Tab<?>) tabbedPane.getSelectedComponent();

        /* cringe */
        if (tab.getSelectedItems().contains(user)) {
            JOptionPane.showMessageDialog(frame,
                    "Нельзя удалить самого себя",
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        tab.remove(frame);
    }

    private void saveReceiptButtonClicked(ActionEvent event) {
        Tab<?> tab = (Tab<?>) tabbedPane.getSelectedComponent();
        if (tab.getSelectedItems().size() > 0) {
            Order order = (Order) tab.getSelectedItems().get(0);

            String fileName = order.toString() + ".txt";

            File file = new File(fileName);

            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)))) {
                writer.write(Database.getInstance().getReceiptText(order));
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                Desktop.getDesktop().open(file);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            JOptionPane.showMessageDialog(frame,
                    "Выберите заказ",
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void create() {
        frame.setVisible(true);
    }
}
