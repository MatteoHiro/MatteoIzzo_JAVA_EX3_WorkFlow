import java.time.LocalDateTime;

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
    }

    }

