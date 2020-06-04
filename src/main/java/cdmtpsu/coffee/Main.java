package cdmtpsu.coffee;

import cdmtpsu.coffee.data.Database;
import cdmtpsu.coffee.data.User;
import cdmtpsu.coffee.ui.MainWindow;
import cdmtpsu.coffee.ui.StartupWindow;

import javax.swing.UIManager;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
        Database.getInstance(); // init
        //new StartupWindow().create();

        try {
            User user = Database.getInstance().getUsers().queryBuilder()
                    .where().eq(User.USERNAME_FIELD_NAME, "user")
                    .queryForFirst();
            new MainWindow(user).create();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
