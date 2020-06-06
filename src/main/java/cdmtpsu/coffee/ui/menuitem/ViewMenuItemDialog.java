package cdmtpsu.coffee.ui.menuitem;

import cdmtpsu.coffee.data.Database;
import cdmtpsu.coffee.data.MenuItem;
import cdmtpsu.coffee.data.RecipeItem;
import cdmtpsu.coffee.ui.recipeitem.ReadOnlyRecipeItemPanel;
import cdmtpsu.coffee.ui.recipeitem.RecipeItemPanel;
import cdmtpsu.coffee.util.CenterLayout;
import cdmtpsu.coffee.util.SwingUtils;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import java.awt.Dimension;
import java.awt.Menu;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.ArrayList;

public final class ViewMenuItemDialog extends JDialog {
    private final ReadOnlyRecipeItemPanel recipeItemPanel;
    private final JPanel contentPane;

    public ViewMenuItemDialog(Window owner, MenuItem menuItem) {
        super(owner);

        /* UI */
        recipeItemPanel = new ReadOnlyRecipeItemPanel(owner);
        contentPane = new JPanel();

        /* recipeItemPanel */
        recipeItemPanel.setPreferredSize(new Dimension(500, 300));
        try {
            Database.getInstance().getRecipeItems().queryBuilder()
                    .where()
                    .eq(RecipeItem.MENU_ITEM_FIELD_NAME, menuItem.getId())
                    .query()
                    .forEach(it -> recipeItemPanel.getRecipeItems().add(it));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        recipeItemPanel.refresh();

        /* contentPane */
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(new CenterLayout());
        contentPane.add(recipeItemPanel);

        /* this */
        setTitle("Просмотреть рецепт");
        setContentPane(contentPane);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setModal(true);
        pack();
        setLocationRelativeTo(owner);
    }
}