import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DocumentDAO {

    public void addDocumentToDatabase(Document document) {
        String query = "INSERT INTO documents (id, name, state, productionDate, modifyDateTime, userId) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, document.getId());
            statement.setString(2, document.getName());
            statement.setString(3, document.getState());
            statement.setString(4, document.getFormattedDate(document.getProductionDate()));
            statement.setString(5, document.getFormattedDate(document.getModifyDateTime()));
            statement.setInt(6, document.getUser().getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

