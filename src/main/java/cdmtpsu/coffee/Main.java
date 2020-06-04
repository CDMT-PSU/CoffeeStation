package cdmtpsu.coffee;

import cdmtpsu.coffee.data.Database;
import cdmtpsu.coffee.ui.StartupWindow;

import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
        Database.dummy();
        new StartupWindow().create();
    }
}
