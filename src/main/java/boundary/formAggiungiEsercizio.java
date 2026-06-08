package boundary;

import controller.GestioneSessioneControl;
import entity.Esercizio;
import entity.SessioneAllenamento;

import javax.swing.*;
import java.awt.*;
import java.time.Duration;

public class formAggiungiEsercizio extends JFrame {

    private JTextField textNome;
    private JTextField textDescrizione;
    private JTextField textRipetizioni;
    private JTextField textDurata;
    private JButton aggiungiEsercizioButton;

    private final GestioneSessioneControl control;
    private final SessioneAllenamento sessioneCorrente;

    public formAggiungiEsercizio(SessioneAllenamento sessioneCorrente) {

        this.sessioneCorrente = sessioneCorrente;
        this.control = new GestioneSessioneControl();

        setTitle("Aggiungi Esercizio");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new GridLayout(5, 2));

        add(new JLabel("Nome esercizio"));
        textNome = new JTextField();
        add(textNome);

        add(new JLabel("Descrizione"));
        textDescrizione = new JTextField();
        add(textDescrizione);

        add(new JLabel("Ripetizioni"));
        textRipetizioni = new JTextField();
        add(textRipetizioni);

        add(new JLabel("Durata (minuti)"));
        textDurata = new JTextField();
        add(textDurata);

        aggiungiEsercizioButton = new JButton("Aggiungi");
        add(aggiungiEsercizioButton);

        aggiungiEsercizioButton.addActionListener(e -> aggiungiEsercizio());
    }

    private void aggiungiEsercizio() {

        try {

            String nome = textNome.getText();
            String descrizione = textDescrizione.getText();

            Integer ripetizioni =
                    Integer.parseInt(textRipetizioni.getText());

            Duration durata =
                    Duration.ofMinutes(
                            Long.parseLong(textDurata.getText()));

            Esercizio esercizio =
                    control.creaEsercizio(nome, descrizione);

            control.aggiungiEsercizio(
                    sessioneCorrente,
                    esercizio,
                    ripetizioni,
                    durata
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Esercizio aggiunto correttamente!"
            );

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Errore",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
