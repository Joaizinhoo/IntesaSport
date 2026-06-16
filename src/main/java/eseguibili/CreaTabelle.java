package eseguibili;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class CreaTabelle {


    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("intesasport");

        emf.close();

        PopolaDBIntesaSport DB = new PopolaDBIntesaSport();
        DB.popolaDB();

        System.out.println("Create tabelle e popolato il database! Pronto all'utilizzo.");
    }
}
