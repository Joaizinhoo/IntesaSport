package entity;

import java.time.Duration;

public class EsercizioDettaglioDTO {
    private String descrizioneEx;
    private String nomeEx;
    private Long id;
    private int durata;
    private int ripetizioni;

    public EsercizioDettaglioDTO(int ripetizioni, int durata, String nomeEx, String descrizioneEx, Long id) {
        this.ripetizioni = ripetizioni;
        this.durata = durata;
        this.nomeEx = nomeEx;
        this.descrizioneEx = descrizioneEx;
        this.id = id;
    }

    public String getDescrizioneEx() {
        return descrizioneEx;
    }

    public String getNomeEx() {
        return nomeEx;
    }

    public int getDurata() {
        return durata;
    }

    public int getRipetizioni() {
        return ripetizioni;
    }

    public Long getId() {
        return id;
    }
}
