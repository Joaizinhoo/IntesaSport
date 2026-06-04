package eseguibili;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class CreaTabelle {


    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("intesasport");

        emf.close();

        System.out.println("Ho avviato Hibernate");
    }
}
