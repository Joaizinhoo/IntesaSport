package entity;

import java.time.Duration;
import java.util.Date;
import java.util.List;

public class SessioneDTO {
    private Long idSessione;
    private String titolo;
    private String descrizione;
    private Date data;
    private Duration durataPrevista;
    private StatoSessione statoSessione;
    private List<EsercizioDettaglioDTO> esercizi;

    public SessioneDTO(Long idSessione, String titolo, String descrizione, Date data, Duration durataPrevista, StatoSessione statoSessione, List<EsercizioDettaglioDTO> esercizi) {
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

    public Duration getDurataPrevista() {
        return durataPrevista;
    }

    public Date getData() {
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
