package cdmtpsu.coffee.data;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Objects;

public final class Database {
    private static final JdbcConnectionSource connectionSource;
    /* dao */
    private static final Dao<Ingredient, Integer> ingredients;
    private static final Dao<MenuItem, Integer> menuItems;
    private static final Dao<Order, Integer> orders;
    private static final Dao<OrderItem, Integer> orderItems;
    private static final Dao<RecipeItem, Integer> recipeItems;
    private static final Dao<User, Integer> users;

    static {
        try {
            connectionSource = new JdbcConnectionSource("jdbc:sqlite:coffee.db");
            /* create tables if they are not exists */
            TableUtils.createTableIfNotExists(connectionSource, Ingredient.class);
            TableUtils.createTableIfNotExists(connectionSource, MenuItem.class);
            TableUtils.createTableIfNotExists(connectionSource, Order.class);
            TableUtils.createTableIfNotExists(connectionSource, OrderItem.class);
            TableUtils.createTableIfNotExists(connectionSource, RecipeItem.class);
            TableUtils.createTableIfNotExists(connectionSource, User.class);
            /* init dao */
            ingredients = DaoManager.createDao(connectionSource, Ingredient.class);
            menuItems = DaoManager.createDao(connectionSource, MenuItem.class);
            orders = DaoManager.createDao(connectionSource, Order.class);
            orderItems = DaoManager.createDao(connectionSource, OrderItem.class);
            recipeItems = DaoManager.createDao(connectionSource, RecipeItem.class);
            users = DaoManager.createDao(connectionSource, User.class);
            /* crutch */
            users.executeRawNoArgs("PRAGMA foreign_keys = ON");
            /* add admin if it doesn't exist */
            if (!usernameExist("admin")) {
                User user = new User();
                user.setUsername("admin");
                user.setHash(Database.hashPassword("admin"));
                user.setRole(User.Role.ADMINISTRATOR);
                users.create(user);
            }

        } catch (SQLException e) {
            throw new RuntimeException("unable to access database", e);
        }
    }

    public static void dummy() {
    }

    public static Dao<Ingredient, Integer> getIngredients() {
        return ingredients;
    }

    public static Dao<MenuItem, Integer> getMenuItems() {
        return menuItems;
    }

    public static Dao<Order, Integer> getOrders() {
        return orders;
    }

    public static Dao<OrderItem, Integer> getOrderItems() {
        return orderItems;
    }

    public static Dao<RecipeItem, Integer> getRecipeItems() {
        return recipeItems;
    }

    public static Dao<User, Integer> getUsers() {
        return users;
    }

    public static boolean usernameExist(String username) {
        try {
            return getUsers().queryBuilder().where().eq(User.USERNAME_FIELD_NAME, username).countOf() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isValidUsername(String username) {
        return username.matches("^[A-Za-z0-9_]{4,15}$");
    }

    public static boolean isValidPassword(String password) {
        return password.matches("[\\s\\S]{4,15}$");
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

    public static boolean authenticate(User user, String password) {
        String hash = user.getHash();
        var saltAndHash = Base64.getDecoder().decode(hash);
        var salt = new byte[16];
        System.arraycopy(saltAndHash, 0, salt, 0, 16);
        return Objects.equals(hashPassword(salt, password), hash);
    }

    private Database() {
    }
}
