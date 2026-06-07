package entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

import java.util.*;

@Entity
@DiscriminatorValue("ALLENATORE")
public class Allenatore extends Utente{
    private int codiceAssociativo;
    @ManyToMany(mappedBy = "AllenatoriAssociati")
    private Set<Atleta> atletiAssociati = new HashSet<>();

    @OneToMany(mappedBy = "allenatore")
    private List<SessioneAllenamento> sessioni = new ArrayList<>();

    public Allenatore(){
        super();
    }

    public Allenatore(String email, String nome, String cognome, String disciplina, int codiceAssociativo) {
        super(email, nome, cognome, disciplina);
        this.codiceAssociativo = codiceAssociativo;
    }

    public Set<Atleta> getAtletiAssociati() {
        return atletiAssociati;
    }

    public void setAtletiAssociati(Set<Atleta> atletiAssociati) {
        this.atletiAssociati = atletiAssociati;
    }

    public int getCodiceAssociativo() {
        return codiceAssociativo;
    }

    public void setCodiceAssociativo(int codiceAssociativo){
        this.codiceAssociativo = codiceAssociativo;
    }

    public List<SessioneAllenamento> getSessioni(){
        return this.sessioni;
    }

    public void setSessioni(List<SessioneAllenamento> sessioni){
        this.sessioni = sessioni;
    }

    @Override
    public String toString() {
        return super.toString() + "Allenatore{" +
                "codiceAssociativo=" + codiceAssociativo +
                ", atletiAssociati=" + atletiAssociati +
                ", sessioni=" + sessioni +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Atleta atleta)) return false;

        // controllo se sono uguali come UTENTI
        if (!super.equals(o)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode());
    }
}
