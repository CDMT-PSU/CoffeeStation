package cdmtpsu.coffee.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Objects;

@DatabaseTable(tableName = Order.TABLE_NAME)
public final class Order implements DataObject {
    /* names */
    public static final String TABLE_NAME = "order";
    public static final String USER_FIELD_NAME = "user_id";
    public static final String DATE_FIELD_NAME = "date";
    /* indices */
    public static final int ID_FIELD_INDEX = 0;
    public static final int USER_FIELD_INDEX = 1;
    public static final int DATE_FIELD_INDEX = 2;

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = USER_FIELD_NAME, foreign = true, foreignAutoRefresh = true,
            columnDefinition = "INTEGER CONSTRAINT `user_id` REFERENCES `user`(id) ON DELETE CASCADE")
    private User user;
    @DatabaseField(columnName = DATE_FIELD_NAME)
    private String date;

    public Order() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public Object getValue(int fieldIndex) {
        switch (fieldIndex) {
            case ID_FIELD_INDEX:
                return id;
            case USER_FIELD_INDEX:
                return user;
            case DATE_FIELD_INDEX:
                return date;
            default:
                return null;
        }
    }

    @Override
    public void setValue(int fieldIndex, Object value) {
        switch (fieldIndex) {
            case ID_FIELD_INDEX:
                id = (int) value;
            case USER_FIELD_INDEX:
                user = (User) value;
                break;
            case DATE_FIELD_INDEX:
                date = (String) value;
                break;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order that = (Order) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        /*return "Order{" +
                "id=" + id +
                ", user=" + user +
                ", date='" + date + '\'' +
                '}';*/
        return id + ", " + user + ", " + date;
    }
}
