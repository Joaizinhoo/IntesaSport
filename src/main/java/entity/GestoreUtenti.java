package entity;

import database.GestorePersistenza;

import java.util.List;
import java.util.ArrayList;

public class GestoreUtenti {
    private GestorePersistenza gp;



    public GestoreUtenti(){
        this.gp = new GestorePersistenza();
    }

    public Atleta ricercaAtletaPerEmail(String email){
        return this.gp.trovaPerEmail(Atleta.class, email);
    }

    public List<Atleta> visualizzaAtletiAssociati(String emailAllenatore) {

       GestorePersistenza gp = new GestorePersistenza();

        Allenatore allenatoreLoggato = gp.trovaPerEmail(Allenatore.class, emailAllenatore);
        if (allenatoreLoggato != null) {
            return new ArrayList<>(allenatoreLoggato.getAtletiAssociati());
        }
        //Ritorna lista vuota se non c'è l'allenatore loggato per evitare exception points
        return new ArrayList<>();
    }

}
