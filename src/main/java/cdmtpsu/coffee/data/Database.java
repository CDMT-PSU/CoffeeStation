package cdmtpsu.coffee.data;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.table.TableUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

public final class Database {
    public static final String USERNAME_PATTERN = "^[A-Za-z0-9_]{4,15}$";
    public static final String PASSWORD_PATTERN = "[\\s\\S]{4,15}$";

    private static Database instance;

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public static boolean isValidUsername(String username) {
        return username.matches(USERNAME_PATTERN);
    }

    public static boolean isValidPassword(String password) {
        return password.matches(PASSWORD_PATTERN);
    }

    public static String hashPassword(String password) {
        var salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return hashPassword(salt, password);
    }

    public static String hashPassword(byte[] salt, String string) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            digest.update(salt);
            byte[] hash = digest.digest(string.getBytes(StandardCharsets.UTF_8));
            byte[] saltAndHash = new byte[80];
            System.arraycopy(salt, 0, saltAndHash, 0, 16);
            System.arraycopy(hash, 0, saltAndHash, 16, 64);
            return Base64.getEncoder().encodeToString(saltAndHash);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    public static boolean authenticate(User user, String password) {
        String hash = user.getHash();
        byte[] saltAndHash = Base64.getDecoder().decode(hash);
        byte[] salt = new byte[16];
        System.arraycopy(saltAndHash, 0, salt, 0, 16);
        return Objects.equals(hashPassword(salt, password), hash);
    }

    private final JdbcConnectionSource connectionSource;
    /* dao */
    private final Dao<Ingredient, Integer> ingredients;
    private final Dao<MenuItem, Integer> menuItems;
    private final Dao<Order, Integer> orders;
    private final Dao<OrderItem, Integer> orderItems;
    private final Dao<RecipeItem, Integer> recipeItems;
    private final Dao<User, Integer> users;

    private Database() {
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
                user.setHash(Database.hashPassword("1234"));
                user.setRole(User.Role.ADMINISTRATOR);
                users.create(user);
            }

        } catch (SQLException e) {
            throw new RuntimeException("unable to access database", e);
        }
    }

    public Dao<Ingredient, Integer> getIngredients() {
        return ingredients;
    }

    public Dao<MenuItem, Integer> getMenuItems() {
        return menuItems;
    }

    public Dao<Order, Integer> getOrders() {
        return orders;
    }

    public Dao<OrderItem, Integer> getOrderItems() {
        return orderItems;
    }

    public Dao<RecipeItem, Integer> getRecipeItems() {
        return recipeItems;
    }

    public Dao<User, Integer> getUsers() {
        return users;
    }

    public boolean usernameExist(String username) {
        try {
            return getUsers().queryBuilder().where().eq(User.USERNAME_FIELD_NAME, username).countOf() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getReceiptText(Order order) {
        StringBuilder builder = new StringBuilder();
        builder.append("==============================");
        builder.append(System.lineSeparator());
        builder.append("ФИО кассира: ").append(order.getUser().getName());
        builder.append(System.lineSeparator());
        builder.append("Дата: ").append(order.getDate());
        builder.append(System.lineSeparator());
        builder.append("==============================");
        builder.append(System.lineSeparator());
        try {
            List<OrderItem> list = orderItems.queryBuilder()
                    .where().eq(OrderItem.ORDER_FIELD_NAME, order.getId())
                    .query();
            int sum = 0;
            for (OrderItem orderItem : list) {
                MenuItem menuItem = orderItem.getMenuItem();
                int value = menuItem.getPrice() * orderItem.getAmount();
                builder.append(menuItem.getName())
                        .append(", ")
                        .append(menuItem.getPrice())
                        .append(" руб. ").append("* ")
                        .append(orderItem.getAmount()).append(" = ")
                        .append(value)
                        .append(" руб.");
                builder.append(System.lineSeparator());
                sum += value;
            }
            builder.append("==============================");
            builder.append(System.lineSeparator());
            builder.append("Итого: ").append(sum).append(" руб.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }
}
