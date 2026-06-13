package entity;

import database.GestorePersistenza;
import jakarta.persistence.*;

@Entity
public class DettaglioEsercizio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int durata;
    private int ripetizioni;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sessione_id")
    private SessioneAllenamento sessioneAllenamento;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "esercizio_id")
    private Esercizio esercizio;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "prestazione_id")
    private Prestazione prestazione;

    public DettaglioEsercizio() {}

    public DettaglioEsercizio(SessioneAllenamento sessione, Esercizio esercizio, int durata, int ripetizioni) {
        this.sessioneAllenamento = sessione;
        this.esercizio = esercizio;
        this.durata = durata;
        this.ripetizioni = ripetizioni;
    }

    public boolean creaPrestazione(Integer ripEff, Integer durataEff, String note){
        GestorePersistenza gp = new GestorePersistenza();

        if (this.getPrestazione() == null){
            Prestazione prestazione = new Prestazione(durataEff, note, ripEff);
            boolean successo = gp.salva(prestazione);
            if(successo) {
                this.setPrestazione(prestazione);
                gp.aggiorna(this);
                return true;
            }
            else{
                return false;
            }
        }


        Prestazione prestazioneEsistente = this.getPrestazione();

        if (ripEff != null) {
            prestazioneEsistente.setEffettiveRipetizioni(ripEff);
        }

        if (durataEff != null) {
            prestazioneEsistente.setTempoImpiegato(durataEff);
        }

        if (note != null && !note.trim().isEmpty()) {
            prestazioneEsistente.setNote(note);
        }

        gp.aggiorna(prestazioneEsistente);

        return true;
    }

    public int getDurata() {
        return durata;
    }

    public void setDurata(int durata) {
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

    public Prestazione getPrestazione() {
        return prestazione;
    }

    public void setPrestazione(Prestazione prestazione) {
        this.prestazione = prestazione;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DettaglioEsercizio that)) return false;
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

