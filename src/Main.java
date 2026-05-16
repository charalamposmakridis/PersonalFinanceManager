import DatabaseHandling.DatabaseConnection;
import DatabaseHandling.DatabaseInitializer;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
        DatabaseInitializer.initializeDatabase();
        System.out.println("Database is ready!");
    }
}