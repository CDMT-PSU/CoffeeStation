package cdmtpsu.coffee;

import cdmtpsu.coffee.data.Database;
import cdmtpsu.coffee.ui.welcome.WelcomeFrame;
import com.alee.laf.WebLookAndFeel;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static final ArrayList<Image> ICONS;

    public static final ImageIcon INGREDIENTS_TAB_ICON;
    public static final ImageIcon MENU_ITEM_TAB_ICON;
    public static final ImageIcon ORDER_TAB_ICON;
    public static final ImageIcon USER_TAB_ICON;

    static {
        ICONS = new ArrayList<>();
        try {
            ICONS.add(ImageIO.read(Main.class.getResource("/icons/frame/x16.png")));
            ICONS.add(ImageIO.read(Main.class.getResource("/icons/frame/x24.png")));
            ICONS.add(ImageIO.read(Main.class.getResource("/icons/frame/x32.png")));
            ICONS.add(ImageIO.read(Main.class.getResource("/icons/frame/x48.png")));
            ICONS.add(ImageIO.read(Main.class.getResource("/icons/frame/x64.png")));
            ICONS.add(ImageIO.read(Main.class.getResource("/icons/frame/x256.png")));

            INGREDIENTS_TAB_ICON = new ImageIcon(ImageIO.read(Main.class.getResource("/icons/tab/ingredients_tab.png")));
            MENU_ITEM_TAB_ICON = new ImageIcon(ImageIO.read(Main.class.getResource("/icons/tab/menu_item_tab.png")));
            ORDER_TAB_ICON = new ImageIcon(ImageIO.read(Main.class.getResource("/icons/tab/order_tab.png")));
            USER_TAB_ICON = new ImageIcon(ImageIO.read(Main.class.getResource("/icons/tab/user_tab.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        UIManager.put("OptionPane.yesButtonText", "Да");
        UIManager.put("OptionPane.noButtonText", "Нет");
        UIManager.put("OptionPane.okButtonText", "ОК");
        UIManager.put("OptionPane.cancelButtonText", "Отмена");
        try {
            WebLookAndFeel.install();
        } catch (Exception ignored) {
        }
        Database.getInstance(); // init
        new WelcomeFrame().setVisible(true);
    }
}
