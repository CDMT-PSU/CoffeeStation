package cdmtpsu.coffee.util;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;
import java.awt.Color;
import java.awt.Component;

// todo: select all on editing
public class TextFieldCellEditor extends DefaultCellEditor {
    private static final Border BLACK =
            BorderFactory.createLineBorder(Color.BLACK);
    private static final Border RED =
            BorderFactory.createLineBorder(Color.RED);


    private final String regex;
    private final JTextField textField;
    private final boolean useInitialValue;

    public TextFieldCellEditor(String regex) {
        this(regex, true);
    }

    public TextFieldCellEditor(String regex, boolean useInitialValue) {
        super(new JTextField());
        this.regex = regex;
        this.useInitialValue = useInitialValue;
        textField = (JTextField) editorComponent;
    }

    @Override
    public boolean stopCellEditing() {
        if (!textField.getText().matches(regex)) {
            textField.setBorder(RED);
            return false;
        }
        return super.stopCellEditing();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table,
                                                 Object value,
                                                 boolean isSelected,
                                                 int rowIndex,
                                                 int columnIndex) {
        textField.setBorder(BLACK);
        if (useInitialValue) {
            textField.setText((String) value);
        }
        return textField;
    }

    @Override
    public Object getCellEditorValue() {
        return textField.getText();
    }
}
