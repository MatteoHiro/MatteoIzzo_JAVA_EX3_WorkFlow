
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {
        // final String workState1 = "Messo in produzione";
        // final String workState2 = "In fase di revisione";
        // final String workState3 = "Completato";
        // final String workState4 = "Rifiutato";

        // User user1 = new User(1, "Mario Rossi", "Developer", "Mrossi@example.com", "Junior");
        // Document doc1 = new Document(1, "Documento 1", workState1, LocalDateTime.now(), LocalDateTime.now());
        WorkFlow wf = new WorkFlow();

        // // Stato iniziale
        // wf.addDocument(doc1, user1);
        // wf.addUser(user1);
        // System.out.println("Utente: " + user1);
        // System.out.println("Stato iniziale Progetto: " + doc1);
        // // Primo cambio di stato
        // doc1.setState(workState1);
        // wf.addLogDocument(doc1);
        // System.out.println("Aggiornamento: Stato cambiato a '" + workState1 + "'");
        // // Secondo cambio di stato
        // doc1.setState(workState2);
        // wf.addLogDocument(doc1);
        // System.out.println("Aggiornamento: Stato cambiato a '" + workState2 + "'");
        // // Terzo cambio di stato
        // doc1.setState(workState3);
        // wf.addLogDocument(doc1);
        // System.out.println("Aggiornamento: Stato cambiato a '" + workState3 + "'");
        // // Rifiuto del documento
        // doc1.setState(workState4);
        // wf.addLogDocument(doc1);
        // System.out.println("Aggiornamento: Documento rifiutato");
        // // Mostra il log dei cambiamenti
        // System.out.println("\nMemoria dei log:");
        // for (Document logDoc : wf.getLogDocuments()) {
        //     System.out.println(logDoc);
        // }
        // Chiamata al metodo per il MENU UTENTE
        // Decommentare per utilizzarlo da terminale
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
                            if (!found) {
                                System.out.println("ID non associato a nessun documento!");
                                break;
                            }

                        }

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

                            if (!found) {
                                System.out.println("ID non associato a nessun documento!");
                                break;
                            }
                        }
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

                        boolean documentFound = false;
                        for (Document doc : wf.getDocuments()) {
                            if (doc.getId() == idSTATE) {
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
                                System.out.println("Stato aggiornato con successo!");
                                break;
                            }
                        }

                        if (!documentFound) {
                            System.out.println("ID documento non trovato!");
                        }
                    }

                    case 5 -> {
                        // Aggiungi un nuovo documento con ID che incrementa
                        // in base al numero di documenti presente in modo da non avere due id uguali
                        int newId = wf.getDocuments().size() + 1;

                        System.out.print("Inserisci l'ID dell'utente che ha creato il documento");
                        int newUserId = scanner.nextInt();
                        scanner.nextLine();

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
                            continue; // Torna al menu senza aggiungere il documento
                        }

                        if (newProductionDate.isAfter(LocalDateTime.now())) {
                            System.out.println("La data di produzione non può essere nel futuro!");
                            continue;
                        }

                        Document newDoc = new Document(newId, newName, newState, newProductionDate, LocalDateTime.now());
                        wf.addDocument(newDoc, foundUser);
                        wf.addLogDocument(newDoc);
                        System.out.println("Documento aggiunto con successo!");
                    }

                    case 6 -> {
                        // Conferma prima di eliminare
                        System.out.print("Sei sicuro di voler eliminare questo documento? (s/n): ");
                        String confirmation = scanner.nextLine().toLowerCase();
                        if (!confirmation.equals("s")) {
                            System.out.println("Operazione annullata.");
                            continue;
                        }

                        // Richiesta ID documento da eliminare
                        System.out.print("Inserisci l'ID del documento da eliminare: ");
                        int idDELETE;
                        try {
                            idDELETE = scanner.nextInt();
                            scanner.nextLine();
                            if (idDELETE <= 0) {
                                System.out.println("L'ID deve essere un numero positivo!");
                                continue;
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Errore: l'ID deve essere un numero!");
                            scanner.nextLine(); // Consuma l'input errato
                            continue;
                        }

                        boolean found = false;
                        for (Document doc : wf.getDocuments()) {
                            if (doc.getId() == idDELETE) {
                                wf.removeDocument(doc);
                                wf.addLogDocument(doc);
                                System.out.println("Documento eliminato con successo!");
                                found = true;
                                break;
                            }
                        }

                        if (!found) {
                            System.out.println("ID non associato a nessun documento!");
                        }
                    }

                    case 7 -> {
                        // Elimina tutti i documenti
                        wf.getDocuments().clear();
                        wf.getLogDocuments().clear();
                        System.out.println("Tutti i documenti sono stati eliminati!");
                    }

                    case 8 -> {
                        // Visualizza gli utenti
                        System.out.println("Lista degli utenti:");
                        for (User user : wf.getUsers()) {
                            System.out.println(user);
                        }
                    }

                    case 9 -> {
                        // Modifica informazioni utente
                        System.out.print("Inserisci l'ID dell'utente da modificare: ");
                        int idUSER = scanner.nextInt();
                        scanner.nextLine();
                        boolean found = false;
                        for (Document userDoc : wf.getDocuments()) {
                            if (userDoc.getId() == idUSER) {
                                found = true;
                            }

                            if (found) {
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
                                        userDoc.getUser().setUsername(newUsername);
                                        System.out.println("Username aggiornato con successo!");
                                    }

                                    case 2 -> {
                                        System.out.print("Inserisci il nuovo ruolo: ");
                                        String newRole = scanner.nextLine();
                                        userDoc.getUser().setRole(newRole);
                                        System.out.println("Ruolo aggiornato con successo!");
                                    }

                                    case 3 -> {
                                        System.out.print("Inserisci la nuova email: ");
                                        String newEmail = scanner.nextLine();
                                        userDoc.getUser().setEmail(newEmail);
                                        System.out.println("Email aggiornata con successo!");
                                    }

                                    case 4 -> {
                                        System.out.print("Inserisci la nuova seniority: ");
                                        String newSeniority = scanner.nextLine();
                                        userDoc.getUser().setSeniority(newSeniority);
                                        System.out.println("Seniority aggiornata con successo!");
                                    }
                                }
                            }

                            if (!found) {
                                System.out.println("ID non associato a nessun utente!");
                                break;
                            }
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
                        wf.removeUser();
                        System.out.println("Utenti eliminati con successo!");
                    }

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
