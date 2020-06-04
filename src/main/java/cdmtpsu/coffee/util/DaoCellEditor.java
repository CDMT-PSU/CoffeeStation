package cdmtpsu.coffee.util;

import com.j256.ormlite.dao.Dao;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import java.awt.Component;
import java.sql.SQLException;

public class DaoCellEditor<T> extends DefaultCellEditor {
    private final Dao<T, Integer> dao;
    private final JComboBox<T> comboBox;
    private Runnable onSelect;

    public DaoCellEditor(Dao<T, Integer> dao) {
        super(new JTextField()); /* dummy */
        this.dao = dao;
        comboBox = new JComboBox<>();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table,
                                                 Object value,
                                                 boolean isSelected,
                                                 int rowIndex,
                                                 int columnIndex) {
        try {
            comboBox.removeAllItems();
            var items = dao.queryForAll();
            items.forEach(comboBox::addItem);
            for (var item : items) {
                if (item.equals(value)) {
                    comboBox.setSelectedItem(item);
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comboBox;
    }

    @Override
    public Object getCellEditorValue() {
        if (onSelect != null) {
            onSelect.run();
        }
        return comboBox.getSelectedItem();
    }

    public void setOnSelect(Runnable onSelect) {
        this.onSelect = onSelect;
    }
}
