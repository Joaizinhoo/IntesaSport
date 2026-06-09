package entity;

import database.GestorePersistenza;

public class GestoreEsercizi {
    private GestorePersistenza gp;

    public GestoreEsercizi(){
        this.gp = new GestorePersistenza();
    }

    public DettaglioEsercizio trovaDettaglioExPerId(Long id){
        DettaglioEsercizio dettEx = gp.trovaPerId(DettaglioEsercizio.class, id);
        return dettEx;
    }
}
