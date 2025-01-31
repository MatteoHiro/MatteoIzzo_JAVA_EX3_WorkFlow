// Commit
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Document {

    private int id;
    private String name;
    private String state;
    private LocalDateTime productionDate;
    private LocalDateTime modifyDateTime;
    private User user;

    public Document(int id, String name, String state, LocalDateTime productionDate, LocalDateTime modifyDateTime, User user) {
        this.id = id;
        this.name = name;
        this.state = state;
        this.productionDate = productionDate;
        this.modifyDateTime = modifyDateTime;
        this.user = user;
    }

    // Getter e Setter
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
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
        Document clone = new Document(this.id, this.name, this.state, this.productionDate, this.modifyDateTime, this.user);
        return clone;
    }

    public User clonedUser() {
        if (this.user != null) {
            User clone = new User(this.user.getId(), this.user.getUsername(), this.user.getRole(), this.user.getEmail(), this.user.getSeniority(), this.user.getDataCreazione());
            return clone;
        }
        return null; // Restituisce null se l'utente è null
    }

    // Getter per la data di modifica
    public LocalDateTime getModifyDateTime() {
        return modifyDateTime;
    }

    // Test
    // public static void main(String[] args) {
    //     Document doc = new Document(1, "Documento 1", "In corso", LocalDateTime.now().minusDays(1), LocalDateTime.now().minusHours(1), null);
    //     User user = new User(1, "Mario Rossi", "Developer", "mario@example.com", "Junior", LocalDateTime.now().minusMonths(1));
    //     doc.setUser(user);
    //     System.out.println(doc);
    //     
    //     Document clonedDoc = doc.clonedDoc();
    //     System.out.println("Copia del documento: " + clonedDoc);
    // }
}
