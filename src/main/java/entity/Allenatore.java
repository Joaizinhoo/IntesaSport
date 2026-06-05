package entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("ALLENATORE")
public class Allenatore extends Utente{
    private int codiceAssociativo;

    @OneToMany(mappedBy = "allenatore")
    private List<SessioneAllenamento> sessioni = new ArrayList<>();

    public Allenatore(){
        super();
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
}
