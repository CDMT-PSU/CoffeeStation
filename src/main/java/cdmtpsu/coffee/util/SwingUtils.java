package cdmtpsu.coffee.util;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.Objects;

public final class SwingUtils {
    public static void onValueChanged(JTextField textField, Runnable runnable) {
        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent event) {
                runnable.run();
            }

            @Override
            public void removeUpdate(DocumentEvent event) {
                runnable.run();
            }

            @Override
            public void changedUpdate(DocumentEvent event) {
                runnable.run();
            }
        });
    }

    private SwingUtils() {
    }
}
