package entity;

import java.time.LocalDate;
import java.util.List;

public class SessioneDTO {
    private Long idSessione;
    private String titolo;
    private String descrizione;
    private LocalDate data;
    private int durataPrevista; // Cambiato da Duration a int (minuti)
    private StatoSessione statoSessione;
    private List<EsercizioDettaglioDTO> esercizi;

    public SessioneDTO(Long idSessione, String titolo, String descrizione, LocalDate data, int durataPrevista, StatoSessione statoSessione, List<EsercizioDettaglioDTO> esercizi) {
        this.idSessione = idSessione;
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.data = data;
        this.durataPrevista = durataPrevista;
        this.statoSessione = statoSessione;
        this.esercizi = esercizi;
    }

    public List<EsercizioDettaglioDTO> getEsercizi() {
        return esercizi;
    }

    public int getDurataPrevista() { // Cambiato tipo di ritorno in int
        return durataPrevista;
    }

    public LocalDate getData() {
        return data;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public String getTitolo() {
        return titolo;
    }

    public StatoSessione getStatoSessione() {
        return statoSessione;
    }

    public Long getIdSessione() {
        return idSessione;
    }
}