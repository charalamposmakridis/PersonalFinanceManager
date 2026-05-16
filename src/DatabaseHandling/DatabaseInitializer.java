package DatabaseHandling;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;

public final class DatabaseInitializer {
    public static void initializeDatabase(){
        try(Connection conn=DatabaseConnection.getConnection();
            InputStream input=DatabaseInitializer.class.getClassLoader().getResourceAsStream("resources/schema.sql")){

            if(input==null){
                throw new RuntimeException("schema.sql not found");
            }

            String sql=new String(input.readAllBytes(), StandardCharsets.UTF_8);

            try(Statement stmt=conn.createStatement()){
                for(String query:sql.split(";")){
                    if(!query.trim().isEmpty()){
                        stmt.execute(query);
                    }
                }
            }

        }catch (Exception e){
            throw new RuntimeException("Could not initialize database.",e);
        }
    }
}
