package cdmtpsu.coffee.ui.main;

import cdmtpsu.coffee.data.User;
import cdmtpsu.coffee.ui.ingredient.IngredientPanel;
import cdmtpsu.coffee.ui.menuitem.MenuItemPanel;
import cdmtpsu.coffee.ui.order.OrderPanel;
import cdmtpsu.coffee.ui.user.UserPanel;
import cdmtpsu.coffee.util.Refreshable;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;

public final class MainFrame extends JFrame {
    private final User user;

    private final JTabbedPane tabbedPane;
    private final JPanel contentPane;

    public MainFrame(User user) {
        this.user = user;

        tabbedPane = new JTabbedPane();
        contentPane = new JPanel();

        /* tabbedPane */
        tabbedPane.addTab("Заказы", new OrderPanel(this));
        tabbedPane.addTab("Меню", new MenuItemPanel(this));
        tabbedPane.addTab("Ингредиенты", new IngredientPanel(this));
        if (user.getRole() == User.Role.ADMINISTRATOR) { /* Только администратор может видеть вкладку "Пользователи". */
            tabbedPane.addTab("Пользователи", new UserPanel(this));
        }
        tabbedPane.addChangeListener(this::tabbedPaneTabChanged);

        /* contentPane */
        contentPane.setPreferredSize(new Dimension(800, 480));
        contentPane.setLayout(new BorderLayout());
        contentPane.add(tabbedPane);

        /* this */
        setTitle("CoffeeStation");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setContentPane(contentPane);
        pack();
        setLocationRelativeTo(null);
    }

    private void tabbedPaneTabChanged(ChangeEvent event) {
        ((Refreshable) tabbedPane.getSelectedComponent()).refresh();
    }

    public User getUser() {
        return user;
    }
}
