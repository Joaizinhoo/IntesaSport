package entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

import java.util.*;

@Entity
@DiscriminatorValue("ATLETA")
public class Atleta extends Utente{


    /////ATTRIBUTI


    private String disciplinaPraticata;
    private String esperienza;
    private String obiettiviSportivi;

    //////ManyToMany*(mappedBy = "");
    private Set<Allenatore> AllenatoriAssociati = new HashSet<>();

    @OneToMany(mappedBy = "atleta")
    private List<SessioneAllenamento> sessioni = new ArrayList<>();



    ///COSTRUTTORI


    public Atleta() {};

    public Atleta(String email, String nome, String cognome, String disciplina, String disciplinaPraticata, String esperienza, String obiettiviSportivi) {
        super(email, nome, cognome, disciplina);
        this.disciplinaPraticata = disciplinaPraticata;
        this.esperienza = esperienza;
        this.obiettiviSportivi = obiettiviSportivi;
    }

    /// SETTER

    public void setAllenatoriAssociati(Set<Allenatore> allenatoriAssociati) {
        AllenatoriAssociati = allenatoriAssociati; }

    public void setObiettiviSportivi(String obiettiviSportivi) {
        this.obiettiviSportivi = obiettiviSportivi;
    }

    public void setDisciplinaPraticata(String disciplinaPraticata){
        this.disciplinaPraticata = disciplinaPraticata;
    }

    public void setEsperienza(String esperienza) {
        this.esperienza = esperienza;
    }

    public void setSessioni(List<SessioneAllenamento> sessioni) {
        this.sessioni = sessioni;
    }


    /// GETTER

    public Set<Allenatore> getAllenatoriAssociati() {
        return AllenatoriAssociati;}

    public String getEsperienza() {
        return esperienza;
    }

    public String getObiettiviSportivi() {
        return obiettiviSportivi;
    }

    public String getDisciplinaPraticata() {
        return disciplinaPraticata;
    }

    public List<SessioneAllenamento> getSessioni() {
        return sessioni;
    }

    /// Overrides

    @Override
    public String toString() {
        return  super.toString() + "Atleta{" +
                "disciplinaPraticata='" + disciplinaPraticata + '\'' +
                ", esperienza='" + esperienza + '\'' +
                ", obiettiviSportivi='" + obiettiviSportivi + '\'' +
                ", AllenatoriAssociati=" + AllenatoriAssociati +
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
