package DatabaseHandling;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class DatabaseConnection {
    private static final String DB_URL="jdbc:sqlite:database/finance.db";

    public static Connection getConnection(){
        try{
            Connection conn= DriverManager.getConnection(DB_URL);
            enableForeignKeys(conn);
            return conn;
        }catch (SQLException e){
            throw new RuntimeException("Cannot connect to database",e);
        }
    }

    private static void enableForeignKeys(Connection conn) throws SQLException{
        try(Statement statement=conn.createStatement()){
            statement.execute("PRAGMA foreign_keys=ON");
        }
    }
}
