package entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "utenti")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_utente", discriminatorType = DiscriminatorType.STRING)
public class Utente {
    @Id
    private String email;

    private String nome;
    private String cognome;
    private String disciplinaPrevalente;

    public Utente(){
    }

    public Utente(String email, String nome, String cognome, String disciplina) {
        this.email = email;
        this.nome = nome;
        this.cognome = cognome;
        this.disciplinaPrevalente = disciplina;
    }

    public String getNome(){
        return this.nome;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public String getCognome(){
        return this.cognome;
    }

    public void setCognome(String cognome){
        this.cognome = cognome;
    }

    public String getEmail(){
        return this.email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getDisciplinaPrevalente(){
        return this.disciplinaPrevalente;
    }

    public void setDisciplinaPrevalente(String disciplinaPrevalente){
        this.disciplinaPrevalente = disciplinaPrevalente;
    }

    @Override
    public String toString() {
        return "Utente{" +
                "email='" + email + '\'' +
                ", nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", disciplinaPrevalente='" + disciplinaPrevalente + '\'' +
                '}';
    }

    //Due utenti sono uguali se hanno la stessa email
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Utente utente)) return false;
        return Objects.equals(email, utente.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
