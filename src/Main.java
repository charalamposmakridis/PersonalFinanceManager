import models.User;
import ui.frames.MainFrame;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            User testUser = new User(
                    1,
                    "testuser",
                    "hashedpassword",
                    "Test User",
                    java.time.LocalDateTime.now()
            );

            new MainFrame(testUser).setVisible(true);

            new MainFrame(testUser).setVisible(true);
        });
    }
}