
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        final String workState1 = "Messo in produzione";
        final String workState2 = "In fase di revisione";
        final String workState3 = "Completato";
        final String workState4 = "Rifiutato";

        Document doc1 = new Document(1, "Documento 1", workState1, LocalDateTime.now().minusDays(1), LocalDateTime.now().minusHours(1));
        WorkFlow wf = new WorkFlow();

        // Stato iniziale
        wf.addDocument(doc1);
        System.out.println("Stato iniziale Progetto: " + doc1);

        // Primo cambio di stato
        doc1.setState(workState1);
        wf.addLogDocument(doc1);
        System.out.println("Aggiornamento: Stato cambiato a '" + workState1 + "'");

        // Secondo cambio di stato
        doc1.setState(workState2);
        wf.addLogDocument(doc1);
        System.out.println("Aggiornamento: Stato cambiato a '" + workState2 + "'");

        // Terzo cambio di stato
        doc1.setState(workState3);
        wf.addLogDocument(doc1);
        System.out.println("Aggiornamento: Stato cambiato a '" + workState3 + "'");

        // Rifiuto del documento
        doc1.setState(workState4);
        wf.addLogDocument(doc1);
        System.out.println("Aggiornamento: Documento rifiutato");

        // Mostra il log dei cambiamenti
        System.out.println("\nMemoria dei log:");
        for (Document logDoc : wf.getLogDocuments()) {
            System.out.println(logDoc);
        }

        // Chiamata al metodo per il MENU UTENTE
        // Decommentare per utilizzarlo da terminale
        // menu(wf);
    }

    // Menu utente 

    public static void menu(WorkFlow wf) {

        try (var scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("\nMenu:");
                System.out.println("1. Visualizza tutti i documenti");
                System.err.println("2. Cerca un documento tramite ID");
                System.out.println("3. Visualizza il log dei cambiamenti tramitre l'ID del documento");
                System.out.println("4. Aggiorna lo stato di un documento");
                System.out.println("5. Aggiungi un nuovo documento");
                System.out.println("0. Esci");
                System.out.print("Digita il numero corrispondete all'azione desiderata: ");
                int choice = scanner.nextInt();
                scanner.nextLine();

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
                        for (Document doc : wf.getDocuments()) {
                            if (doc.getId() == id) {
                                System.out.println(doc);
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

                            if(!found){
                                System.out.println("ID non associato a nessun documento!");
                                break;
                            }
                        }
                    }
                    case 4 -> {
                        // Aggiorna lo stato di un documento
                        System.out.print("Inserisci l'ID del documento da aggiornare: ");
                        int idSTATE = scanner.nextInt();
                        scanner.nextLine();
                        for (Document doc : wf.getDocuments()) {
                            if (doc.getId() == idSTATE) {
                                System.out.print("Inserisci il nuovo stato del documento: ");
                                String newState = scanner.nextLine();
                                doc.setState(newState);
                                wf.addLogDocument(doc);
                                System.out.println("Stato aggiornato con successo!");
                                break;
                            }
                        }
                    }
                    case 5 -> {
                        // Aggiungi un nuovo documento con ID che incrementa
                        // in base al numero di documenti presente in modo da non avere due id uguali
                        int newId = wf.getDocuments().size() + 1;
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
                        Document newDoc = new Document(newId, newName, newState, newProductionDate, LocalDateTime.now());
                        wf.addDocument(newDoc);
                        wf.addLogDocument(newDoc);
                        System.out.println("Documento aggiunto con successo!");
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
