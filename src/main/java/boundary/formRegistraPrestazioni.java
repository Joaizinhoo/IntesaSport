package boundary;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import controller.IntesaSport;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.time.Duration;
import java.util.List;
import java.awt.*;

public class formRegistraPrestazioni {
    private JTextField textEmail;
    private JTextField textDisciplina;
    private JComboBox boxStatoSessione;
    private JTextField textData;
    private JButton caricaSessioniButton;
    private JTable table1;
    private JPanel panel;
    private JTextField textIdDettaglio;
    private JPanel panelModifica;
    private JTextField textRipetizioni;
    private JTextField textNote;
    private JTextField textTempoImpiegato;
    private JButton buttonRegPres;
    private JPanel panelMain;
    private JTextField textIdSessione;

    private void eseguiCaricamentoTabella() {
        String email = textEmail.getText();
        String disciplina = textDisciplina.getText();
        String statoString = (String) boxStatoSessione.getSelectedItem();
        String dataString = textData.getText();

        if (email == null || email.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "Il campo E-Mail Atleta è obbligatorio per effettuare la ricerca!",
                    "Errore Input",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<String[]> righeDati = IntesaSport.visualizzaSessioniAssegnate(email, dataString, statoString, disciplina);

        String[] colonne = {
                "ID", "Titolo", "Descrizione", "Data", "Durata prevista",
                "Stato sessione", "Id dettaglio esercizio", "Descrizione esercizio", "Nome esercizio",
                "Durata esercizio", "Ripetizioni esercizio"
        };

        DefaultTableModel model = new DefaultTableModel(colonne, 0);
        String ultimoIdSessione = "";

        for (String[] riga : righeDati) {
            String[] rigaCopia = new String[riga.length];
            System.arraycopy(riga, 0, rigaCopia, 0, riga.length);
            String attualeIdSessione = rigaCopia[0];

            if (attualeIdSessione != null && attualeIdSessione.equals(ultimoIdSessione)) {
                rigaCopia[0] = "";
                rigaCopia[1] = "";
                rigaCopia[2] = "";
                rigaCopia[3] = "";
                rigaCopia[4] = "";
                rigaCopia[5] = "";
            } else {
                ultimoIdSessione = attualeIdSessione;
            }
            model.addRow(rigaCopia);
        }

        table1.setModel(model);

        // Manteniamo le tue impostazioni di ridimensionamento colonne
        table1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table1.getColumnModel().getColumn(0).setPreferredWidth(40);
        table1.getColumnModel().getColumn(1).setPreferredWidth(120);
        table1.getColumnModel().getColumn(2).setPreferredWidth(180);
        table1.getColumnModel().getColumn(3).setWidth(90);
        table1.getColumnModel().getColumn(3).setPreferredWidth(90);
        table1.getColumnModel().getColumn(4).setPreferredWidth(110);
        table1.getColumnModel().getColumn(5).setPreferredWidth(110);
        table1.getColumnModel().getColumn(6).setPreferredWidth(140);
        table1.getColumnModel().getColumn(7).setPreferredWidth(180);
        table1.getColumnModel().getColumn(8).setPreferredWidth(120);
        table1.getColumnModel().getColumn(9).setPreferredWidth(110);
        table1.getColumnModel().getColumn(10).setPreferredWidth(130);
        table1.getTableHeader().setResizingAllowed(true);
    }

    public formRegistraPrestazioni() {
        panelModifica.setVisible(false);

        caricaSessioniButton.addActionListener(e -> {
            eseguiCaricamentoTabella();
        });

        table1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int rigaSelezionata = table1.getSelectedRow();

                    if (rigaSelezionata != -1) {

                        Object idDettaglioObj = table1.getValueAt(rigaSelezionata, 6);

                        if (idDettaglioObj != null && !idDettaglioObj.toString().trim().isEmpty()) {
                            panelModifica.setVisible(true);

                            Window finestraPrincipale = SwingUtilities.getWindowAncestor(table1);
                            if (finestraPrincipale != null) {
                                finestraPrincipale.pack();
                            }

                            String idDettaglioScelto = idDettaglioObj.toString();

                            textIdDettaglio.setText(idDettaglioScelto);
                        }
                    }
                }
            }
        });

        buttonRegPres.addActionListener(e -> {

            String idDettaglio = textIdDettaglio.getText().trim();
            String emailAtleta = textEmail.getText().trim();
            String ripetizioni = textRipetizioni.getText().trim();
            String note = textNote.getText().trim();
            String tempo = textTempoImpiegato.getText().trim();

            if (idDettaglio.isEmpty()) {
                JOptionPane.showMessageDialog(null,
                        "Seleziona prima un esercizio dalla tabella!",
                        "Attenzione",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            int campicompilati = 0;

            if (!ripetizioni.isEmpty()) campicompilati++;
            if (!note.isEmpty()) campicompilati++;
            if (!tempo.isEmpty()) campicompilati++;

            if (campicompilati < 1) {
                JOptionPane.showMessageDialog(null,
                        "Per registrare la prestazione devi compilare almeno un campo a scelta tra:\n" + "- Ripetizioni\n- Note\n- Tempo impiegato", "Compilazione Insufficiente",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            Integer ripetizioniInt = null;
            Long tempoLong = null;
            Duration tempoDuration = null;

            try {
                if (!ripetizioni.isEmpty()) {
                    ripetizioniInt = Integer.parseInt(ripetizioni);
                    if (ripetizioniInt < 0) {
                        JOptionPane.showMessageDialog(null,
                                "Il tempo e le ripetizioni non possono essere negativi", "Errore",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                if (!tempo.isEmpty()) {
                    tempoLong = Long.parseLong(tempo);
                    if (tempoLong < 0) {
                        JOptionPane.showMessageDialog(null,
                                "Il tempo e le ripetizioni non possono essere negativi", "Errore",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    tempoDuration = Duration.ofNanos(tempoLong);
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Inserisci solo numeri nei campi numerici!");
                return;
            }

            boolean successo = IntesaSport.registraRisultatiEsercizio(emailAtleta, Long.parseLong(idDettaglio), ripetizioniInt, tempoDuration, note);

            if (successo) {
                eseguiCaricamentoTabella();

                textRipetizioni.setText("");
                textNote.setText("");
                textTempoImpiegato.setText("");

                JOptionPane.showMessageDialog(null,
                        "Registrazione avvenuta con successo", "Successo",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            } else {
                JOptionPane.showMessageDialog(null,
                        "Errore nella registrazione delle prestazioni", "Errore",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        });
    }

    public JFrame apriFormRegistraPrestazioni() {

        JFrame frame = new JFrame("Registra prestazioni");

        frame.setContentPane(panelMain);

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        return frame;
    }


    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panelMain = new JPanel();
        panelMain.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel = new JPanel();
        panel.setLayout(new GridLayoutManager(6, 3, new Insets(10, 10, 15, 15), -1, -1));
        panelMain.add(panel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel.add(spacer1, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Stato sessione");
        panel.add(label1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        boxStatoSessione = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        defaultComboBoxModel1.addElement("-- Seleziona stato --");
        defaultComboBoxModel1.addElement("Assegnata");
        defaultComboBoxModel1.addElement("Completata");
        defaultComboBoxModel1.addElement("In corso");
        boxStatoSessione.setModel(defaultComboBoxModel1);
        panel.add(boxStatoSessione, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), new Dimension(200, -1), 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Data");
        label2.setVerticalAlignment(0);
        panel.add(label2, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textData = new JTextField();
        panel.add(textData, new GridConstraints(3, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), new Dimension(200, -1), 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel.add(scrollPane1, new GridConstraints(4, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(-1, 250), new Dimension(-1, 250), 0, false));
        table1 = new JTable();
        scrollPane1.setViewportView(table1);
        final JLabel label3 = new JLabel();
        label3.setText("E-Mail Atleta");
        panel.add(label3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Disciplina");
        panel.add(label4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        textDisciplina = new JTextField();
        textDisciplina.setText("");
        panel.add(textDisciplina, new GridConstraints(1, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), new Dimension(200, -1), 0, false));
        textEmail = new JTextField();
        panel.add(textEmail, new GridConstraints(0, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), new Dimension(200, -1), 0, false));
        caricaSessioniButton = new JButton();
        caricaSessioniButton.setText("Carica sessioni");
        panel.add(caricaSessioniButton, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, 30), new Dimension(150, 30), 0, false));
        panelModifica = new JPanel();
        panelModifica.setLayout(new GridLayoutManager(4, 4, new Insets(0, 0, 0, 0), -1, -1));
        panel.add(panelModifica, new GridConstraints(5, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Note");
        panelModifica.add(label5, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("Tempo impiegato");
        panelModifica.add(label6, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textNote = new JTextField();
        panelModifica.add(textNote, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        textTempoImpiegato = new JTextField();
        panelModifica.add(textTempoImpiegato, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        textRipetizioni = new JTextField();
        panelModifica.add(textRipetizioni, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("Ripetizioni");
        panelModifica.add(label7, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label8 = new JLabel();
        label8.setText("ID dettaglio ex. scelto:");
        panelModifica.add(label8, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textIdDettaglio = new JTextField();
        textIdDettaglio.setEditable(false);
        textIdDettaglio.setInheritsPopupMenu(false);
        textIdDettaglio.setSelectionColor(new Color(-14007439));
        textIdDettaglio.setText("");
        panelModifica.add(textIdDettaglio, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(25, -1), new Dimension(25, -1), new Dimension(25, -1), 0, false));
        buttonRegPres = new JButton();
        buttonRegPres.setText("Registra prestazioni");
        panelModifica.add(buttonRegPres, new GridConstraints(3, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(160, -1), new Dimension(160, -1), 0, false));
        textIdSessione = new JTextField();
        textIdSessione.setEditable(false);
        textIdSessione.setInheritsPopupMenu(false);
        textIdSessione.setSelectionColor(new Color(-14007439));
        textIdSessione.setText("");
        panelModifica.add(textIdSessione, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(25, -1), new Dimension(25, -1), new Dimension(25, -1), 0, false));
        final JLabel label9 = new JLabel();
        label9.setText("ID sessione:");
        panelModifica.add(label9, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panelMain;
    }

}

