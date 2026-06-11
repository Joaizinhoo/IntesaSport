package controller;

import database.GestorePersistenza;
import entity.*;

import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.List;

public class IntesaSport {

    public static List<String[]> visualizzaSessioniAssegnate(String email, String data, String stato, String disciplina){
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

        LocalDate dataFiltro = null; //converte la data da stringa a data vera e propria
        if (data != null && !data.trim().isEmpty()) {
            try {
                DateTimeFormatter formattatore = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                dataFiltro = LocalDate.parse(data, formattatore);
            } catch (DateTimeParseException ex) {
                return new ArrayList<>(); //ritorna se la data è scritta male
            }
        }

        List<SessioneDTO> dtoList = atleta.visualizzaSessioniAssegnate(dataFiltro, statoEnum, disciplinaFiltro);

        for (SessioneDTO sessione: dtoList){
            for(EsercizioDettaglioDTO esercizio: sessione.getEsercizi()){
                String[] riga = new String[]{
                        sessione.getIdSessione().toString(),
                        sessione.getTitolo(),
                        sessione.getDescrizione(),
                        sessione.getData().toString(),
                        sessione.getDurataPrevista().toNanos() + " minuti",
                        sessione.getStatoSessione().toString(),
                        esercizio.getId().toString(),
                        esercizio.getDescrizioneEx(),
                        esercizio.getNomeEx(),
                        esercizio.getDurata().toNanos() + " minuti",
                        String.valueOf(esercizio.getRipetizioni())
                };

                righeTabella.add(riga);
            }
        }

        return righeTabella;
    }

    public static boolean registraRisultatiEsercizio(Long idDettaglioEx, Integer ripEff, Duration durataEff, String note){

        GestoreEsercizi gEx = new GestoreEsercizi();
        DettaglioEsercizio dettEx = gEx.trovaDettaglioExPerId(idDettaglioEx);

        if (dettEx == null) {
            return false;
        }

        return dettEx.creaPrestazione(ripEff, durataEff, note);

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

    public static boolean creaNuovaSessione(Atleta atleta, LocalDate data, int durata, String descrizione, List<Object[]> dettagliScelti) {

        // controlli sui dari
        if (atleta == null) {
            return false;
        }

        if (data == null) {
            return false;
        }

        if (dettagliScelti == null || dettagliScelti.isEmpty()) {
            return false;
        }

        GestorePersistenza gp = new GestorePersistenza();

        Allenatore allenatoreLoggato = gp.trovaPerEmail(Allenatore.class, "mario.rossi@sport.it");
        if (allenatoreLoggato == null) {
            return false;
        }

        // Assegno i valori alla sessione allenamento
        SessioneAllenamento nuovaSessione = new SessioneAllenamento();
        nuovaSessione.setAtleta(atleta);
        nuovaSessione.setAllenatore(allenatoreLoggato);
        nuovaSessione.setDate(data);
        nuovaSessione.setDurataPrevista(Duration.ofMinutes(durata));
        nuovaSessione.setDescrizione(descrizione);
        nuovaSessione.setStatoSessione(StatoSessione.ASSEGNATA);

        // associo i dettagli allenamento
        for (Object[] dato : dettagliScelti) {
            Esercizio e = (Esercizio) dato[0];
            int ripetizioni = (int) dato[1];
            int minuti = (int) dato [2];
            nuovaSessione.creaDettaglioEsercizio(e, minuti, ripetizioni);
        }

        return gp.salvaTutti(nuovaSessione);
    }


}
