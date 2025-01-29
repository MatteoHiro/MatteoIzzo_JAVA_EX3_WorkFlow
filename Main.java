
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {
        WorkFlow wf = new WorkFlow();
        menu(wf);
    }

    // Menu utente 
    public static void menu(WorkFlow wf) {

        try (var scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("\nMenu:");
                System.out.println("1. Visualizza tutti i documenti ");
                System.err.println("2. Cerca un documento tramite ID");
                System.out.println("3. Visualizza il log dei cambiamenti tramitre l'ID del documento ");
                System.out.println("4. Aggiorna lo stato di un documento ");
                System.out.println("5. Aggiungi un nuovo documento ");
                System.out.println("6. Elimina un documento ");
                System.out.println("7. Elimina tutti i documenti ");
                System.out.println("8. Visualizza gli utenti ");
                System.out.println("9. Modifica informazioni utente ");
                System.out.println("10. Elimina un utente ");
                System.out.println("11. Aggiungi utente ");
                System.out.println("0. Esci");
                System.out.print("Digita il numero corrispondete all'azione desiderata: ");
                int choice = scanner.nextInt();
                scanner.nextLine();
                if (choice < 0 || choice > 11) {
                    System.out.println("Errore: Scelta non valida. Inserisci un numero tra 0 e 11.");
                    continue;
                }

                switch (choice) {
                    case 1 -> {
                        // Visualizza tutti i documenti
                        for (Document doc : wf.getDocuments()) {
                            System.out.println(doc);
                        }

                        // Visualizza tutti i documenti nel database
                        String query = "SELECT * FROM documents";
                        wf.showDocumentsFromDB(query);
                        break;

                    }
                    case 2 -> {
                        // Cerca un documento tramite ID
                        System.out.print("Inserisci l'ID del documento da cercare: ");
                        int id = scanner.nextInt();
                        scanner.nextLine();
                        boolean found = false;

                        for (Document doc : wf.getDocuments()) {
                            if (doc.getId() == id) {
                                System.out.println(doc);
                                found = true;
                            }
                        }

                        if (!found) {
                            System.out.println("ID non associato a nessun documento!");
                            break;
                        }

                        // Visualizza il documento nel database
                        String query = "SELECT * FROM documents WHERE id = " + id;
                        wf.showDocumentsFromDB(query);
                        break;

                    }
                    case 3 -> {
                        // Visualizza log dei cambiamenti di un documento
                        System.out.print("Inserisci l'ID del documento da cercare: ");
                        int idLOG = scanner.nextInt();
                        scanner.nextLine();
                        boolean found = false;
                        for (Document docLOG : wf.getLogDocuments()) {
                            if (docLOG.getId() == idLOG) {
                                System.out.println(docLOG);
                                found = true;
                            }
                        }

                        if (!found) {
                            System.out.println("ID non associato a nessun documento!");
                            break;
                        }

                        // Visualizza il log del documento nel database
                        String queryLOG = "SELECT * FROM document_logs WHERE document_id = " + idLOG;
                        wf.showDocumentsFromDB(queryLOG);
                        break;
                    }
                    case 4 -> {
                        // Aggiorna lo stato di un documento
                        System.out.print("Inserisci l'ID del documento da aggiornare: ");
                        int idSTATE;
                        try {
                            idSTATE = scanner.nextInt();
                            scanner.nextLine();
                        } catch (InputMismatchException e) {
                            System.out.println("Errore: L'ID deve essere un numero!");
                            scanner.nextLine(); // Consuma l'input errato
                            break;
                        }

                        Document doc = null;
                        boolean documentFound = false;
                        for (Document d : wf.getDocuments()) {
                            if (d.getId() == idSTATE) {
                                doc = d;
                                documentFound = true;
                                System.out.print("Inserisci il nuovo stato del documento: ");
                                String newState = scanner.nextLine();

                                System.out.print("Inserisci l'ID dell'utente che ha apportato la modifica: ");
                                int newUserId;
                                try {
                                    newUserId = scanner.nextInt();
                                    scanner.nextLine();
                                } catch (InputMismatchException e) {
                                    System.out.println("Errore: L'ID deve essere un numero!");
                                    scanner.nextLine(); // Consuma l'input errato
                                    break;
                                }

                                User foundUser = null;
                                for (User user : wf.getUsers()) {
                                    if (user.getId() == newUserId) {
                                        foundUser = user;
                                        break;
                                    }
                                }

                                if (foundUser == null) {
                                    System.out.println("ID non associato a nessun utente!");
                                    break;
                                }

                                doc.setState(newState);
                                doc.setUser(foundUser);
                                wf.addLogDocument(doc);

                                // Aggiorna lo stato del documento nel database
                                String query = "UPDATE documents SET state = ?, user_id = ? WHERE id = ?";
                                try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
                                    statement.setString(1, newState);
                                    statement.setInt(2, newUserId);
                                    statement.setInt(3, idSTATE);
                                    statement.executeUpdate();
                                    System.out.println("Stato aggiornato con successo!");
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                break;
                            }
                        }

                        if (!documentFound) {
                            System.out.println("ID documento non trovato!");
                        }

                        // Memorizza il log del documento nel database
                        String queryLOG = "INSERT INTO document_logs (document_id, name, state, production_date, creation_date, user_id) VALUES (?, ?, ?, ?, ?, ?)";
                        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(queryLOG)) {
                            statement.setInt(1, idSTATE);
                            statement.setString(2, doc.getName());
                            statement.setString(3, doc.getState());
                            statement.setTimestamp(4, Timestamp.valueOf(doc.getProductionDate()));
                            statement.setTimestamp(5, Timestamp.valueOf(doc.getModifyDateTime()));
                            statement.setInt(6, doc.getUser().getId());
                            statement.executeUpdate();
                            System.out.println("Log documento aggiunto con successo!");
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }

                    case 5 -> {
                        // Aggiungi un nuovo documento con ID che incrementa
                        // in base al numero di documenti presente in modo da non avere due id uguali
                        int newId = wf.getDocuments().size() + 1;

                        System.out.print("Inserisci l'ID dell'utente che ha creato il documento: ");
                        int userId = scanner.nextInt();
                        scanner.nextLine(); // Consuma la nuova linea

                        User user = null;
                        for (User u : wf.getUsers()) {
                            if (u.getId() == userId) {
                                user = u;
                                break;
                            }
                        }

                        if (user == null) {
                            System.out.println("ID utente non trovato. Riprova.");
                            break;
                        }

                        System.out.print("Inserisci il nome del nuovo documento: ");
                        String newName = scanner.nextLine();
                        System.out.print("Inserisci lo stato iniziale del nuovo documento: ");
                        String newState = scanner.nextLine();
                        System.out.print("Inserisci la data di produzione del nuovo documento (formato YYYY-MM-DD HH:MM): ");
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                        LocalDateTime newProductionDate;
                        try {
                            newProductionDate = LocalDateTime.parse(scanner.nextLine(), formatter);
                        } catch (DateTimeParseException e) {
                            System.out.println("Formato data non valido! Assicurati di usare il formato YYYY-MM-DD HH:MM.");
                            break; // Torna al menu senza aggiungere il documento
                        }

                        Document newDoc = new Document(newId, newName, newState, newProductionDate, LocalDateTime.now());
                        wf.addDocument(newDoc, user);
                        wf.addLogDocument(newDoc);

                        // Aggiungi il documento al database
                        String query = "INSERT INTO documents (id, name, state, production_date, creation_date, user_id) VALUES (?, ?, ?, ?, ?, ?)";
                        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
                            stmt.setInt(1, newDoc.getId());
                            stmt.setString(2, newDoc.getName());
                            stmt.setString(3, newDoc.getState());
                            stmt.setTimestamp(4, Timestamp.valueOf(newDoc.getProductionDate()));
                            stmt.setTimestamp(5, Timestamp.valueOf(newDoc.getProductionDate()));
                            stmt.setInt(6, user.getId());
                            stmt.executeUpdate();
                            System.out.println("Documento aggiunto con successo nel database!");
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }

                    case 6 -> {
                        System.out.print("Inserisci l'ID del documento da eliminare: ");
                        int idDELETE = scanner.nextInt();
                        scanner.nextLine();

                        // Controlla se l'ID esiste nel database prima di tentare la cancellazione
                        String checkQuery = "SELECT COUNT(*) FROM documents WHERE id = ?";
                        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {

                            checkStmt.setInt(1, idDELETE);
                            ResultSet rs = checkStmt.executeQuery();
                            if (rs.next() && rs.getInt(1) == 0) {
                                System.out.println("ID non trovato nel database!");
                                return;
                            }

                        } catch (SQLException e) {
                            System.out.println("Errore durante la verifica dell'ID.");
                            e.printStackTrace();
                            return;
                        }

                        // Rimuove il documento dalla memoria e aggiunge il log
                        boolean found = false;
                        for (Document doc : wf.getDocuments()) {
                            if (doc.getId() == idDELETE) {
                                wf.removeDocument(doc);
                                wf.addLogDocument(doc);
                                found = true;
                                break;
                            }
                        }

                        if (!found) {
                            System.out.println("ID non associato a nessun documento in memoria!");
                        }

                        // Elimina il documento dal database
                        String query = "DELETE FROM documents WHERE id = ?";
                        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

                            stmt.setInt(1, idDELETE);
                            int rowsAffected = stmt.executeUpdate();

                            if (rowsAffected > 0) {
                                System.out.println("Documento eliminato con successo dal database!");
                            } else {
                                System.out.println("Errore: nessun documento eliminato.");
                            }

                        } catch (SQLException e) {
                            System.out.println("Errore durante l'eliminazione del documento.");
                            e.printStackTrace();
                        }
                    }

                    case 7 -> {
                        // Elimina tutti i documenti
                        wf.getDocuments().clear();
                        wf.getLogDocuments().clear();
                        System.out.println("Tutti i documenti sono stati eliminati!");

                        // Elimina tutti i documenti dal database
                        String query = "DELETE FROM documents";
                        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
                            stmt.executeUpdate();
                            System.out.println("Tutti i documenti sono stati eliminati dal database!");
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        break;
                    }

                    case 8 -> {
                        // Visualizza gli utenti
                        System.out.println("Lista degli utenti:");
                        for (User user : wf.getUsers()) {
                            System.out.println(user);
                        }

                        // Visualizza gli utenti nel database
                        String query = "SELECT * FROM users";
                        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
                            System.out.println("ID\tUsername\tRuolo\tEmail\tSeniority");
                            while (rs.next()) {
                                int id = rs.getInt("id");
                                String username = rs.getString("username");
                                String role = rs.getString("role");
                                String email = rs.getString("email");
                                String seniority = rs.getString("seniority");
                                System.out.printf("%d\t%s\t%s\t%s\t%s\n", id, username, role, email, seniority);
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }

                    case 9 -> {
                        // Modifica informazioni utente
                        System.out.print("Inserisci l'ID dell'utente da modificare: ");
                        int idUSER = scanner.nextInt();
                        scanner.nextLine();
                        boolean found = false;
                        for (User user : wf.getUsers()) {
                            if (user.getId() == idUSER) {
                                found = true;
                                System.err.println("Quale modifica vuoi effettuare?");
                                System.out.println("1. Modifica username");
                                System.out.println("2. Modifica ruolo");
                                System.out.println("3. Modifica email");
                                System.out.println("4. Modifica seniority");
                                int choiceUser = scanner.nextInt();
                                scanner.nextLine();

                                switch (choiceUser) {
                                    case 1 -> {
                                        System.out.print("Inserisci il nuovo username: ");
                                        String newUsername = scanner.nextLine();
                                        user.setUsername(newUsername);
                                        System.out.println("Username aggiornato con successo!");

                                        // Aggiorna il database
                                        String query = "UPDATE users SET username = ? WHERE id = ?";
                                        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
                                            stmt.setString(1, newUsername);
                                            stmt.setInt(2, user.getId());
                                            stmt.executeUpdate();
                                            System.out.println("Username aggiornato con successo nel database!");
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    case 2 -> {
                                        System.out.print("Inserisci il nuovo ruolo: ");
                                        String newRole = scanner.nextLine();
                                        user.setRole(newRole);
                                        System.out.println("Ruolo aggiornato con successo!");

                                        // Aggiorna il database
                                        String query = "UPDATE users SET role = ? WHERE id = ?";
                                        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
                                            stmt.setString(1, newRole);
                                            stmt.setInt(2, user.getId());
                                            stmt.executeUpdate();
                                            System.out.println("Ruolo aggiornato con successo nel database!");
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    case 3 -> {
                                        System.out.print("Inserisci la nuova email: ");
                                        String newEmail = scanner.nextLine();
                                        user.setEmail(newEmail);
                                        System.out.println("Email aggiornata con successo!");

                                        // Aggiorna il database
                                        String query = "UPDATE users SET email = ? WHERE id = ?";
                                        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
                                            stmt.setString(1, newEmail);
                                            stmt.setInt(2, user.getId());
                                            stmt.executeUpdate();
                                            System.out.println("Email aggiornata con successo nel database!");
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    case 4 -> {
                                        System.out.print("Inserisci la nuova seniority: ");
                                        String newSeniority = scanner.nextLine();
                                        user.setSeniority(newSeniority);
                                        System.out.println("Seniority aggiornata con successo!");

                                        // Aggiorna il database
                                        String query = "UPDATE users SET seniority = ? WHERE id = ?";
                                        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
                                            stmt.setString(1, newSeniority);
                                            stmt.setInt(2, user.getId());
                                            stmt.executeUpdate();
                                            System.out.println("Seniority aggiornata con successo nel database!");
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                                break;
                            }
                        }

                        if (!found) {
                            System.out.println("ID non associato a nessun utente!");
                        }
                    }

                    case 10 -> {
                        // Elimina utente
                        System.err.print("Inserisci l'ID dell'utente da eliminare: ");
                        int idUSER = scanner.nextInt();
                        scanner.nextLine();
                        for (User user : wf.getUsers()) {
                            if (user.getId() == idUSER) {
                                wf.removeUser(user);
                                System.out.println("Utente eliminato con successo!");
                                break;
                            }
                        }
                        wf.removedUser();
                        System.out.println("Utenti eliminati con successo!");

                        // Elimina l'utente dal database
                        String query = "DELETE FROM users WHERE id = ?";
                        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
                            stmt.setInt(1, idUSER);
                            stmt.executeUpdate();
                            System.out.println("Utente eliminato con successo dal database!");
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }

                    // Aggiungi utente
                    case 11 -> {
                        System.out.print("Inserisci il nome dell'utente da aggiungere: ");
                        String newUsername = scanner.nextLine();
                        if (!Pattern.matches("^[a-zA-Z\\s]{3,50}$", newUsername)) {
                            System.out.println("Nome utente non valido. Riprova.");
                            break;
                        }

                        System.out.print("Inserisci il ruolo dell'utente da aggiungere: ");
                        String newUserRole = scanner.nextLine();
                        if (!Pattern.matches("^[a-zA-Z\\s]{3,30}$", newUserRole)) {
                            System.out.println("Ruolo utente non valido. Riprova.");
                            break;
                        }

                        System.out.print("Inserisci l'email dell'utente da aggiungere: ");
                        String newUserEmail = scanner.nextLine();
                        if (!Pattern.matches("^[\\w\\.-]+@[a-zA-Z\\d\\.-]+\\.[a-zA-Z]{2,}$", newUserEmail)) {
                            System.out.println("Email non valida. Riprova.");
                            break;
                        }

                        // Mostra la legenda per la seniority
                        System.out.println("Seleziona la seniority dell'utente (scegli una delle seguenti opzioni):");
                        System.out.println("1. Junior");
                        System.out.println("2. Mid");
                        System.out.println("3. Senior");
                        System.out.println("4. Entry Level");
                        System.out.println("5. Manager");
                        System.out.println("6. Director");

                        System.out.print("Inserisci la seniority dell'utente da aggiungere: ");
                        String newUserSeniority = scanner.nextLine();

                        if (!Pattern.matches("^(Junior|Mid|Senior|Entry Level|Manager|Director)$", newUserSeniority)) {
                            System.out.println("Seniority non valida. Riprova.");
                            break;
                        }

                        int newUserID = wf.getUsers().size() + 1;
                        User newUser = new User(newUserID, newUsername, newUserRole, newUserEmail, newUserSeniority);
                        wf.addUser(newUser);
                        System.out.println("Utente aggiunto con successo!");

                        // Aggiungi l'utente al database
                        UserDAO userDAO = new UserDAO();
                        userDAO.addUserToDatabase(newUser);
                        break;
                    }

                    case 0 -> {
                        System.out.println("Arrivederci!");
                        return;
                    }
                }
            }

        }

    }

}
