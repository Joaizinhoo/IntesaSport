package entity;

import dto.EsercizioDettaglioDTO;
import dto.SessioneDTO;
import database.GestorePersistenza;
import jakarta.persistence.*;

import java.time.LocalDate;
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
    private Set<Allenatore> allenatoriAssociati = new HashSet<>();

    @OneToMany(mappedBy = "atleta", fetch = FetchType.EAGER)
    private List<SessioneAllenamento> sessioni = new ArrayList<>();



    ///COSTRUTTORI


    public Atleta() {};

    public Atleta(String email, String nome, String cognome, String disciplina, String disciplinaPraticata, String esperienza, String obiettiviSportivi) {
        super(email, nome, cognome, disciplina);
        this.disciplinaPraticata = disciplinaPraticata;
        this.esperienza = esperienza;
        this.obiettiviSportivi = obiettiviSportivi;
    }

    public List<SessioneDTO> visualizzaSessioniAssegnate(LocalDate data, StatoSessione stato, String disciplina){
        GestorePersistenza gp = new GestorePersistenza();

        List<SessioneDTO> dtoList = new ArrayList<>();

        List<SessioneAllenamento> sessioniNonFiltrate =  gp.cercaPerCampo(
                SessioneAllenamento.class,
                "atleta.email",
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

    public List<SessioneAllenamento> ordinaEFiltraSessioni(List<SessioneAllenamento> sessioniDaOrdinare, LocalDate data, StatoSessione stato, String disciplina) {
        List<SessioneAllenamento> sessioniFiltrate = new ArrayList<>();

        if (sessioniDaOrdinare == null) {
            return sessioniFiltrate;
        }

        if (disciplina != null) {
            if (!this.disciplinaPraticata.equalsIgnoreCase(disciplina.trim())) {
                return sessioniFiltrate;
            }
        }

        for (SessioneAllenamento s : sessioniDaOrdinare) {
            if (data != null && !s.getDate().equals(data)) {
                continue;
            }

            if (stato != null && s.getStatoSessione() != stato) {
                continue;
            }

            sessioniFiltrate.add(s);
        }

        sessioniFiltrate.sort(Comparator.comparing(SessioneAllenamento::getDate));

        return sessioniFiltrate;
    }

    public SessioneAllenamento getSessionePerDettaglioEx(Long idDettaglioEx){
        if (idDettaglioEx == null) {
            return null;
        }

        for (SessioneAllenamento sessione : this.sessioni) {

            if (sessione.trovaDettaglioExPerId(idDettaglioEx) != null) {
                return sessione;
            }
        }

        return null;
    }

    /// SETTER

    public void setAllenatoriAssociati(Set<Allenatore> allenatoriAssociati) {
        allenatoriAssociati = allenatoriAssociati; }

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
        return allenatoriAssociati;}

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
        return this.getNome() + " " + this.getCognome() + " (" + this.getEmail() + ")";
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
