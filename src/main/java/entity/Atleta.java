package entity;

import com.mysql.cj.Session;
import database.GestorePersistenza;
import jakarta.persistence.*;

import java.util.*;

@Entity
@DiscriminatorValue("ATLETA")
public class Atleta extends Utente{


    /////ATTRIBUTI


    private String disciplinaPraticata;
    private String esperienza;
    private String obiettiviSportivi;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "atleta_allenatore",
            joinColumns = { @JoinColumn(name = "email_atleta") },
            inverseJoinColumns = { @JoinColumn(name = "email_allenatore") }
    )
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

    public List<SessioneDTO> visualizzaSessioniAssegnate(Date data, StatoSessione stato, String disciplina){
        GestorePersistenza gp = new GestorePersistenza();

        List<SessioneDTO> dtoList = new ArrayList<>();

        List<SessioneAllenamento> sessioniNonFiltrate =  gp.cercaPerCampo(
                SessioneAllenamento.class,
                "atleta_id",
                this.getEmail()
        );

        for (SessioneAllenamento s: ordinaEFiltraSessioni(sessioniNonFiltrate, data, stato, disciplina)){
            List<EsercizioDettaglioDTO> listaEsercizi = s.getListaEserciziSessione();

            SessioneDTO dto = new SessioneDTO(
                    s.getId(),
                    s.getTitolo(),
                    s.getDescrizione(),
                    s.getDate(),
                    s.getDurataPrevista(),
                    s.getStatoSessione(),
                    listaEsercizi
            );

            dtoList.add(dto);
        }

        return dtoList;

    }

    public List<SessioneAllenamento> ordinaEFiltraSessioni(List<SessioneAllenamento> sessioniDaOrdinare, Date data, StatoSessione stato, String disciplina) {
        List<SessioneAllenamento> sessioniFiltrate = new ArrayList<>();

        if (sessioniDaOrdinare == null) {
            return sessioniFiltrate;
        }

        if (!this.disciplinaPraticata.equals(disciplina)) {
            return sessioniFiltrate;
        }

        for (SessioneAllenamento s : sessioniDaOrdinare) {
            // Controlliamo solo i dati della sessione corrente (data e stato)
            if (s.getDate().equals(data) && s.getStatoSessione().equals(stato)) {
                sessioniFiltrate.add(s);
            }
        }

        // Logica di business: ordina le sessioni per data
        sessioniFiltrate.sort(Comparator.comparing(SessioneAllenamento::getDate));

        return sessioniFiltrate;
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
