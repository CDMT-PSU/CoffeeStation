package cdmtpsu.coffee.data;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Objects;

@DatabaseTable(tableName = User.TABLE_NAME)
public final class User {
    /* names */
    public static final String TABLE_NAME = "user";
    public static final String USERNAME_FIELD_NAME = "username";
    public static final String HASH_FIELD_NAME = "hash";
    public static final String ROLE_FIELD_NAME = "role";
    /* indices */
    private static final int USERNAME_FIELD_INDEX = 0;
    private static final int HASH_FIELD_INDEX = 1;
    private static final int ROLE_FIELD_INDEX = 2;

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = USERNAME_FIELD_NAME)
    private String username;
    @DatabaseField(columnName = HASH_FIELD_NAME)
    private String hash;
    @DatabaseField(columnName = ROLE_FIELD_NAME, dataType = DataType.ENUM_INTEGER)
    private Role role;

    public User() {
    }

    public Object getValue(int fieldIndex) {
        switch (fieldIndex) {
            case USERNAME_FIELD_INDEX:
                return username;
            case HASH_FIELD_INDEX:
                return hash;
            case ROLE_FIELD_INDEX:
                return role;
            default:
                return null;
        }
    }

    public void setValue(int fieldIndex, Object value) {
        switch (fieldIndex) {
            case USERNAME_FIELD_INDEX:
                username = (String) value;
                break;
            case HASH_FIELD_INDEX:
                hash = Database.hashPassword((String) value);
                break;
            case ROLE_FIELD_INDEX:
                role = (Role) value;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User that = (User) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    private enum Role {
        USER, ADMINISTRATOR
    }
}
