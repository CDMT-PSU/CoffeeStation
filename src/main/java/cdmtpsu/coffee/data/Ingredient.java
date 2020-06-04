package cdmtpsu.coffee.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Objects;

@DatabaseTable(tableName = Ingredient.TABLE_NAME)
public final class Ingredient implements DataObject {
    /* names */
    public static final String TABLE_NAME = "ingredient";
    public static final String NAME_FIELD_NAME = "name";
    public static final String UNIT_FIELD_NAME = "unit";
    /* indices */
    public static final int NAME_FIELD_INDEX = 0;
    public static final int UNIT_FIELD_INDEX = 1;

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = NAME_FIELD_NAME)
    private String name;
    @DatabaseField(columnName = UNIT_FIELD_NAME)
    private String unit;

    public Ingredient() {
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

    @Override
    public Object getValue(int fieldIndex) {
        switch (fieldIndex) {
            case NAME_FIELD_INDEX:
                return name;
            case UNIT_FIELD_INDEX:
                return unit;
            default:
                return null;
        }
    }

    @Override
    public void setValue(int fieldIndex, Object value) {
        switch (fieldIndex) {
            case NAME_FIELD_INDEX:
                name = (String) value;
                break;
            case UNIT_FIELD_INDEX:
                unit = (String) value;
                break;
        }
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
        /*return "Ingredient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", unit='" + unit + '\'' +
                '}';*/
        return name + ", " + unit;
    }
}
