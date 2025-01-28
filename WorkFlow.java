
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
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
        String query = "INSERT INTO documents (id, name, state, production_date, creation_date, user_id) VALUES (?, ?, ?, ?, ?, ?)";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setInt(1, document.getId());
        stmt.setString(2, document.getName());
        stmt.setString(3, document.getState());
        stmt.setTimestamp(4, Timestamp.valueOf(document.getProductionDate()));
        stmt.setTimestamp(5, Timestamp.valueOf(document.getProductionDate()));
        stmt.setInt(6, user.getId());
        stmt.executeUpdate();
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

    public List<Document> getUserID(){
        List<Document> userID = new ArrayList<>();
        for (Document doc : documents) {
            userID.add(doc);
        }
        return userID;
    }

    public List<User> removedUser(){
        List<User> removedUsers = new ArrayList<>(users); // Crea una copia della lista degli utenti
        users.clear(); // Pulisce la lista degli utenti
        return removedUsers; // Restituisce gli utenti rimossi
    }

    public Document getDocumentById(int idLOG) {
        throw new UnsupportedOperationException("Unimplemented method 'getDocumentById'");
    }

}
