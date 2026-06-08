package entity;

import database.GestorePersistenza;

public class GestoreUtenti {
    private GestorePersistenza gp;

    public GestoreUtenti(){
        this.gp = new GestorePersistenza();
    }

    public Atleta ricercaAtletaPerEmail(String email){
        return this.gp.trovaPerEmail(Atleta.class, email);
    }

}
