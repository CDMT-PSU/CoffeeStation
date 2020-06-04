package cdmtpsu.coffee.data;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.Base64;

public final class Database {
    private static final JdbcConnectionSource connectionSource;
    private static final Dao<User, Integer> users;
    private static final Dao<Order, Integer> orders;
    private static final Dao<MenuItem, Integer> menuItems;
    private static final Dao<Ingredient, Integer> ingredients;

    static {
        try {
            connectionSource = new JdbcConnectionSource("jdbc:sqlite:coffee.db");

            users = DaoManager.createDao(connectionSource, User.class);
            orders = DaoManager.createDao(connectionSource, Order.class);
            menuItems = DaoManager.createDao(connectionSource, MenuItem.class);
            ingredients = DaoManager.createDao(connectionSource, Ingredient.class);

            /* crutch */
            users.executeRawNoArgs("PRAGMA foreign_keys = ON");
        } catch (SQLException e) {
            throw new RuntimeException("unable to access database", e);
        }
    }

    public static Dao<User, Integer> getUsers() {
        return users;
    }

    public static String hashPassword(String password) {
        var salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return hashPassword(salt, password);
    }

    public static String hashPassword(byte[] salt, String string) {
        try {
            var digest = MessageDigest.getInstance("SHA-512");
            digest.update(salt);
            var hash = digest.digest(string.getBytes(StandardCharsets.UTF_8));
            var saltAndHash = new byte[80];
            System.arraycopy(salt, 0, saltAndHash, 0, 16);
            System.arraycopy(hash, 0, saltAndHash, 16, 64);
            return Base64.getEncoder().encodeToString(saltAndHash);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    private Database() {
    }
}
