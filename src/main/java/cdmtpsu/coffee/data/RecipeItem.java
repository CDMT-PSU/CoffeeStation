package cdmtpsu.coffee.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Objects;

@DatabaseTable(tableName = RecipeItem.TABLE_NAME)
public final class RecipeItem {
    /* names */
    public static final String TABLE_NAME = "recipe_item";
    public static final String MENU_ITEM_FIELD_NAME = "menu_item_id";
    public static final String INGREDIENT_FIELD_NAME = "ingredient_id";
    public static final String AMOUNT_FIELD_NAME = "amount";
    /* indices */
    public static final int MENU_ITEM_FIELD_INDEX = 0;
    public static final int INGREDIENT_FIELD_INDEX = 1;
    public static final int AMOUNT_FIELD_INDEX = 2;

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = MENU_ITEM_FIELD_NAME, foreign = true, foreignAutoRefresh = true)
    private MenuItem menuItem;
    @DatabaseField(columnName = INGREDIENT_FIELD_NAME, foreign = true, foreignAutoRefresh = true)
    private Ingredient ingredient;
    @DatabaseField(columnName = AMOUNT_FIELD_NAME)
    private int amount;

    public RecipeItem() {
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Object getValue(int fieldIndex) {
        switch (fieldIndex) {
            case MENU_ITEM_FIELD_INDEX:
                return menuItem;
            case INGREDIENT_FIELD_INDEX:
                return ingredient;
            case AMOUNT_FIELD_INDEX:
                return amount;
            default:
                return null;
        }
    }

    public void setValue(int fieldIndex, Object value) {
        switch (fieldIndex) {
            case MENU_ITEM_FIELD_INDEX:
                menuItem = (MenuItem) value;
                break;
            case INGREDIENT_FIELD_INDEX:
                ingredient = (Ingredient) value;
            case AMOUNT_FIELD_INDEX:
                amount = (int) value;
                break;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeItem that = (RecipeItem) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
