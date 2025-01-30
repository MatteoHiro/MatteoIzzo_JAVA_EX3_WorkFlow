
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/WORK_FLOW";
    private static final String USER = "root";
    private static final String PASSWORD = "ALEmaTT00!!";

    public static Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connessione al database stabilita!");
            return connection;
        } catch (SQLException e) {
            System.err.println("Errore durante la connessione al database: " + e.getMessage());
            return null;
        }
    }
}
