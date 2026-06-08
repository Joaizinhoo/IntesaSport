package controller;

import entity.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class IntesaSport {

    public static List<String[]> visualizzaSessioniAssegnate(String email, Date data, StatoSessione stato, String disciplina){
        List<String[]> righeTabella = new ArrayList<>();

        GestoreUtenti gutenti = new GestoreUtenti();

        Atleta atleta = gutenti.ricercaAtletaPerEmail(email);

        List<SessioneDTO> dtoList = atleta.visualizzaSessioniAssegnate(data, stato, disciplina);

        for (SessioneDTO sessione: dtoList){
            for(EsercizioDettaglioDTO esercizio: sessione.getEsercizi()){
                String[] riga = new String[]{
                        sessione.getIdSessione().toString(),
                        sessione.getTitolo(),
                        sessione.getDescrizione(),
                        sessione.getData().toString(),
                        sessione.getDurataPrevista().toString(),
                        sessione.getStatoSessione().toString(),
                        esercizio.getDescrizioneEx(),
                        esercizio.getNomeEx(),
                        esercizio.getDurata().toString(),
                        String.valueOf(esercizio.getRipetizioni())
                };

                righeTabella.add(riga);
            }
        }

        return righeTabella;
    }
}
