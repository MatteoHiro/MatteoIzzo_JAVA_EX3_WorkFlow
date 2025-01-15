import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Document {

    int id;
    String name;
    String state;
    LocalDateTime productionDate;
    LocalDateTime modifyDateTime;
    User user;

    public Document(int id, String name, String state, LocalDateTime productionDate, LocalDateTime modifyDateTime) {
        this.id = id;
        this.name = name;
        this.state = state;
        this.productionDate = productionDate;
        this.modifyDateTime = modifyDateTime;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getProductionDate() {
        return this.productionDate;
    }

    public void setProductionDate(LocalDateTime productionDate) {
        this.productionDate = productionDate;
        this.modifyDateTime = LocalDateTime.now();
    }

    public String getFormattedDate(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
        return dateTime.format(formatter);
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
        this.modifyDateTime = LocalDateTime.now();
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Document clonedDoc() {
        Document clone = new Document(this.id, this.name, this.state, this.productionDate, this.modifyDateTime);
        clone.setUser(this.user); // Copia l'utente associato
        return clone;
    }

    @Override
    public String toString() {
        return "id: " + id
                + ", Nome: '" + name + '\''
                + ", Stato: '" + state + '\''
                + ", Data di produzione: " + getFormattedDate(productionDate)
                + ", Data modifiche: " + getFormattedDate(modifyDateTime)
                + ", Utente: " + user.getUsername();
    }

// Test
    // public static void main(String[] args) {
    //     Document doc = new Document(1, "Documento 1", "In corso", LocalDateTime.now().minusDays(1), LocalDateTime.now().minusHours(1));
    //     User user = new User("Mario Rossi");
    //     doc.setUser(user);
    //     System.out.println(doc);
    //     
    //     Document clonedDoc = doc.clonedDoc();
    //     System.out.println("Copia del documento: " + clonedDoc);
    // }
}
