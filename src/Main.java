import DatabaseHandling.DatabaseInitializer;
import ui.frames.LoginFrame;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        DatabaseInitializer.initializeDatabase();

        SwingUtilities.invokeLater(() -> {

            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);

        });
    }
}