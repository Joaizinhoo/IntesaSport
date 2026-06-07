package entity;

import jakarta.persistence.*;

import java.time.Duration;

@Entity
public class Prestazione {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int effettiveRipetizioni;
    private String note;
    private Duration tempoImpiegato;

    public Prestazione() {
    }

    public Prestazione(Duration tempoImpiegato, String note, int effettiveRipetizioni, Long id) {
        this.tempoImpiegato = tempoImpiegato;
        this.note = note;
        this.effettiveRipetizioni = effettiveRipetizioni;
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

    public int getEffettiveRipetizioni() {
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
