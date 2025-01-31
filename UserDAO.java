// Commit
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDAO {

    private static final Logger LOGGER = Logger.getLogger(UserDAO.class.getName());

    public void addUserToDatabase(User user) {
        String query = "INSERT INTO User (id_user, username, role_job, email, seniority) VALUES (?, ?, ?, ?, ?)";

        // Verifica che l'utente non sia null prima di procedere
        if (user == null) {
            LOGGER.warning("L'utente è null. Non è stato possibile aggiungere l'utente al database.");
            return;
        }

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {

            // Impostazione dei parametri della query
            // statement.setInt(1, user.getId());
            statement.setString(2, user.getUsername());
            statement.setString(3, user.getRole());
            statement.setString(4, user.getEmail());
            statement.setString(5, user.getSeniority());

            statement.executeUpdate();
            LOGGER.log(Level.INFO, "Utente aggiunto al database: ", user.getUsername());
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore nell'inserimento dell'utente nel database", e);
        }
    }
}
