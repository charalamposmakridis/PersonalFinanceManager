import DatabaseHandling.DatabaseInitializer;
import models.User;
import ui.frames.LoginFrame;
import ui.frames.MainFrame;

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