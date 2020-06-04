package cdmtpsu.coffee.ui;

import cdmtpsu.coffee.Main;
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
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

public final class MainWindow {
    /* ui components */
    private final JButton addButton;
    private final JButton removeButton;
    private final JToolBar toolBar;
    private final JTabbedPane tabbedPane;
    private final JPanel contentPane;
    private final JFrame frame;

    public MainWindow(User user) {
        /* init components */
        addButton = new JButton();
        removeButton = new JButton();
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

        /* toolBar */
        toolBar.setFloatable(false);
        toolBar.add(addButton);
        toolBar.add(removeButton);

        /* tabbedPane */
        tabbedPane.addChangeListener(this::tabbedPaneTabChanged);
        tabbedPane.addTab("Заказы", new OrderTab(user));
        tabbedPane.addTab("Позиции заказов", new OrderItemTab(user));
        tabbedPane.addTab("Позиции меню", new MenuItemTab(user));
        tabbedPane.addTab("Ингредиенты", new IngredientTab(user));
        tabbedPane.addTab("Рецепты", new RecipeItemTab(user));
        if (user.getRole() == User.Role.ADMINISTRATOR) {
            tabbedPane.addTab("Пользователи", new UserTab());
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
    }

    private void tabbedPaneTabChanged(ChangeEvent event) {
        Tab<?> tab = (Tab<?>) tabbedPane.getSelectedComponent();
        addButton.setEnabled(tab.userCanAdd());
        removeButton.setEnabled(tab.userCanRemove());
        tab.refresh();
    }

    private void addButtonClicked(ActionEvent event) {
        ((Tab<?>) tabbedPane.getSelectedComponent()).add(frame);
    }

    private void removeButtonClicked(ActionEvent event) {
        ((Tab<?>) tabbedPane.getSelectedComponent()).remove(frame);
    }

    public void create() {
        frame.setVisible(true);
    }
}
