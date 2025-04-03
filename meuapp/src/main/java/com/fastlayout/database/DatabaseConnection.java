package  com.fastlayout.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import software.amazon.jdbc.PropertyDefinition;



public class DatabaseConnection {

    private String host;
    private String db;
    private int dbPort;
    private String dbUser;
    private String dbPsw;
    

    public DatabaseConnection(String host, String db, int dbPort, String dbUser, String dbPsw) {
        this.host = host + ".db.vistahost.com.br";  //".cwc1g77rsraq.sa-east-1.rds.amazonaws.com";
        this.db = db.trim();
        this.dbPort = dbPort;
        this.dbUser = dbUser.trim();
        this.dbPsw = dbPsw.trim();

        
    }

    public Connection getConnection() throws SQLException {

        String jdbcUrl = "jdbc:mysql://" + host + ":" + dbPort + "/" + db;
        
        Properties properties = new Properties();
        properties.setProperty("user", dbUser);
        properties.setProperty("password", dbPsw);
        properties.setProperty(PropertyDefinition.TCP_KEEP_ALIVE.name, "true");
        properties.setProperty("enableCleartextPlugin", "true");
        


        return DriverManager.getConnection(jdbcUrl,properties);
        /*return null;*/
    }

}
