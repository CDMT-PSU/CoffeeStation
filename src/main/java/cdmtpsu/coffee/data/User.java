package cdmtpsu.coffee.data;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Objects;

@DatabaseTable(tableName = User.TABLE_NAME)
public final class User {
    public static final String USERNAME_PATTERN = "^[A-Za-z0-9_]{4,15}$";
    public static final String PASSWORD_PATTERN = "[\\s\\S]{4,15}$";
    public static final String NAME_PATTERN = "(?:\\S+\\s){2}\\S+";

    public static final String TABLE_NAME = "user";
    public static final String USERNAME_FIELD_NAME = "username";
    public static final String HASH_FIELD_NAME = "hash";
    public static final String NAME_FIELD_NAME = "name";
    public static final String ROLE_FIELD_NAME = "role";

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = USERNAME_FIELD_NAME)
    private String username;
    @DatabaseField(columnName = HASH_FIELD_NAME)
    private String hash;
    @DatabaseField(columnName = NAME_FIELD_NAME)
    private String name;
    @DatabaseField(columnName = ROLE_FIELD_NAME, dataType = DataType.ENUM_INTEGER)
    private Role role;

    public User() {
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
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
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", hash='" + hash + '\'' +
                ", name='" + name + '\'' +
                ", role=" + role +
                '}';
    }

    public enum Role {
        USER, ADMINISTRATOR
    }
}
