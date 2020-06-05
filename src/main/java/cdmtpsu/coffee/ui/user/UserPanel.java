package cdmtpsu.coffee.ui.user;

import cdmtpsu.coffee.data.Database;
import cdmtpsu.coffee.data.User;
import cdmtpsu.coffee.ui.main.MainFrame;
import cdmtpsu.coffee.util.Refreshable;
import com.j256.ormlite.dao.Dao;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import java.awt.BorderLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.ArrayList;

public final class UserPanel extends JPanel implements Refreshable {
    private final Window owner;

    private final Dao<User, Integer> dao;
    private final ArrayList<User> users;
    private final TableModel tableModel;

    private final JButton addButton;
    private final JButton editButton;
    private final JButton removeButton;
    private final JButton changePasswordButton;
    private final JToolBar toolBar;
    private final JTable table;
    private final JScrollPane scrollPane;

    public UserPanel(Window owner) {
        this.owner = owner;

        dao = Database.getInstance().getUsers();
        users = new ArrayList<>();
        tableModel = new TableModel(users);
        scrollPane = new JScrollPane();

        /* UI */
        addButton = new JButton();
        editButton = new JButton();
        removeButton = new JButton();
        changePasswordButton = new JButton();
        toolBar = new JToolBar();
        table = new JTable();

        /* addButton */
        addButton.setText("Добавить");
        addButton.addActionListener(this::addButtonClicked);

        /* editButton */
        editButton.setText("Редактировать");
        editButton.addActionListener(this::editButtonClicked);

        /* removeButton */
        removeButton.setText("Удалить");
        removeButton.addActionListener(this::removeButtonClicked);

        /* changePasswordButton */
        changePasswordButton.setText("Изменить пароль");
        changePasswordButton.addActionListener(this::changePasswordButtonClicked);

        /* toolBar */
        toolBar.setFloatable(false);
        toolBar.add(addButton);
        toolBar.add(editButton);
        toolBar.add(removeButton);
        toolBar.addSeparator();
        toolBar.add(changePasswordButton);

        /* table */
        table.setModel(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        /* scrollPane */
        scrollPane.setViewportView(table);

        /* this */
        setLayout(new BorderLayout());
        add(toolBar, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        /**/

        refresh();
    }

    /* Event handlers */
    private void addButtonClicked(ActionEvent event) {
        AddUserDialog dialog = new AddUserDialog(owner);
        dialog.setVisible(true);
        User result = dialog.getResult();
        if (result != null) {
            try {
                dao.create(result);
                refresh();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void editButtonClicked(ActionEvent event) {
        User user = getSelectedUser();
        if (user == null) {
            showUserNotSelectedMessageDialog();
            return;
        }
        EditUserDialog dialog = new EditUserDialog(owner, user);
        dialog.setVisible(true);
        User result = dialog.getResult();
        if (result != null) {
            try {
                dao.update(result);
                refresh();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void removeButtonClicked(ActionEvent event) {
        User user = getSelectedUser();
        if (user == null) {
            showUserNotSelectedMessageDialog();
            return;
        }

        /* Не даем удалить самого себя! */
        User sessionUser = ((MainFrame) owner).getUser();
        if (user.equals(sessionUser)) {
            showSelfDeletionMessageDialog();
            return;
        }

        int confirmed = showUserDeletionConfirmationDialog();
        if (confirmed != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            dao.delete(user);
            refresh();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void changePasswordButtonClicked(ActionEvent event) {
        User user = getSelectedUser();
        if (user == null) {
            showUserNotSelectedMessageDialog();
            return;
        }
        ChangePasswordDialog dialog = new ChangePasswordDialog(owner, user);
        dialog.setVisible(true);
        User result = dialog.getResult();
        if (result != null) {
            try {
                dao.update(user);
                refresh();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    /**/

    /* Logic */
    public void refresh() {
        users.clear();
        dao.forEach(users::add);
        tableModel.fireTableDataChanged();
    }

    private User getSelectedUser() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            return null;
        }
        return users.get(selectedRow);
    }

    private void showUserNotSelectedMessageDialog() {
        JOptionPane.showMessageDialog(owner,
                "Укажите пользователя в таблице.", "Внимание",
                JOptionPane.WARNING_MESSAGE);
    }

    private void showSelfDeletionMessageDialog() {
        JOptionPane.showMessageDialog(owner,
                "Вы не можете удалить самого себя.", "Внимание",
                JOptionPane.WARNING_MESSAGE);
    }

    private int showUserDeletionConfirmationDialog() {
        return JOptionPane.showConfirmDialog(owner,
                "Вы действительно хотите удалить указанного пользователя?", "Подтверждение",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
    }
    /**/

    private static final class TableModel extends AbstractTableModel {
        private static final int COLUMN_COUNT = 4;
        private static final String[] COLUMN_NAMES = {"Имя пользователя", "Хеш", "ФИО", "Роль"};

        private final ArrayList<User> users;

        TableModel(ArrayList<User> users) {
            this.users = users;
        }

        @Override
        public int getRowCount() {
            return users.size();
        }

        @Override
        public int getColumnCount() {
            return COLUMN_COUNT;
        }

        @Override
        public String getColumnName(int columnIndex) {
            return COLUMN_NAMES[columnIndex];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            User user = users.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return user.getUsername();
                case 1:
                    return user.getHash();
                case 2:
                    return user.getName();
                case 3:
                    return user.getRole();
            }
            return null;
        }

        @Override
        public void setValueAt(Object value, int rowIndex, int columnIndex) {
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }
    }
}
