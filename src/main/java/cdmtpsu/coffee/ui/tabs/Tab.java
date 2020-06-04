package cdmtpsu.coffee.ui.tabs;

import cdmtpsu.coffee.data.DataObject;
import com.j256.ormlite.dao.Dao;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import java.awt.BorderLayout;
import java.awt.Window;
import java.sql.SQLException;
import java.util.ArrayList;

public abstract class Tab<T extends DataObject> extends JPanel {
    private final Dao<T, Integer> dao;
    private final ArrayList<T> items;
    private final TabTableModel<T> tableModel;
    private final JTable table;

    private final boolean userCanAdd;
    private final boolean userCanRemove;
    private final boolean userCanEdit;

    public Tab(String[] columns, Class[] classes, Dao<T, Integer> dao,
               boolean userCanAdd, boolean userCanRemove, boolean userCanEdit) {
        this.dao = dao;
        items = new ArrayList<>();
        tableModel = new TabTableModel<>(columns, classes, userCanEdit, items, dao);
        table = new JTable(tableModel);
        setLayout(new BorderLayout());
        add(new JScrollPane(table));
        refresh();

        this.userCanAdd = userCanAdd;
        this.userCanEdit = userCanEdit;
        this.userCanRemove = userCanRemove;
    }

    public boolean userCanAdd() {
        return userCanAdd;
    }

    public boolean userCanRemove() {
        return userCanRemove;
    }

    public boolean userCanEdit() {
        return userCanAdd;
    }

    public void setCellEditor(String columnIdentifier, TableCellEditor cellEditor) {
        table.getColumn(columnIdentifier).setCellEditor(cellEditor);
    }

    public void refresh() {
        items.clear();
        dao.forEach(items::add);
        tableModel.fireTableDataChanged();
    }

    public void add(Window owner) {
        var item = createItem(owner);
        if (item == null) {
            return;
        }
        items.add(item);
        tableModel.fireTableDataChanged();
        try {
            dao.create(item);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract T createItem(Window owner);

    public void remove(Window owner) {
        var rows = table.getSelectedRows();
        if (rows.length > 0) {
            var result = JOptionPane.showConfirmDialog(
                    owner,
                    "Вы действительно хотите удалить указанные записи?",
                    "Сообщение",
                    JOptionPane.YES_NO_OPTION
            );
            if (result == JOptionPane.NO_OPTION) {
                return;
            }
        }
        for (int i = rows.length - 1; i >= 0; i--) {
            var item = items.get(rows[i]);
            items.remove(item);
            tableModel.fireTableDataChanged();
            try {
                dao.delete(item);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static final class TabTableModel<T extends DataObject> extends AbstractTableModel {
        private final String[] columns;
        private final Class[] classes;
        private final boolean editable;
        private final ArrayList<T> items;
        private final Dao<T, Integer> dao;

        TabTableModel(String[] columns,
                      Class[] classes,
                      boolean editable,
                      ArrayList<T> items,
                      Dao<T, Integer> dao) {
            this.columns = columns;
            this.classes = classes;
            this.editable = editable;
            this.items = items;
            this.dao = dao;
        }

        @Override
        public int getRowCount() {
            return items.size();
        }

        @Override
        public int getColumnCount() {
            return columns.length;
        }

        @Override
        public String getColumnName(int columnIndex) {
            return columns[columnIndex];
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return classes[columnIndex];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return items.get(rowIndex).getValue(columnIndex);
        }

        @Override
        public void setValueAt(Object value, int rowIndex, int columnIndex) {
            var item = items.get(rowIndex);
            var oldValue = item.getValue(columnIndex);
            item.setValue(columnIndex, value);
            try {
                dao.update(item);
            } catch (SQLException e) {
                //e.printStackTrace();
                item.setValue(columnIndex, oldValue);
            }
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return editable;
        }
    }
}
