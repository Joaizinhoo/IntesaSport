package entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CreaPrestazioneTest {

    private DettaglioEsercizio contesto;

    @BeforeEach
    public void setUp() {
        contesto = new DettaglioEsercizio();
    }

    // 1 - Cammino: 1-2-3-4-5-9 [PROBLEMA: SI DOVREBBE FAR FALLIRE IL SALVATAGGIO AD HIBERNATE]
    @Disabled("Cammino non eseguibile: il salvataggio va sempre a buon fine")
    @Test
    public void testCase1_SalvataggioFallito() {
        contesto.setPrestazione(null);

        boolean risultato = contesto.creaPrestazione(30, 20, "Prova");

        assertFalse(risultato);
    }

    // TC_02 - Cammino: 1-2-3-4-5-6-7-8
    @Test
    public void testCase2_SalvataggioSuccesso() {
        contesto.setPrestazione(null); // Precondizione

        boolean risultato = contesto.creaPrestazione(30, 20, "Prova");

        assertTrue(risultato);
        assertNotNull(contesto.getPrestazione());
    }

    // TC_03 - Cammino: 1-2-10-11-12-13-14-15-16-17-18
    @Test
    public void testCase3_AggiornaTuttiICampi() {
        Prestazione prestazioneEsistente = new Prestazione(10, "Prova vecchia", 5);
        contesto.setPrestazione(prestazioneEsistente);

        boolean risultato = contesto.creaPrestazione(30, 20, "Prova nuova");

        assertTrue(risultato);
        assertEquals(30, prestazioneEsistente.getEffettiveRipetizioni());
        assertEquals(20, prestazioneEsistente.getTempoImpiegato());
        assertEquals("Prova nuova", prestazioneEsistente.getNote());
    }

    // TC_04 - Cammino: 1-2-10-11-13-14-15-16-17-18
    @Test
    public void testCase4_SaltaAggiornamentoRipetizioni() {
        String noteIniziali = "Prova vecchia";
        Prestazione prestazioneEsistente = new Prestazione(10, noteIniziali, 5);
        contesto.setPrestazione(prestazioneEsistente);

        boolean risultato = contesto.creaPrestazione(null, 20, "Prova nuova");

        assertTrue(risultato);
        assertEquals(5, prestazioneEsistente.getEffettiveRipetizioni(), "Il valore non doveva cambiare");
        assertEquals(20, prestazioneEsistente.getTempoImpiegato());
        assertEquals("Prova nuova", prestazioneEsistente.getNote());
    }

    // TC_05 - Cammino: 1-2-10-11-12-13-15-16-17-18
    @Test
    public void testCase5_SaltaAggiornamentoDurata() {
        Prestazione prestazioneEsistente = new Prestazione(10, "Prova vecchia", 5);
        contesto.setPrestazione(prestazioneEsistente);

        boolean risultato = contesto.creaPrestazione(30, null, "Prova nuova");

        assertTrue(risultato);
        assertEquals(30, prestazioneEsistente.getEffettiveRipetizioni());
        assertEquals(10, prestazioneEsistente.getTempoImpiegato(), "Il valore non doveva cambiare");
        assertEquals("Prova nuova", prestazioneEsistente.getNote());
    }

    // TC_06 - Cammino: 1-2-10-11-12-13-14-15-17-18
    @Test
    public void testCase6_SaltaAggiornamentoNote() {
        String noteIniziali = "Prova vecchia";
        Prestazione prestazioneEsistente = new Prestazione(10, noteIniziali, 5);
        contesto.setPrestazione(prestazioneEsistente);

        boolean risultato = contesto.creaPrestazione(30, 20, null);

        assertTrue(risultato);
        assertEquals(30, prestazioneEsistente.getEffettiveRipetizioni());
        assertEquals(20, prestazioneEsistente.getTempoImpiegato());
        assertEquals(noteIniziali, prestazioneEsistente.getNote(), "Il valore non doveva cambiare");
    }
}