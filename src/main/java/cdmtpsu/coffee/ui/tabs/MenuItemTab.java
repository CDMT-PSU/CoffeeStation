package cdmtpsu.coffee.ui.tabs;

import cdmtpsu.coffee.data.Database;
import cdmtpsu.coffee.data.MenuItem;
import cdmtpsu.coffee.data.User;
import cdmtpsu.coffee.util.SpinnerCellEditor;
import cdmtpsu.coffee.util.TextFieldCellEditor;

import java.awt.Window;

public final class MenuItemTab extends Tab<MenuItem> {
    public MenuItemTab(User user) {
        super(
                new String[]{
                        "Название",
                        "Цена"
                },
                new Class[]{
                        String.class,
                        Integer.class
                },
                Database.getInstance().getMenuItems(),
                user.getRole() == User.Role.ADMINISTRATOR,
                user.getRole() == User.Role.ADMINISTRATOR,
                user.getRole() == User.Role.ADMINISTRATOR);
        setCellEditor("Название", new TextFieldCellEditor("[\\s\\S]+"));
        setCellEditor("Цена", new SpinnerCellEditor(0, 999999));
    }

    @Override
    public MenuItem createItem(Window owner) {
        MenuItem menuItem = new MenuItem();
        menuItem.setName("Введите название");
        menuItem.setPrice(0);
        return menuItem;
    }
}
