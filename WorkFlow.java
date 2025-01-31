// Commit
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class WorkFlow {

    private final List<Document> documents;
    private final List<Document> logDocuments;
    private final List<User> users;

    public WorkFlow() {
        this.documents = new ArrayList<>();
        this.logDocuments = new ArrayList<>();
        this.users = new ArrayList<>();
    }

    public void addDocument(Document document, User user) {
        if (user == null) {
            System.err.print("Un documento deve essere creato da un utente valido.");
            return;
        }
        String query = "INSERT INTO Document (name_doc, state_doc, productionDate, modifyDateTime, id_user) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, document.getName());
            statement.setString(2, document.getState());
            statement.setString(3, document.getFormattedDate(document.getProductionDate()));
            statement.setString(4, document.getFormattedDate(document.getModifyDateTime()));
            statement.setInt(5, document.getUser().getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        document.setUser(user);
        documents.add(document);
    }

    public void addUser(User user) {
        users.add(user);
    }

    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT id_user, username, role_job, email, seniority, data_creazione_utente FROM User";
        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("id_user");
                String username = rs.getString("username");
                String roleJob = rs.getString("role_job");
                String email = rs.getString("email");
                String seniority = rs.getString("seniority");
                Timestamp dataCreazione = rs.getTimestamp("data_creazione_utente");

                // Crea un nuovo oggetto User con i dati recuperati
                User user = new User(id, username, roleJob, email, seniority, dataCreazione);
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public void removeUser(User user) {
        users.remove(user);
    }

    public void addLogDocument(Document document) {
        logDocuments.add(document.clonedDoc());
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public List<Document> getLogDocuments() {
        return logDocuments;
    }

    public void removeDocument(Document doc) {
        documents.remove(doc);
    }

    public List<Document> getUserID() {
        List<Document> userID = new ArrayList<>();
        for (Document doc : documents) {
            userID.add(doc);
        }
        return userID;
    }

    public List<User> removedUser() {
        List<User> removedUsers = new ArrayList<>(users); // Crea una copia della lista degli utenti
        users.clear(); // Pulisce la lista degli utenti
        return removedUsers; // Restituisce gli utenti rimossi
    }

    public Document getDocumentById(int idLOG) {
        for (Document doc : documents) {
            if (doc.getId() == idLOG) {
                return doc;
            }
        }
        return null;
    }

    public void showDocumentsFromDB(String query) {
        System.out.println("Documenti presenti nel database:");
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id_document");
                String name = rs.getString("name_doc");
                String state = rs.getString("state_doc");
                Timestamp productionDate = rs.getTimestamp("productionDate");
                Timestamp modifyDateTime = rs.getTimestamp("modifyDateTime");
                int userId = rs.getInt("id_user");
                System.out.println("ID: " + id + ", Name: " + name + ", State: " + state
                        + ", Production Date: " + productionDate
                        + ", User ID: " + userId
                        + ", Ultima modifica: " + modifyDateTime);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadDocumentsFromDB() {
        String query = "SELECT * FROM Document";
        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int id = rs.getInt("id_document");
                String name = rs.getString("name_doc");
                String state = rs.getString("state_doc");
                LocalDateTime productionDate = rs.getTimestamp("productionDate").toLocalDateTime();
                int userId = rs.getInt("id_user");
                User user = getUserById(userId);
                Document doc = new Document(id, name, state, productionDate, LocalDateTime.now(), user);
                documents.add(doc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void showDocumentLogs(int documentId) {
        String query = "SELECT * FROM document_version WHERE documento_id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, documentId); // Imposta l'id del documento da cercare nei log

            ResultSet rs = stmt.executeQuery();

            // Controlla se ci sono risultati
            if (!rs.next()) {
                System.out.println("Nessun log trovato per il documento con ID: " + documentId);
                return;
            }

            // Cicla sui risultati e mostra i log
            do {
                int id = rs.getInt("id_document_version");
                String name = rs.getString("name_doc");
                String state = rs.getString("state_doc");
                Timestamp productionDate = rs.getTimestamp("productionDate");
                Timestamp modifyDateTime = rs.getTimestamp("modifyDateTime");
                int userId = rs.getInt("id_user");

                System.out.println("Log ID: " + id);
                System.out.println("Nome documento: " + name);
                System.out.println("Stato documento: " + state);
                System.out.println("Data di produzione: " + productionDate);
                System.out.println("Data di modifica: " + modifyDateTime);
                System.out.println("ID utente: " + userId);
                System.out.println("---------------------------");

            } while (rs.next());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private User getUserById(int userId) {
        for (User user : users) {
            if (user.getId() == userId) {
                return user;
            }
        }
        return null;
    }

}
