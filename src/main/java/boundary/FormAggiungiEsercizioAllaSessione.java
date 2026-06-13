package boundary;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import controller.IntesaSport;
import entity.Esercizio;
import entity.EsercizioDettaglioDTO;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class FormAggiungiEsercizioAllaSessione extends JFrame {

    private JList<Esercizio> listEserciziDatabase;
    private JButton creaNuovoEsercizioButton;
    private JSpinner repetitionSpinner;
    private JSpinner durationSpinner;
    private JButton aggiungiEsercizioAllaSessioneButton;
    private JPanel contentPane;
    private JTextArea txtDescrizioneEse;

    // Variabili per passare i dati alla finestra principale
    private EsercizioDettaglioDTO dettaglioCreato;
    private boolean confermato = false; //diventa true se si preme "aggiungi"

    public FormAggiungiEsercizioAllaSessione() {

        $$$setupUI$$$();
        this.setContentPane(contentPane);
        this.pack();
        this.setMinimumSize(new Dimension(500, 500));
        this.setLocationRelativeTo(null);

        //Popolo la JList
        ricaricaListaEsercizi();

        // cambio la descrizione dinamicamente quando viene premuto un esercizio
        listEserciziDatabase.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                //evito il doppio click
                if (!e.getValueIsAdjusting()) {
                    Esercizio selezionato = listEserciziDatabase.getSelectedValue();
                    if (selezionato != null) {
                        // aggiunge descrizione
                        txtDescrizioneEse.setText(selezionato.getDescrizione());
                    } else {
                        // svuota descrizione
                        txtDescrizioneEse.setText("");
                    }
                }
            }
        });


        // LISTENER "Crea Nuovo Esercizio"
        creaNuovoEsercizioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FormCreaEsercizio formCrea = new FormCreaEsercizio();
                //rendendolo modale posso bloccare la finestra finchè quella di pop-up di creazione esercizio non si chiude
                formCrea.setModal(true);
                formCrea.setLocationRelativeTo(FormAggiungiEsercizioAllaSessione.this);
                formCrea.setVisible(true);

                //ricarico la JList in caso fosse stato aggiunto un nuovo esercizio
                ricaricaListaEsercizi();

                String nomeNewEsercizio = formCrea.getNomeEsercizioCreato();
                if (nomeNewEsercizio != null)
                    selezionaEsercizioNellaLista(nomeNewEsercizio);
            }
        });
        aggiungiEsercizioAllaSessioneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                // RECUPERO I DATI DAL FORM
                Esercizio esercizioSelezionato = listEserciziDatabase.getSelectedValue();

                if (esercizioSelezionato == null) {
                    JOptionPane.showMessageDialog(FormAggiungiEsercizioAllaSessione.this,
                            "Selezionare un esercizio dalla lista",
                            "Nessun Esercizio Selezionato",
                            JOptionPane.WARNING_MESSAGE);
                    return; // Blocca l'esecuzione
                }

                //Leggo i dati dai JSpinner
                int ripetizioni = (Integer) repetitionSpinner.getValue();
                int minuti = (Integer) durationSpinner.getValue();

                // Anche se ho creato dei JSpinner il cui valore non può essere negativo per il tempo
                // e minore di 1 per le ripetizioni preferisco avere un doppio controllo
                if (ripetizioni < 1 || minuti < 0) {
                    JOptionPane.showMessageDialog(FormAggiungiEsercizioAllaSessione.this,
                            "Inserisci almeno una ripetizione ed un tempo non negativo",
                            "Errore Dati",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Anche qui, le ripetizioni ho impostato che partano sempre da 1 dalle opzioni del JSpinner
                // Ma ho preferito inserire un doppio controllo
                if (ripetizioni == 0 && minuti == 0) {
                    JOptionPane.showMessageDialog(FormAggiungiEsercizioAllaSessione.this,
                            "Inserisci almeno una ripetizione o un minuto di durata.",
                            "Dati Incompleti",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Modificato: passiamo direttamente il valore primitivo 'minuti' (int) al costruttore di EsercizioDettaglioDTO
                dettaglioCreato = new EsercizioDettaglioDTO(
                        ripetizioni,
                        minuti,
                        esercizioSelezionato.getNome(),
                        esercizioSelezionato.getDescrizione(),
                        null
                );
                confermato = true;

                //Chiudo il form una volta premuto
                FormAggiungiEsercizioAllaSessione.this.dispose();

            }
        });
    }


    //GETTER PER PRELEVARE I DATI SALVATI

    public boolean isConfermato() {
        return this.confermato;
    }

    public EsercizioDettaglioDTO getDettaglioCreato() {
        return this.dettaglioCreato;
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(10, 3, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.setMaximumSize(new Dimension(1000, 1000));
        final JLabel label1 = new JLabel();
        label1.setText("Seleziona un esercizio da aggiungere");
        contentPane.add(label1, new GridConstraints(0, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Esercizio non presente?");
        contentPane.add(label2, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        creaNuovoEsercizioButton = new JButton();
        creaNuovoEsercizioButton.setText("Crea Nuovo Esercizio");
        contentPane.add(creaNuovoEsercizioButton, new GridConstraints(4, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Ripetizioni");
        contentPane.add(label3, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        contentPane.add(repetitionSpinner, new GridConstraints(6, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Durata in minuti");
        contentPane.add(label4, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        contentPane.add(durationSpinner, new GridConstraints(7, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JSeparator separator1 = new JSeparator();
        contentPane.add(separator1, new GridConstraints(5, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JSeparator separator2 = new JSeparator();
        contentPane.add(separator2, new GridConstraints(8, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        aggiungiEsercizioAllaSessioneButton = new JButton();
        aggiungiEsercizioAllaSessioneButton.setText("Aggiungi esercizio alla sessione di allenamento");
        contentPane.add(aggiungiEsercizioAllaSessioneButton, new GridConstraints(9, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("");
        contentPane.add(label5, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        contentPane.add(scrollPane1, new GridConstraints(2, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        txtDescrizioneEse = new JTextArea();
        txtDescrizioneEse.setEditable(false);
        txtDescrizioneEse.setLineWrap(true);
        txtDescrizioneEse.setRows(4);
        txtDescrizioneEse.setWrapStyleWord(true);
        scrollPane1.setViewportView(txtDescrizioneEse);
        final JScrollPane scrollPane2 = new JScrollPane();
        contentPane.add(scrollPane2, new GridConstraints(1, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        listEserciziDatabase = new JList();
        listEserciziDatabase.setVisibleRowCount(7);
        scrollPane2.setViewportView(listEserciziDatabase);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

    private void createUIComponents() {
        SpinnerModel spinnerRipetizioni = new SpinnerNumberModel(1, 1, 5000, 1);
        repetitionSpinner = new JSpinner(spinnerRipetizioni);
        SpinnerModel spinnerDurata = new SpinnerNumberModel(0, 0, 240, 1);
        durationSpinner = new JSpinner(spinnerDurata);
    }

    private void ricaricaListaEsercizi() {
        DefaultListModel<Esercizio> modelloLista = new DefaultListModel<>();
        List<Esercizio> eserciziDalDB = IntesaSport.visualizzaListaEsercizi();

        for (Esercizio es : eserciziDalDB) {
            modelloLista.addElement(es);
        }

        listEserciziDatabase.setModel(modelloLista);
    }


    // Questo metodo mi serve per selezionare automaticamente un esercizio appena creato
    private void selezionaEsercizioNellaLista(String nomeEsercizio) {
        ListModel<Esercizio> modello = listEserciziDatabase.getModel();
        for (int i = 0; i < modello.getSize(); i++) {
            Esercizio es = modello.getElementAt(i);
            if (es.getNome().equals(nomeEsercizio)) {
                // trovo l'indice dell'ultimo elemento inserito
                listEserciziDatabase.setSelectedIndex(i);
                // scrollo la lista all'elemento trovato (ultimo ese)
                listEserciziDatabase.ensureIndexIsVisible(i);
                break;
            }
        }
    }
}