package cdmtpsu.coffee.ui.tabs;

import cdmtpsu.coffee.data.Database;
import cdmtpsu.coffee.data.Ingredient;
import cdmtpsu.coffee.data.User;
import cdmtpsu.coffee.util.TextFieldCellEditor;

import java.awt.Window;

public final class IngredientTab extends Tab<Ingredient> {
    public IngredientTab(User user) {
        super(
                new String[]{
                        "Название",
                        "Единицы измерения"
                },
                new Class[]{
                        String.class,
                        String.class
                },
                Database.getInstance().getIngredients(),
                user.getRole() == User.Role.ADMINISTRATOR,
                user.getRole() == User.Role.ADMINISTRATOR,
                user.getRole() == User.Role.ADMINISTRATOR);
        setCellEditor("Название", new TextFieldCellEditor("[\\s\\S]+"));
        setCellEditor("Единицы измерения", new TextFieldCellEditor("[\\s\\S]+"));
    }

    @Override
    public Ingredient createItem(Window owner) {
        Ingredient ingredient = new Ingredient();
        ingredient.setName("Введите название");
        ingredient.setUnit("Введите единицы измерения");
        return ingredient;
    }
}
