package cdmtpsu.coffee.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.util.Objects;

@DatabaseTable(tableName = RecipeItem.TABLE_NAME)
public final class RecipeItem {
    public static final String TABLE_NAME = "recipe_item";
    public static final String MENU_ITEM_FIELD_NAME = "menu_item_id";
    public static final String INGREDIENT_FIELD_NAME = "ingredient_id";
    public static final String AMOUNT_FIELD_NAME = "amount";

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = MENU_ITEM_FIELD_NAME, foreign = true, foreignAutoRefresh = true,
            columnDefinition = "INTEGER CONSTRAINT `menu_item_id` REFERENCES `menu_item`(id) ON DELETE CASCADE")
    private MenuItem menuItem;
    @DatabaseField(columnName = INGREDIENT_FIELD_NAME, foreign = true, foreignAutoRefresh = true,
            columnDefinition = "INTEGER CONSTRAINT `ingredient_id` REFERENCES `ingredient`(id) ON DELETE CASCADE")
    private Ingredient ingredient;
    @DatabaseField(columnName = AMOUNT_FIELD_NAME)
    private int amount;

    public RecipeItem() {
    }

    public int getId() {
        return id;
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

    @Override
    public String toString() {
        return "RecipeItem{" +
                "id=" + id +
                ", menuItem=" + menuItem +
                ", ingredient=" + ingredient +
                ", amount=" + amount +
                '}';
    }
}
