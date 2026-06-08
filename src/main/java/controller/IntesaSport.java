package controller;

import entity.GestoreUtenti;
import entity.SessioneDTO;
import entity.Atleta;
import entity.StatoSessione;

import java.util.Date;
import java.util.List;

public class IntesaSport {

    public static List<String[]> visualizzaSessioniAssegnate(String email, Date data, StatoSessione stato, String disciplina){

        GestoreUtenti gutenti = new GestoreUtenti();

        Atleta atleta = gutenti.ricercaAtletaPerEmail(email);

        List<SessioneDTO> dtoList = atleta.visualizzaSessioniAssegnate(data, stato, disciplina);

        //DA FINIRE IMPLEMENTAZIONE PER PASSARE VISIVAMENTE ALLA GUI
    }
}
