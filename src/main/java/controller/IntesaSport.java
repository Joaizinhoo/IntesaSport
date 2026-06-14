package controller;

import DTO.EsercizioDettaglioDTO;
import DTO.SessioneDTO;
import database.GestorePersistenza;
import entity.*;

import java.util.ArrayList;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class IntesaSport {

    public static List<String[]> visualizzaSessioniAssegnate(String email, LocalDate data, String stato, String disciplina){
        List<String[]> righeTabella = new ArrayList<>();

        GestoreUtenti gutenti = new GestoreUtenti();

        Atleta atleta = gutenti.ricercaAtletaPerEmail(email);

        if (atleta == null){
            return righeTabella;
        }

        String disciplinaFiltro = (disciplina == null || disciplina.trim().isEmpty()) ? null : disciplina.trim();

        StatoSessione statoEnum = null;

        switch (stato) {
            case "Assegnata":
                statoEnum = StatoSessione.ASSEGNATA;
                break;
            case "Completata":
                statoEnum = StatoSessione.COMPLETATA;
                break;
            case "In corso":
                statoEnum = StatoSessione.IN_CORSO;
                break;
            default:
                break;
        }

        List<SessioneDTO> dtoList = atleta.visualizzaSessioniAssegnate(data, statoEnum, disciplinaFiltro);

        for (SessioneDTO sessione: dtoList){
            for(EsercizioDettaglioDTO esercizio: sessione.getEsercizi()){
                String[] riga = new String[]{
                        sessione.getIdSessione().toString(),
                        sessione.getTitolo(),
                        sessione.getDescrizione(),
                        sessione.getData().toString(),
                        sessione.getDurataPrevista() + " minuti",
                        sessione.getStatoSessione().toString(),
                        esercizio.getId().toString(),
                        esercizio.getDescrizioneEx(),
                        esercizio.getNomeEx(),
                        esercizio.getDurata() + " minuti",
                        String.valueOf(esercizio.getRipetizioni())
                };

                righeTabella.add(riga);
            }
        }

        return righeTabella;
    }

    public static boolean registraRisultatiEsercizio(String emailAtleta, Long idDettaglioEx, Integer ripEff, Integer durataEff, String note){
        GestoreUtenti gu = new GestoreUtenti();
        Atleta atleta = gu.ricercaAtletaPerEmail(emailAtleta);

        if (atleta == null){
            System.out.println("BLOCCO: Atleta non trovato!");
            return false;
        }

        SessioneAllenamento sessione = atleta.getSessionePerDettaglioEx(idDettaglioEx);

        if(sessione == null){
            System.out.println("BLOCCO: Sessione non trovato!");
            return false;
        }

        DettaglioEsercizio dettEx = sessione.trovaDettaglioExPerId(idDettaglioEx);

        if(dettEx == null){
            System.out.println("BLOCCO: dettex non trovato!");
            return false;
        }

        /*Prestazione prestazioneEsistente = dettEx.getPrestazione(); //CONTROLLO SULLA PRESTAZIONE CHE NON SIA GIA COMPLETATA
        if (prestazioneEsistente != null && prestazioneEsistente.prestazioneCompleta()) {
            System.out.println("BLOCCO: Prestazione completa!");
            return false;
        }*/

        boolean risultato = dettEx.creaPrestazione(ripEff, durataEff, note);

        if (risultato){ // SERVE PER GESTIRE LO STATO DELLA PRESTAZIONE

            if (sessione.sessioneCompleta()) {
                sessione.aggiornaStato(StatoSessione.COMPLETATA);
            }
            else {
                sessione.aggiornaStato(StatoSessione.IN_CORSO);
            }
        }

        return risultato;
    }

    //Metodo con il quale il controller andrà a richiamare il creaNuovoEsercizio del gestoreEsercizi, Information Expert di esercizi

    public static boolean creaEsercizio (String nome, String descrizione){

        GestoreEsercizi ges = new GestoreEsercizi();
        return ges.creaNuovoEsercizio(nome, descrizione);

    }

    //Il controller deve poter fornire un metodo alle boundary per poter popolare i menu di selezione degli esercizi

    public static List<Esercizio> visualizzaListaEsercizi() {
        GestoreEsercizi ges = new GestoreEsercizi();
        return ges.visualizzaListaEsercizi();
    }

    public static List<Atleta> visualizzaAtletiAssociati(){
        GestoreUtenti gu = new GestoreUtenti();
        return gu.visualizzaAtletiAssociati("mario.rossi@sport.it");
    }

    public static boolean creaNuovaSessione(SessioneDTO dto, String emailAtleta) {

        // controlli sui dati
        if(dto.getTitolo().isEmpty()){
            return false;
        }

        if (emailAtleta == null || emailAtleta.isEmpty()) {
            return false;
        }

        if (dto.getData() == null) {
            return false;
        }

        if (dto.getEsercizi() == null || dto.getEsercizi().isEmpty()) {
            return false;
        }

        GestorePersistenza gp = new GestorePersistenza();
        GestoreUtenti gu = new GestoreUtenti();

        // Se l'email inserita è di un allenatore ritorna errore
        Atleta atleta = gu.ricercaAtletaPerEmail(emailAtleta);
        if (atleta == null || !(atleta instanceof Atleta)) {
            return false;
        }

        Allenatore allenatoreLoggato = gp.trovaPerEmail(Allenatore.class, "mario.rossi@sport.it");
        if (allenatoreLoggato == null) {
            return false;
        }

        // Assegno i valori alla sessione allenamento
        SessioneAllenamento nuovaSessione = new SessioneAllenamento();
        nuovaSessione.setTitolo(dto.getTitolo());
        nuovaSessione.setAtleta(atleta);
        nuovaSessione.setAllenatore(allenatoreLoggato);
        nuovaSessione.setDate(dto.getData());
        nuovaSessione.setDurataPrevista(dto.getDurataPrevista());
        nuovaSessione.setDescrizione(dto.getDescrizione());
        nuovaSessione.setStatoSessione(StatoSessione.ASSEGNATA);

        // associo i dettagli allenamento
        for (EsercizioDettaglioDTO dettDTO : dto.getEsercizi()) {
            Esercizio e = gp.cercaPrimoPerCampi(Esercizio.class, Map.of("nome", dettDTO.getNomeEx()));
            if (e != null) {
                nuovaSessione.creaDettaglioEsercizio(e, dettDTO.getDurata(), dettDTO.getRipetizioni());
            }
        }

        return gp.salvaTutti(nuovaSessione);
    }


}
