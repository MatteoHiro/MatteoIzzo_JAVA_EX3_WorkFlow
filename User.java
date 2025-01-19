
import java.util.ArrayList;
import java.util.List;

public class User {

    private static List<User> users = new ArrayList<>();

    int id;
   private String username;
   private String role;
   private String email;
   private String seniority;

    public User(int newUserId, String userName, String userRole, String userEmail, String userSeniority) {
        this.id = newUserId;
        this.username = userName;
        this.role = userRole;
        this.email = userEmail;
        this.seniority = userSeniority;
    }

    public void addUser(User user) {
        if (user == null) {
            System.err.println("L'utente non può essere null.");
            return;
        }
        users.add(user);
        System.out.println("Utente aggiunto: " + user.getUsername()); // Aggiungi un log
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    public User cloneUser(){
        return new User(this.id, this.username, this.role, this.email, this.seniority);
    }

    @Override
    public String toString() {
        return "User: "
                + "id: " + id
                + ", username: '" + username + '\''
                + ", role: '" + role + '\''
                + ", email: '" + email + '\''
                + ", seniority: '" + seniority + '\'';
    }

    // public static void main(String[] args) {
    //     User user = new User();
    //     user.setId(1);
    //     user.setUsername("admin");
    //     user.setRole("Admin");
    //     user.setEmail("admin@example.com");
    //     user.setSeniority("Senior Developer");
    //     System.out.println(user);
    // }
}
