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

    

    


   


}