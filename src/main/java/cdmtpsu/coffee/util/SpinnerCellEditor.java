package cdmtpsu.coffee.util;

import javax.swing.DefaultCellEditor;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import java.awt.Component;

// todo: buttons presses when editing begins
public class SpinnerCellEditor extends DefaultCellEditor {
    private final JSpinner spinner;

    public SpinnerCellEditor(int min, int max) {
        super(new JTextField()); /* dummy */
        var model = new SpinnerNumberModel();
        model.setMinimum(min);
        model.setMaximum(max);
        spinner = new JSpinner(model);
    }

    @Override
    public boolean stopCellEditing() {
        return super.stopCellEditing();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table,
                                                 Object value,
                                                 boolean isSelected,
                                                 int rowIndex,
                                                 int columnIndex) {
        spinner.setValue(value);
        return spinner;
    }

    @Override
    public Object getCellEditorValue() {
        return spinner.getValue();
    }
}
