package entity;

import jakarta.persistence.*;

import java.time.Duration;

@Entity
public class DettaglioEsercizio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer durata;
    private int ripetizioni;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sessione_id")
    private SessioneAllenamento sessioneAllenamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "esercizio_id")
    private Esercizio esercizio;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "prestazione_id")
    private Prestazione prestazione;

    public DettaglioEsercizio() {}

    public DettaglioEsercizio(SessioneAllenamento sessione, Esercizio esercizio, Duration durata, int ripetizioni) {
        this.sessioneAllenamento = sessione;
        this.esercizio = esercizio;
        this.durata = durata;
        this.ripetizioni = ripetizioni;
    }

    public Duration getDurata() {
        return durata;
    }

    public void setDurata(Duration durata) {
        this.durata = durata;
    }

    public int getRipetizioni() {
        return ripetizioni;
    }

    public void setRipetizioni(int ripetizioni) {
        this.ripetizioni = ripetizioni;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SessioneAllenamento getSessioneAllenamento() {
        return sessioneAllenamento;
    }

    public void setSessioneAllenamento(SessioneAllenamento sessioneAllenamento) {
        this.sessioneAllenamento = sessioneAllenamento;
    }

    public Esercizio getEsercizio() {
        return esercizio;
    }

    public void setEsercizio(Esercizio esercizio) {
        this.esercizio = esercizio;
    }

    @Override
    public String toString() {
        return "DettaglioEsercizio{" +
                "id=" + id +
                ", durata=" + durata +
                ", ripetizioni=" + ripetizioni +
                ", sessioneAllenamento=" + sessioneAllenamento +
                ", esercizio=" + esercizio +
                '}';
    }

}

