package cdmtpsu.coffee.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.util.Objects;

@DatabaseTable(tableName = Order.TABLE_NAME)
public final class Order {
    public static final String TABLE_NAME = "order";
    public static final String USER_FIELD_NAME = "user_id";
    public static final String DATE_FIELD_NAME = "date";

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = USER_FIELD_NAME, foreign = true, foreignAutoRefresh = true,
            columnDefinition = "INTEGER CONSTRAINT `user_id` REFERENCES `user`(id) ON DELETE CASCADE")
    private User user;
    @DatabaseField(columnName = DATE_FIELD_NAME)
    private String date;

    public Order() {
    }

    public int getId() {
        return id;
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
        return "Order{" +
                "id=" + id +
                ", user=" + user +
                ", date='" + date + '\'' +
                '}';
    }
}
