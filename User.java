public class User {
    int id;
    String username;
    String role;
    String email;
    String seniority;

    public User(int newUserId, String userName, String userRole, String userEmail, String userSeniority) {
        this.id = newUserId; 
        this.username = userName;
        this.role = userRole;
        this.email = userEmail;
        this.seniority = userSeniority;
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


    @Override
    public String toString() {
        return "User: " +
                "id: " + id +
                ", username: '" + username + '\'' +
                ", role: '" + role + '\'' +
                ", email: '" + email + '\'' +
                ", seniority: '" + seniority + '\'';
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

