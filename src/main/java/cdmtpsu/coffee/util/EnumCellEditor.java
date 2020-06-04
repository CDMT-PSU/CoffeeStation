package cdmtpsu.coffee.util;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import java.awt.Component;
import java.util.EnumSet;

public class EnumCellEditor<T extends Enum<T>> extends DefaultCellEditor {
    private final JComboBox<T> comboBox;
    private final Class<T> enumClass;
    private Runnable onSelect;

    public EnumCellEditor(Class<T> enumClass) {
        super(new JTextField()); /* dummy */
        this.enumClass = enumClass;
        comboBox = new JComboBox<>();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table,
                                                 Object value,
                                                 boolean isSelected,
                                                 int rowIndex,
                                                 int columnIndex) {
        comboBox.removeAllItems();
        EnumSet.allOf(enumClass).forEach(comboBox::addItem);
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
