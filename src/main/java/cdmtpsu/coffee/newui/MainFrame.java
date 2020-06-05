package cdmtpsu.coffee.newui;

import cdmtpsu.coffee.newui.ingredient.IngredientPanel;
import cdmtpsu.coffee.newui.menuitem.MenuItemPanel;
import cdmtpsu.coffee.newui.user.UserPanel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import java.awt.Dimension;

public final class MainFrame extends JFrame {
    private final JTabbedPane tabbedPane;
    private final JPanel contentPane;

    public MainFrame() {
        tabbedPane = new JTabbedPane();
        contentPane = new JPanel();

        /* tabbedPane */
        tabbedPane.addTab("Меню", new MenuItemPanel(this));
        tabbedPane.addTab("Ингредиенты", new IngredientPanel(this));
        tabbedPane.addTab("Пользователи", new UserPanel(this));

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

}
