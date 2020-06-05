package cdmtpsu.coffee.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Objects;

@DatabaseTable(tableName = MenuItem.TABLE_NAME)
public final class MenuItem implements DataObject {
    /* names */
    public static final String TABLE_NAME = "menu_item";
    public static final String NAME_FIELD_NAME = "name";
    public static final String PRICE_FIELD_NAME = "price";
    /* indices */
    public static final int NAME_FIELD_INDEX = 0;
    public static final int PRICE_FIELD_INDEX = 1;

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = NAME_FIELD_NAME)
    private String name;
    @DatabaseField(columnName = PRICE_FIELD_NAME)
    private int price;

    public MenuItem() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public Object getValue(int fieldIndex) {
        switch (fieldIndex) {
            case NAME_FIELD_INDEX:
                return name;
            case PRICE_FIELD_INDEX:
                return price;
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
            case PRICE_FIELD_INDEX:
                price = (int) value;
                break;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuItem that = (MenuItem) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        /*return "MenuItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';*/
        return name;
    }
}