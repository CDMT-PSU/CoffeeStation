package cdmtpsu.coffee.ui.welcome;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import java.awt.Dimension;

public final class WelcomeFrame extends JFrame {
    private final JTabbedPane tabbedPane;
    private final JPanel contentPane;

    public WelcomeFrame() {
        tabbedPane = new JTabbedPane();
        contentPane = new JPanel();

        /* tabbedPane */
        tabbedPane.addTab("Войти", new LogInPanel(this));
        tabbedPane.addTab("Создать учетную запись", new SignUpPanel(this));

        /* contentPane */
        contentPane.setPreferredSize(new Dimension(320, 300));
        contentPane.setLayout(new BorderLayout());
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        /* this */
        setTitle("CoffeeStation");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setContentPane(contentPane);
        pack();
        setLocationRelativeTo(null);
    }
}
