package cdmtpsu.coffee.data;

import com.j256.ormlite.jdbc.JdbcConnectionSource;

import java.sql.SQLException;

public final class Database {
    private static final JdbcConnectionSource connectionSource;

    static {
        try {
            connectionSource = new JdbcConnectionSource("jdbc:sqlite:coffee.db");
        } catch (SQLException e) {
            throw new RuntimeException("unable to access database", e);
        }
    }

}
