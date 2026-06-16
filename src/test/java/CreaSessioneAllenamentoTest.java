import controller.IntesaSport;
import dto.EsercizioDettaglioDTO;
import dto.SessioneDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CreaSessioneAllenamentoTest {

    private SessioneDTO dtoValido;
    private List<EsercizioDettaglioDTO> listaEsercizi;
    private final String EMAIL_ATLETA = "elena.neri@email.it"; // mail valida per il caso di test

    @BeforeEach
    public void setUp() {
        // allenatore e lista validi
        listaEsercizi = new ArrayList<>();
        listaEsercizi.add(new EsercizioDettaglioDTO(15, 10, "Salto con la corda", "Circuito HIIT", null));

        // campi sessione dto validi
        dtoValido = new SessioneDTO(
                null,
                "Sessione Forza",
                "Scheda personalizzata",
                LocalDate.now().plusDays(2),
                100,
                null,
                listaEsercizi
        );
    }

    @Test
    @DisplayName("TC-01: Input Validi - Sessione Creata")
    public void testTC01_InputValidi() {
        boolean risultato = IntesaSport.creaNuovaSessione(dtoValido, EMAIL_ATLETA);
        assertTrue(risultato, "Il metodo dovrebbe restituire true se tutti i dati sono conformi");
    }

    @Test
    @DisplayName("TC-02: Email non esistente")
    public void testTC02_EmailNonEsistente() {
        boolean risultato = IntesaSport.creaNuovaSessione(dtoValido, "fantasma@sport.it");
        assertFalse(risultato, "Il salvataggio deve fallire se l'email atleta non esiste nel DB");
    }

    @Test
    @DisplayName("TC-03: Email vuota")
    public void testTC03_EmailVuota() {
        boolean risultatoVuota = IntesaSport.creaNuovaSessione(dtoValido, "");
        assertFalse(risultatoVuota, "Il salvataggio deve fallire se l'email è vuota");

        boolean risultatoNull = IntesaSport.creaNuovaSessione(dtoValido, null);
        assertFalse(risultatoNull, "Il salvataggio deve fallire se l'email è null");
    }

    @Test
    @DisplayName("TC-04: Titolo vuoto")
    public void testTC04_TitoloVuoto() {
        SessioneDTO dtoErrato = new SessioneDTO(null, "", "Scheda personalizzata", LocalDate.now().plusDays(2), 100, null, listaEsercizi);
        boolean risultato = IntesaSport.creaNuovaSessione(dtoErrato, EMAIL_ATLETA);
        assertFalse(risultato, "Il salvataggio deve fallire se il titolo è vuoto");
    }

    @Test
    @DisplayName("TC-05: Titolo troppo lungo (>= 255)")
    public void testTC05_TitoloTroppoLungo() {
        String titoloLungo = new String(new char[260]).replace('\0', 'A'); // Stringa di 260 'A'
        SessioneDTO dtoErrato = new SessioneDTO(null, titoloLungo, "Scheda personalizzata", LocalDate.now().plusDays(2), 100, null, listaEsercizi);
        boolean risultato = IntesaSport.creaNuovaSessione(dtoErrato, EMAIL_ATLETA);
        assertFalse(risultato, "Il salvataggio deve fallire se il titolo è >= 255 caratteri");
    }

    @Test
    @DisplayName("TC-06: Data nel passato")
    public void testTC06_DataNelPassato() {
        SessioneDTO dtoErrato = new SessioneDTO(null, "Sessione Forza", "Scheda personalizzata", LocalDate.now().minusDays(5), 100, null, listaEsercizi);
        boolean risultato = IntesaSport.creaNuovaSessione(dtoErrato, EMAIL_ATLETA);
        assertFalse(risultato, "Il salvataggio deve fallire se la data è passata");
    }

    @Test
    @DisplayName("TC-07: Descrizione vuota")
    public void testTC07_DescrizioneVuota() {
        SessioneDTO dtoErrato = new SessioneDTO(null, "Sessione Forza", "", LocalDate.now().plusDays(2), 100, null, listaEsercizi);
        boolean risultato = IntesaSport.creaNuovaSessione(dtoErrato, EMAIL_ATLETA);
        assertFalse(risultato, "Il salvataggio deve fallire se la descrizione è vuota");
    }

    @Test
    @DisplayName("TC-08: Descrizione troppo lunga (>= 1000)")
    public void testTC08_DescrizioneTroppoLunga() {
        String descLunga = new String(new char[1005]).replace('\0', 'B'); // Stringa di 1005 'B'
        SessioneDTO dtoErrato = new SessioneDTO(null, "Sessione Forza", descLunga, LocalDate.now().plusDays(2), 100, null, listaEsercizi);
        boolean risultato = IntesaSport.creaNuovaSessione(dtoErrato, EMAIL_ATLETA);
        assertFalse(risultato, "Il salvataggio deve fallire se la descrizione è >= 1000 caratteri");
    }

    @Test
    @DisplayName("TC-09: Lista Esercizi vuota")
    public void testTC09_ListaEserciziVuota() {
        SessioneDTO dtoErrato = new SessioneDTO(null, "Sessione Forza", "Scheda personalizzata", LocalDate.now().plusDays(2), 100, null, new ArrayList<>());
        boolean risultato = IntesaSport.creaNuovaSessione(dtoErrato, EMAIL_ATLETA);
        assertFalse(risultato, "Il salvataggio deve fallire se la lista esercizi è vuota");
    }

    @Test
    @DisplayName("TC-10: Durata negativa")
    public void testTC10_DurataNegativa() {
        SessioneDTO dtoErrato = new SessioneDTO(null, "Sessione Forza", "Scheda personalizzata", LocalDate.now().plusDays(2), -15, null, listaEsercizi);
        boolean risultato = IntesaSport.creaNuovaSessione(dtoErrato, EMAIL_ATLETA);
        assertFalse(risultato, "Il salvataggio deve fallire se la durata è negativa");
    }

    @Test
    @DisplayName("TC-11: Email di un atleta non associato")
    public void testTC11_EmailNonAssociata() {
        // Usa una mail che non è tra i contatti dell'allenatore loggato nel tuo DB
        boolean risultato = IntesaSport.creaNuovaSessione(dtoValido, "esterno@sport.it");
        assertFalse(risultato, "Il salvataggio deve fallire se l'atleta non è autorizzato/associato");
    }

    @Test
    @DisplayName("TC-12: Email troppo lunga (>= 255)")
    public void testTC12_EmailTroppoLunga() {
        String emailLunga = new String(new char[260]).replace('\0', 'C') + "@sport.it"; // Stringa di 260 'C' + dominio
        boolean risultato = IntesaSport.creaNuovaSessione(dtoValido, emailLunga);
        assertFalse(risultato, "Il salvataggio deve fallire se il formato dell'email è troppo lungo");
    }
}