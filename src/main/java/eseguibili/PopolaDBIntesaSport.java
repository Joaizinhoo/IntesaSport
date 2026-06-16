package eseguibili;

import entity.*;
import database.GestorePersistenza;
import java.time.LocalDate;

public class PopolaDBIntesaSport {

    GestorePersistenza gp = new GestorePersistenza();

    public void popolaDB(){
        try {
            // 1. ALLENATORI - ATLETI
            Allenatore mRossi = new Allenatore("mario.rossi@sport.it", "Mario", "Rossi", "Calcio e Atletica", 1001);
            Allenatore aVerdi = new Allenatore("anna.verdi@intesasport.it", "Anna", "Verdi", "Nuoto e Triathlon", 1002);

            Atleta lBianchi = new Atleta("luca.bianchi@email.it", "Luca", "Bianchi", null, "Calcio", null, "Migliorare lo scatto breve e la resistenza");
            Atleta eNeri = new Atleta("elena.neri@email.it", "Elena", "Neri", null, "Nuoto", null, "Preparazione per gara 100m stile libero");

            gp.salva(mRossi);
            gp.salva(aVerdi);

            // 2. ASSOCIAZIONI ATLETA-ALLENATORE
            lBianchi.getAllenatoriAssociati().add(mRossi);
            eNeri.getAllenatoriAssociati().add(mRossi);
            eNeri.getAllenatoriAssociati().add(aVerdi);

            gp.salva(lBianchi);
            gp.salva(eNeri);

            // 3. ESERCIZI
            Esercizio es1 = new Esercizio("Squat a corpo libero", "Esercizio per lo sviluppo della forza degli arti inferiori.");
            Esercizio es2 = new Esercizio("Scatto 50 metri", "Sprint alla massima velocità su pista rettilinea.");
            Esercizio es3 = new Esercizio("Ripetute 100m Stile Libero", "Sessione in vasca corta/lunga a ritmo gara.");
            Esercizio es4 = new Esercizio("Plank Addominale", "Isometria per il rinforzo del core.");

            gp.salva(es1);
            gp.salva(es2);
            gp.salva(es3);
            gp.salva(es4);

            // 4. SESSIONI ALLENAMENTO
            SessioneAllenamento sess1 = new SessioneAllenamento();
            sess1.setDate(LocalDate.parse("2026-06-18"));
            sess1.setTitolo("Potenziamento Gambe e Scatto");
            sess1.setDescrizione("Focus su forza esplosiva e velocità");
            sess1.setDurataPrevista(60);
            sess1.setStatoSessione(StatoSessione.ASSEGNATA);
            sess1.setAllenatore(mRossi);
            sess1.setAtleta(lBianchi);

            SessioneAllenamento sess2 = new SessioneAllenamento();
            sess2.setDate(LocalDate.parse("2026-06-16"));
            sess2.setTitolo("Soglia Aerobica Vasca");
            sess2.setDescrizione("Allenamento intensivo di resistenza in acqua");
            sess2.setDurataPrevista(45);
            sess2.setStatoSessione(StatoSessione.COMPLETATA);
            sess2.setAllenatore(aVerdi);
            sess2.setAtleta(eNeri);

            // 5. DETTAGLI ESERCIZIO

            // Dettagli Sessione 1 (Luca Bianchi)
            sess1.creaDettaglioEsercizio(es1, 10, 4);
            sess1.creaDettaglioEsercizio(es2, 15, 3);

            // Dettagli Sessione 2 (Elena Neri)
            sess2.creaDettaglioEsercizio(es3, 30, 6);
            sess2.creaDettaglioEsercizio(es4, 5, 4);

            // 6. SALVATAGGIO DELLE SESSIONI
            gp.salva(sess1);
            gp.salva(sess2);

            System.out.println("Database popolato con successo");

        } catch (Exception e) {
            System.err.println("Errore durante il popolamento del database:");
            e.printStackTrace();
        }
    }
}