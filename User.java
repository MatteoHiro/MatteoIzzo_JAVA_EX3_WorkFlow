// Commit
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class User {

    private int id_user;
    private String username;
    private String role_job;
    private String email;
    private String seniority;
    private Timestamp data_creazione_utente;

    public User(int id_user, String username, String role, String email, String seniority, Timestamp dataCreazione) {
        this.id_user = id_user;
        this.username = username;
        this.role_job = role;
        this.email = email;
        this.seniority = seniority;
        this.data_creazione_utente = dataCreazione;
    }

    public int getId() {
        return id_user;
    }

    public void setId(int id_user) {
        this.id_user = id_user;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role_job;
    }

    public void setRole(String role) {
        this.role_job = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSeniority() {
        return seniority;
    }

    public void setSeniority(String seniority) {
        this.seniority = seniority;
    }

    public Timestamp getDataCreazione() {
        return data_creazione_utente;
    }

    public void setDataCreazione(Timestamp dataCreazione) {
        this.data_creazione_utente = dataCreazione;
    }

    public void addUser(User user) {
        if (user == null) {
            System.err.println("L'utente non può essere null.");
            return;
        }
        // codice per aggiungere l'utente alla lista o al database
        System.out.println("Utente aggiunto: " + user.getUsername());
        String query = "INSERT INTO User (id_user, username, role_job, email, seniority, data_creazione_utente) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, user.getId());
            stmt.setString(2, user.getUsername());
            stmt.setString(3, user.getRole());
            stmt.setString(4, user.getEmail());
            stmt.setString(5, user.getSeniority());
            stmt.setTimestamp(6, user.getDataCreazione()); // Aggiungi la data di creazione
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return String.format(
                "%-15s %s%n%-15s %s%n%-15s %s%n%-15s %s%n%-15s %s%n%s%n",
                "ID Utente:", id_user,
                "Username:", username,
                "Ruolo:", role_job,
                "Email:", email,
                "Seniority:", seniority,
                "---------------------------------"
        );
    }

}
