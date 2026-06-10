package entity;
import java.util.Map;

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

    public boolean esisteEsercizio(String nome) {

        //Controllo se l'esercizio con quel nome è già presente nel DB (è unique)

        GestorePersistenza gp = new GestorePersistenza();
        Esercizio esistente = gp.cercaPrimoPerCampi(
                Esercizio.class,
                Map.of("nome", nome)
        );
        return esistente != null;
    }

    public boolean creaNuovoEsercizio(String nome, String descrizione) {
        Esercizio esistente = esisteEsercizio(nome);
        if (esistente != null) {
            return false;
        }
        //Creo istanza nuovo esercizio se non è già presente un ese con quel nome
        Esercizio nuovoEsercizio = new Esercizio(nome, descrizione);
        GestorePersistenza gp = new GestorePersistenza();

        //true se creato false se non creato
        return gp.salva(nuovoEsercizio);
    }


}
