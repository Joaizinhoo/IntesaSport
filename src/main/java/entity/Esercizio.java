package entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Objects;

@Entity

public class Esercizio implements Comparable<Esercizio> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String descrizione;

    public Esercizio() {}

    public Esercizio(String nome, String descrizione) {
        this.nome = nome;
        this.descrizione = descrizione;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    //Abbiamo scelto che due esercizi sono uguali se hanno lo stesso nome, indifferentemente dalla descrizione

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Esercizio)) return false;
        Esercizio altro = (Esercizio) o;
        if (this.nome == null) {
            return altro.nome == null;
        }
        return this.nome.equalsIgnoreCase(altro.nome);
    }

    @Override
    public int hashCode() {
        return this.nome != null ? this.nome.toLowerCase().hashCode() : 0;
    }

    @Override
    public String toString() {
        return this.nome;
    }

    @Override
    public int compareTo (Esercizio e) {
        return this.nome.compareToIgnoreCase(e.getNome());
    }

}
