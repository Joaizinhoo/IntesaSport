package entity;

import database.GestorePersistenza;

import java.util.List;

public class GestoreUtenti {
    private GestorePersistenza gp;



    public GestoreUtenti(){
        this.gp = new GestorePersistenza();
    }

    public Atleta ricercaAtletaPerEmail(String email){
        return this.gp.trovaPerEmail(Atleta.class, email);
    }

    public List<Atleta> visualizzaAtletiAssociati(String emailAllenatore) {

        database.GestorePersistenza gp = new database.GestorePersistenza();

        entity.Allenatore allenatoreLoggato = gp.trovaPerEmail(entity.Allenatore.class, emailAllenatore);
        if (allenatoreLoggato != null) {
            return new java.util.ArrayList<>(allenatoreLoggato.getAtletiAssociati());
        }
        //Ritorna lista vuota se non c'è l'allenatore loggato per evitare exception points
        return new java.util.ArrayList<>();
    }

}
