package entity;

import database.GestorePersistenza;
import jakarta.persistence.*;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class SessioneAllenamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titolo;
    private String descrizione;
    private LocalDate date;
    private Duration durataPrevista;

    @Enumerated(EnumType.STRING)
    private StatoSessione statoSessione;

    @ManyToOne
    @JoinColumn(name = "allenatore_id")
    private Allenatore allenatore;

    @ManyToOne
    @JoinColumn(name = "atleta_id")
    private Atleta atleta;

    @OneToMany(mappedBy = "sessioneAllenamento", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
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
                    esercizio.getDescrizione(),
                    dettaglio.getId()
            );

            dtoList.add(dto);
        }

        return dtoList;
    }

    public void aggiornaStato(StatoSessione stato){
        GestorePersistenza gp = new GestorePersistenza();
        this.setStatoSessione(stato);

        gp.aggiorna(this);
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
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
    public boolean creaDettaglioEsercizio(Esercizio ese, int durata, int ripetizioni) {

        // CONTROLLI SUI DATI

        if(ese == null)
            return false;

        //Basta che solo uno dei due valori sia settato, e visto che la ripetizione
        //per qualsiasi esercizio è sempre almeno 1 controllo che sia almeno ==1

        if(durata<0||ripetizioni<1)
            return false;

        //Se la durata è maggiore di 5 ore
        if(durata>300)
            return false;

        // FINE CONTROLLI SUI DATI

        java.time.Duration durataConvertita = java.time.Duration.ofMinutes(durata);
        DettaglioEsercizio nuovoDettaglio = new DettaglioEsercizio(this, ese, durataConvertita, ripetizioni);

        //Se per qualche modo la lista dei DettaglioEsercizio non è stata inizializzata applico questa prefcauzione
        if (this.dettaglioEsercizi == null) {
            this.dettaglioEsercizi = new ArrayList<DettaglioEsercizio>();
        }
        this.dettaglioEsercizi.add(nuovoDettaglio);
        return true;
    }
    public List<DettaglioEsercizio> getDettaglioEsercizi() {
        return dettaglioEsercizi;
    }



}
