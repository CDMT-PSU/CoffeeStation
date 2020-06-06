package cdmtpsu.coffee.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.util.Objects;

@DatabaseTable(tableName = Ingredient.TABLE_NAME)
public final class Ingredient {
    public static final String TABLE_NAME = "ingredient";
    public static final String NAME_FIELD_NAME = "name";
    public static final String UNIT_FIELD_NAME = "unit";
    public static final String AMOUNT_FIELD_NAME = "amount";
    public static final String WARN_AMOUNT_FIELD_NAME = "warn_amount";

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = NAME_FIELD_NAME)
    private String name;
    @DatabaseField(columnName = UNIT_FIELD_NAME)
    private String unit;
    @DatabaseField(columnName = AMOUNT_FIELD_NAME)
    private int amount;
    @DatabaseField(columnName = WARN_AMOUNT_FIELD_NAME)
    private int warnAmount;

    public Ingredient() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getWarnAmount() {
        return warnAmount;
    }

    public void setWarnAmount(int warnAmount) {
        this.warnAmount = warnAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", unit='" + unit + '\'' +
                ", amount=" + amount +
                ", warnAmount=" + warnAmount +
                '}';
    }
}
