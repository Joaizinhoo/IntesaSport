package entity;

import database.GestorePersistenza;
import jakarta.persistence.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class SessioneAllenamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titolo;
    private String descrizione;
    private Date date;
    private Duration durataPrevista;

    @Enumerated(EnumType.STRING)
    private StatoSessione statoSessione;

    @ManyToOne
    @JoinColumn(name = "allenatore_id")
    private Allenatore allenatore;

    @ManyToOne
    @JoinColumn(name = "atleta_id")
    private Atleta atleta;

    @OneToMany(mappedBy = "sessioneAllenamento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DettaglioEsercizio> dettaglioEsercizi = new ArrayList<>();

    public List<EsercizioDettaglioDTO> getListaEserciziSessione(){
        GestorePersistenza gp = new GestorePersistenza();

        List<EsercizioDettaglioDTO> dtoList = new ArrayList<>();

        for (DettaglioEsercizio dettaglio: this.dettaglioEsercizi){
            Esercizio esercizio = dettaglio.getEsercizio();

            EsercizioDettaglioDTO dto = new EsercizioDettaglioDTO(
                    dettaglio.getRipetizioni(),
                    dettaglio.getDurata(),
                    esercizio.getNome(),
                    esercizio.getDescrizione()
            );

            dtoList.add(dto);
        }

        return dtoList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Duration getDurataPrevista() {
        return durataPrevista;
    }

    public void setDurataPrevista(Duration durataPrevista) {
        this.durataPrevista = durataPrevista;
    }

    public StatoSessione getStatoSessione() {
        return statoSessione;
    }

    public void setStatoSessione(StatoSessione statoSessione) {
        this.statoSessione = statoSessione;
    }

    public Allenatore getAllenatore() {
        return allenatore;
    }

    public void setAllenatore(Allenatore allenatore) {
        this.allenatore = allenatore;
    }

    public Atleta getAtleta() {
        return atleta;
    }

    public void setAtleta(Atleta atleta) {
        this.atleta = atleta;
    }

    @Override
    public String toString() {
        return "SessioneAllenamento{" +
                "id=" + id +
                ", titolo='" + titolo + '\'' +
                ", descrizione='" + descrizione + '\'' +
                ", date=" + date +
                ", durataPrevista=" + durataPrevista +
                ", statoSessione=" + statoSessione +
                ", allenatore=" + allenatore +
                ", atleta=" + atleta +
                '}';
    }
    public void aggiungiDettaglioEsercizio(DettaglioEsercizio dettaglio) {
        if (dettaglio == null) {
            throw new IllegalArgumentException("Dettaglio esercizio non valido");
        }

        dettaglio.setSessioneAllenamento(this);
        this.dettaglioEsercizi.add(dettaglio);
    }
    public List<DettaglioEsercizio> getDettaglioEsercizi() {
        return dettaglioEsercizi;
    }


}
