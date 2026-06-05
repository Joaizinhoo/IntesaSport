package entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("ATLETA")
public class Atleta extends Utente{
    private String disciplinaPraticata;
    private String esperienza;
    private String obiettiviSportivi;

    @OneToMany(mappedBy = "atleta")
    private List<SessioneAllenamento> sessioni = new ArrayList<>();

    public Atleta(){
        super();
    }

    public String getDisciplinaPraticata() {
        return disciplinaPraticata;
    }

    public void setDisciplinaPraticata(String disciplinaPraticata){
        this.disciplinaPraticata = disciplinaPraticata;
    }

    public String getEsperienza() {
        return esperienza;
    }

    public void setEsperienza(String esperienza) {
        this.esperienza = esperienza;
    }

    public String getObiettiviSportivi() {
        return obiettiviSportivi;
    }

    public void setObiettiviSportivi(String obiettiviSportivi) {
        this.obiettiviSportivi = obiettiviSportivi;
    }

    public List<SessioneAllenamento> getSessioni() {
        return sessioni;
    }

    public void setSessioni(List<SessioneAllenamento> sessioni) {
        this.sessioni = sessioni;
    }
}
