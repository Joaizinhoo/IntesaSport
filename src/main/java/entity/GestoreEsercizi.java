package entity;
import java.util.*;

import database.GestorePersistenza;

public class GestoreEsercizi {
    private GestorePersistenza gp;

    public GestoreEsercizi(){
        this.gp = new GestorePersistenza();
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
        boolean esistente = esisteEsercizio(nome);
        if (esistente) {
            return false;
        }
        //Creo istanza nuovo esercizio se non è già presente un ese con quel nome
        Esercizio nuovoEsercizio = new Esercizio(nome, descrizione);
        GestorePersistenza gp = new GestorePersistenza();

        //true se creato false se non creato
        return gp.salva(nuovoEsercizio);
    }

    public List<Esercizio> visualizzaListaEsercizi() {
        GestorePersistenza gp = new GestorePersistenza();
        List<Esercizio> listaEsercizi = gp.cercaPerCampi(Esercizio.class, new java.util.HashMap<>());
        Collections.sort(listaEsercizi);
        return listaEsercizi;
    }


}
