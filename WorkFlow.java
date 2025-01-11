import java.util.ArrayList;
import java.util.List;

public class WorkFlow {
    private List<Document> documents;
    private List<Document> logDocuments;

    public WorkFlow() {
        this.documents = new ArrayList<>();
        this.logDocuments = new ArrayList<>();
    }

    public void addDocument(Document document) {
        documents.add(document);
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

}
