package cdmtpsu.coffee.ui.tabs;

import cdmtpsu.coffee.data.Database;
import cdmtpsu.coffee.data.Ingredient;
import cdmtpsu.coffee.data.MenuItem;
import cdmtpsu.coffee.data.RecipeItem;
import cdmtpsu.coffee.data.User;
import cdmtpsu.coffee.util.DaoCellEditor;
import cdmtpsu.coffee.util.SpinnerCellEditor;

import javax.swing.JOptionPane;
import java.awt.Window;
import java.sql.SQLException;

public final class RecipeItemTab extends Tab<RecipeItem> {
    public RecipeItemTab(User user) {
        super(
                new String[]{
                        "Позиция меню",
                        "Ингредиент",
                        "Количество"
                },
                new Class[]{
                        MenuItem.class,
                        Ingredient.class,
                        Integer.class
                },
                Database.getInstance().getRecipeItems(),
                user.getRole() == User.Role.ADMINISTRATOR,
                user.getRole() == User.Role.ADMINISTRATOR,
                user.getRole() == User.Role.ADMINISTRATOR);
        setCellEditor("Позиция меню", new DaoCellEditor<>(Database.getInstance().getMenuItems()));
        setCellEditor("Ингредиент", new DaoCellEditor<>(Database.getInstance().getIngredients()));
        setCellEditor("Количество", new SpinnerCellEditor(1, 999999));
    }

    @Override
    public RecipeItem createItem(Window owner) {
        MenuItem menuItem = null;
        Ingredient ingredient = null;
        try {
            menuItem = Database.getInstance().getMenuItems().queryBuilder().queryForFirst();
            ingredient = Database.getInstance().getIngredients().queryBuilder().queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (menuItem == null) {
            JOptionPane.showMessageDialog(
                    owner,
                    "Нет доступных позиций меню",
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE
            );
            return null;
        }

        if (ingredient == null) {
            JOptionPane.showMessageDialog(
                    owner,
                    "Нет доступных ингредиентов",
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE
            );
            return null;
        }

        RecipeItem recipeItem = new RecipeItem();
        recipeItem.setMenuItem(menuItem);
        recipeItem.setIngredient(ingredient);
        recipeItem.setAmount(1);

        return recipeItem;
    }
}
