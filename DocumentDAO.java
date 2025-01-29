import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class DocumentDAO {

    public void addDocumentToDatabase(Document document) {
        String query = "INSERT INTO Document (name_doc, state_doc, productionDate, modifyDateTime, id_user) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Non settare id_document perché è AUTO_INCREMENT
            statement.setString(1, document.getName());
            statement.setString(2, document.getState());
            
            // Passa direttamente il Timestamp
            statement.setTimestamp(3, Timestamp.valueOf(document.getProductionDate()));
            statement.setTimestamp(4, Timestamp.valueOf(document.getModifyDateTime()));
            
            // Setta l'ID dell'utente
            statement.setInt(5, document.getUser().getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
