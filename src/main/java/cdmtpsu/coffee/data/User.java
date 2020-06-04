package cdmtpsu.coffee.data;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Objects;

@DatabaseTable(tableName = User.TABLE_NAME)
public final class User implements DataObject {
    /* names */
    public static final String TABLE_NAME = "user";
    public static final String USERNAME_FIELD_NAME = "username";
    public static final String NAME_FIELD_NAME = "name";
    public static final String HASH_FIELD_NAME = "hash";
    public static final String ROLE_FIELD_NAME = "role";
    /* indices */
    public static final int USERNAME_FIELD_INDEX = 0;
    public static final int NAME_FIELD_INDEX = 1;
    public static final int HASH_FIELD_INDEX = 2;
    public static final int ROLE_FIELD_INDEX = 3;

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = USERNAME_FIELD_NAME)
    private String username;
    @DatabaseField(columnName = NAME_FIELD_NAME)
    private String name;
    @DatabaseField(columnName = HASH_FIELD_NAME)
    private String hash;
    @DatabaseField(columnName = ROLE_FIELD_NAME, dataType = DataType.ENUM_INTEGER)
    private Role role;

    public User() {
    }

    /* crutch */
    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public Object getValue(int fieldIndex) {
        switch (fieldIndex) {
            case USERNAME_FIELD_INDEX:
                return username;
            case NAME_FIELD_INDEX:
                return name;
            case HASH_FIELD_INDEX:
                return hash;
            case ROLE_FIELD_INDEX:
                return role;
            default:
                return null;
        }
    }

    @Override
    public void setValue(int fieldIndex, Object value) {
        switch (fieldIndex) {
            case USERNAME_FIELD_INDEX:
                username = (String) value;
                break;
            case NAME_FIELD_INDEX:
                name = (String) value;
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

    @Override
    public String toString() {
        /*return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", hash='" + hash + '\'' +
                ", role=" + role +
                '}';*/
        return name;
    }

    public enum Role {
        USER, ADMINISTRATOR
    }
}
