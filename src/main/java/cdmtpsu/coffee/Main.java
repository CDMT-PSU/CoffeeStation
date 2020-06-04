package cdmtpsu.coffee;

import cdmtpsu.coffee.data.Database;
import cdmtpsu.coffee.data.User;
import cdmtpsu.coffee.ui.MainWindow;

import javax.imageio.ImageIO;
import javax.swing.UIManager;
import java.awt.Image;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class Main {
    public static final ArrayList<Image> ICONS;

    static {
        ICONS = new ArrayList<>();
        try {
            ICONS.add(ImageIO.read(Main.class.getResource("/x16.png")));
            ICONS.add(ImageIO.read(Main.class.getResource("/x24.png")));
            ICONS.add(ImageIO.read(Main.class.getResource("/x32.png")));
            ICONS.add(ImageIO.read(Main.class.getResource("/x48.png")));
            ICONS.add(ImageIO.read(Main.class.getResource("/x64.png")));
            ICONS.add(ImageIO.read(Main.class.getResource("/x256.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

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
