package controller;

import entity.*;

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
}
