package cdmtpsu.coffee.ui.main;

import cdmtpsu.coffee.Main;
import cdmtpsu.coffee.data.User;
import cdmtpsu.coffee.ui.ingredient.IngredientPanel;
import cdmtpsu.coffee.ui.menuitem.MenuItemPanel;
import cdmtpsu.coffee.ui.order.OrderPanel;
import cdmtpsu.coffee.ui.user.UserPanel;
import cdmtpsu.coffee.ui.welcome.WelcomeFrame;
import cdmtpsu.coffee.util.Refreshable;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public final class MainFrame extends JFrame {
    private final User user;

    private final JTabbedPane tabbedPane;
    private final JPanel contentPane;

    public MainFrame(User user) {
        this.user = user;

        tabbedPane = new JTabbedPane();
        contentPane = new JPanel();

        /* tabbedPane */
        tabbedPane.addTab("Заказы", Main.ORDER_TAB_ICON, new OrderPanel(this));
        tabbedPane.addTab("Меню", Main.MENU_ITEM_TAB_ICON, new MenuItemPanel(this));
        tabbedPane.addTab("Ингредиенты", Main.INGREDIENTS_TAB_ICON, new IngredientPanel(this));
        if (user.getRole() == User.Role.ADMINISTRATOR) { /* Только администратор может видеть вкладку "Пользователи". */
            tabbedPane.addTab("Пользователи", Main.USER_TAB_ICON, new UserPanel(this));
        }
        tabbedPane.addChangeListener(this::tabbedPaneTabChanged);

        /* contentPane */
        contentPane.setPreferredSize(new Dimension(800, 480));
        contentPane.setLayout(new BorderLayout());
        contentPane.add(tabbedPane);

        /* this */
        setTitle("CoffeeStation");
        //setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                int confirmed = showOrderDeletionConfirmationDialog();
                if (confirmed == JOptionPane.YES_OPTION) {
                    dispose();
                    new WelcomeFrame().setVisible(true);
                }
            }
        });
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setContentPane(contentPane);
        setIconImages(Main.ICONS);
        pack();
        setLocationRelativeTo(null);
    }

    private void tabbedPaneTabChanged(ChangeEvent event) {
        ((Refreshable) tabbedPane.getSelectedComponent()).refresh();
    }

    public User getUser() {
        return user;
    }

    private int showOrderDeletionConfirmationDialog() {
        return JOptionPane.showConfirmDialog(this,
                "Завершить сеанс работы?", "Подтверждение",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
    }
}
