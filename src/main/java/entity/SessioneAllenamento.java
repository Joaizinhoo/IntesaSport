package entity;

import database.GestorePersistenza;
import jakarta.persistence.*;

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
    private int durataPrevista;

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

        DettaglioEsercizio nuovoDettaglio = new DettaglioEsercizio(this, ese, durata, ripetizioni);

        //Se per qualche modo la lista dei DettaglioEsercizio non è stata inizializzata applico questa prefcauzione
        if (this.dettaglioEsercizi == null) {
            this.dettaglioEsercizi = new ArrayList<DettaglioEsercizio>();
        }
        this.dettaglioEsercizi.add(nuovoDettaglio);
        return true;
    }

    public void setDettaglioEsercizi(List<DettaglioEsercizio> dettaglioEsercizi) {
        this.dettaglioEsercizi = dettaglioEsercizi;
    }

    public List<DettaglioEsercizio> getDettaglioEsercizi() {
        return dettaglioEsercizi;
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

    public int getDurataPrevista() {
        return durataPrevista;
    }

    public void setDurataPrevista(int durataPrevista) {
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

    public DettaglioEsercizio trovaDettaglioExPerId(Long idDettaglioEx) {
        if (idDettaglioEx == null) {
            return null;
        }

        for (DettaglioEsercizio dettaglio : this.dettaglioEsercizi) {

            if (dettaglio.getId() != null && dettaglio.getId().equals(idDettaglioEx)) {
                return dettaglio;
            }
        }

        return null;
    }

    public void aggiornaStato(StatoSessione stato){
        GestorePersistenza gp = new GestorePersistenza();
        this.setStatoSessione(stato);

        gp.aggiorna(this);
    }

    public boolean sessioneCompleta() {
        if (this.dettaglioEsercizi.isEmpty()) {
            return false;
        }

        for (DettaglioEsercizio dett : this.dettaglioEsercizi) {
            Prestazione p = dett.getPrestazione();

            if (p == null) {
                return false;
            }

            if (!p.prestazioneCompleta()) {
                return false;
            }
        }

        return true;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SessioneAllenamento that)) return false;
        if (this.id == null || that.getId() == null) return false;
        return java.util.Objects.equals(id, that.getId());
    }

    @Override
    public int hashCode() {
        if (id != null) {
            return java.util.Objects.hash(id);
        }
        return super.hashCode();
    }

}
