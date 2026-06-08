package controller;

import entity.DettaglioEsercizio;
import entity.Esercizio;
import entity.SessioneAllenamento;

import java.time.Duration;

public class GestioneSessioneControl {

    public Esercizio creaEsercizio(String nome, String descrizione) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome esercizio non valido");
        }

        Esercizio esercizio = new Esercizio();
        esercizio.setNome(nome);
        esercizio.setDescrizione(descrizione);

        return esercizio;
    }

    public void aggiungiEsercizio(
            SessioneAllenamento sessione,
            Esercizio esercizio,
            Integer ripetizioniPreviste,
            Duration durataPrevista
    ) {
        if (sessione == null) {
            throw new IllegalArgumentException("Sessione non valida");
        }

        if (esercizio == null) {
            throw new IllegalArgumentException("Esercizio non valido");
        }

        if (ripetizioniPreviste == null && durataPrevista == null) {
            throw new IllegalArgumentException("Inserire ripetizioni o durata");
        }

        DettaglioEsercizio dettaglio = new DettaglioEsercizio();
        dettaglio.setSessioneAllenamento(sessione);
        dettaglio.setEsercizio(esercizio);

        if (ripetizioniPreviste != null) {
            dettaglio.setRipetizioni(ripetizioniPreviste);
        }

        dettaglio.setDurata(durataPrevista);

        sessione.aggiungiDettaglioEsercizio(dettaglio);
    }
}


