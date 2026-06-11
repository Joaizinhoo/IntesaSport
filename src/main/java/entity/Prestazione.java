package entity;

import jakarta.persistence.*;

import java.time.Duration;

@Entity
public class Prestazione {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "effettiveRipetizioni", nullable = true)
    private Integer effettiveRipetizioni;

    @Column(name = "note", nullable = true)
    private String note;

    @Column(name = "tempoImpiegato", nullable = true)
    private Duration tempoImpiegato;

    public Prestazione() {
    }

    public Prestazione(Duration tempoImpiegato, String note, Integer effettiveRipetizioni) {
        this.tempoImpiegato = tempoImpiegato;
        this.note = note;
        this.effettiveRipetizioni = effettiveRipetizioni;
    }

    public boolean prestazioneCompleta() {
        boolean notePresenti = this.note != null && !this.note.trim().isEmpty();

        return notePresenti && this.effettiveRipetizioni != null && this.tempoImpiegato != null;
    }

    public Duration getTempoImpiegato() {
        return tempoImpiegato;
    }

    public void setTempoImpiegato(Duration tempoImpiegato) {
        this.tempoImpiegato = tempoImpiegato;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getEffettiveRipetizioni() {
        return effettiveRipetizioni;
    }

    public void setEffettiveRipetizioni(int effettiveRipetizioni) {
        this.effettiveRipetizioni = effettiveRipetizioni;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Prestazione{" +
                "id=" + id +
                ", effettiveRipetizioni=" + effettiveRipetizioni +
                ", note='" + note + '\'' +
                ", tempoImpiegato=" + tempoImpiegato +
                '}';
    }
}
