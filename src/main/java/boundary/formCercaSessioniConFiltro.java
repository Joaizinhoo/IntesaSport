package boundary;

import controller.IntesaSport;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class formCercaSessioniConFiltro {

    private JTextField textEmail;
    private JTextField textDisciplina;
    private JComboBox<String> boxStatoSessione;
    private JTextField textData;
    private JButton caricaSessioniButton;
    private JPanel panel;
    private JPanel risultatiPanel;   // pannello dove mettiamo le card

    public formCercaSessioniConFiltro() {
        buildUI();

        caricaSessioniButton.addActionListener(e -> {
            String email = textEmail.getText();
            String disciplina = textDisciplina.getText();
            String statoString = (String) boxStatoSessione.getSelectedItem();
            String dataString = textData.getText();

            if (email == null || email.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null,
                        "Il campo E-Mail Atleta è obbligatorio!",
                        "Errore Input",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            List<String[]> righe = IntesaSport.visualizzaSessioniAssegnate(
                    email, dataString, statoString, disciplina);

            mostraSessioni(raggruppaSessioni(righe));
        });
    }

    // ----------------------------------------------------------------
    // 1. Costruisce l'interfaccia a mano (sostituisce il Designer)
    // ----------------------------------------------------------------
    private void buildUI() {
        panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- Pannello filtri in alto ---
        JPanel filtriPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        textEmail      = new JTextField(20);
        textDisciplina = new JTextField(20);
        textData       = new JTextField(20);

        boxStatoSessione = new JComboBox<>(new String[]{
                "-- Seleziona stato --", "Assegnata", "Completata", "In corso"
        });

        caricaSessioniButton = new JButton("Carica sessioni");

        aggiungiRigaFiltro(filtriPanel, gbc, 0, "E-Mail Atleta",  textEmail);
        aggiungiRigaFiltro(filtriPanel, gbc, 1, "Disciplina",     textDisciplina);
        aggiungiRigaFiltro(filtriPanel, gbc, 2, "Stato sessione", boxStatoSessione);
        aggiungiRigaFiltro(filtriPanel, gbc, 3, "Data",           textData);

        gbc.gridx = 1; gbc.gridy = 4; gbc.anchor = GridBagConstraints.WEST;
        filtriPanel.add(caricaSessioniButton, gbc);

        // --- Pannello risultati (card) al centro, scrollabile ---
        risultatiPanel = new JPanel();
        risultatiPanel.setLayout(new BoxLayout(risultatiPanel, BoxLayout.Y_AXIS));
        risultatiPanel.setBackground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(risultatiPanel);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        panel.add(filtriPanel, BorderLayout.NORTH);
        panel.add(scroll,      BorderLayout.CENTER);
    }

    private void aggiungiRigaFiltro(JPanel p, GridBagConstraints gbc,
                                    int riga, String etichetta, JComponent campo) {
        gbc.gridx = 0; gbc.gridy = riga; gbc.weightx = 0;
        p.add(new JLabel(etichetta), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        p.add(campo, gbc);
    }

    // ----------------------------------------------------------------
    // 2. Raggruppa le righe piatte per ID sessione
    //    Ogni entry: ID -> { info sessione, lista esercizi }
    // ----------------------------------------------------------------
    private Map<String, SessioneUI> raggruppaSessioni(List<String[]> righe) {
        // LinkedHashMap mantiene l'ordine di inserimento
        Map<String, SessioneUI> mappa = new LinkedHashMap<>();

        for (String[] r : righe) {
            // colonne: 0=ID, 1=Titolo, 2=Descr, 3=Data, 4=Durata,
            //          5=Stato, 6=DescrEserc, 7=NomeEserc, 8=DurataEserc, 9=RipEserc
            String id = r[0];
            mappa.putIfAbsent(id, new SessioneUI(r[0], r[1], r[2], r[3], r[4], r[5]));
            mappa.get(id).esercizi.add(new EsercizioUI(r[6], r[7], r[8], r[9]));
        }
        return mappa;
    }

    // ----------------------------------------------------------------
    // 3. Svuota il pannello e crea una card per ogni sessione
    // ----------------------------------------------------------------
    private void mostraSessioni(Map<String, SessioneUI> sessioni) {
        risultatiPanel.removeAll();

        // ✅ FIX: forza il pannello ad allinearsi correttamente
        risultatiPanel.setLayout(new BoxLayout(risultatiPanel, BoxLayout.Y_AXIS));

        if (sessioni.isEmpty()) {
            JLabel nessuno = new JLabel("Nessuna sessione trovata.");
            nessuno.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            nessuno.setAlignmentX(Component.LEFT_ALIGNMENT);
            risultatiPanel.add(nessuno);
        }

        for (SessioneUI s : sessioni.values()) {
            risultatiPanel.add(creaCard(s));
            risultatiPanel.add(Box.createVerticalStrut(10));
        }

        // ✅ FIX: aggiunge spazio vuoto in fondo per non stirare l'ultima card
        risultatiPanel.add(Box.createVerticalGlue());

        risultatiPanel.revalidate();
        risultatiPanel.repaint();
    }

    // ----------------------------------------------------------------
    // 4. Costruisce la card di una singola sessione
    // ----------------------------------------------------------------
    private JPanel creaCard(SessioneUI s) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1, true),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        card.setBackground(new Color(245, 248, 255));

        // -- Header: titolo + badge stato --
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel titolo = new JLabel(s.titolo);
        titolo.setFont(new Font("SansSerif", Font.BOLD, 15));

        JLabel badge = new JLabel(s.stato);
        badge.setOpaque(true);
        badge.setBackground(coloreStato(s.stato));
        badge.setForeground(Color.WHITE);
        badge.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));
        badge.setFont(new Font("SansSerif", Font.PLAIN, 11));

        header.add(titolo, BorderLayout.WEST);
        header.add(badge,  BorderLayout.EAST);

        // -- Info sessione --
        JLabel info = new JLabel(
                "<html><i>" + s.descrizione + "</i>&nbsp;&nbsp;"
                        + "📅 " + s.data + "&nbsp;&nbsp;⏱ " + s.durata + "</html>");
        info.setFont(new Font("SansSerif", Font.PLAIN, 12));
        info.setForeground(new Color(80, 80, 80));

        // -- Lista esercizi come pulsanti --
        JPanel eserciziPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        eserciziPanel.setOpaque(false);

        for (EsercizioUI es : s.esercizi) {
            JButton btn = new JButton(es.nome);
            btn.setToolTipText(es.descrizione);
            btn.setFont(new Font("SansSerif", Font.PLAIN, 12));
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btn.addActionListener(ev -> apriDettaglioEsercizio(es, s));
            eserciziPanel.add(btn);
        }

        JPanel corpo = new JPanel(new GridLayout(2, 1, 0, 4));
        corpo.setOpaque(false);
        corpo.add(info);
        corpo.add(eserciziPanel);

        card.add(header, BorderLayout.NORTH);
        card.add(corpo,  BorderLayout.CENTER);

        // ✅ FIX: setMaximumSize DOPO aver aggiunto tutti i componenti
        card.setAlignmentX(Component.LEFT_ALIGNMENT); // ✅ fondamentale per BoxLayout
        Dimension pref = card.getPreferredSize();
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, pref.height + 20));

        return card;
    }

    // ----------------------------------------------------------------
    // 5. Dialog che si apre cliccando un esercizio
    // ----------------------------------------------------------------
    private void apriDettaglioEsercizio(EsercizioUI es, SessioneUI s) {
        JDialog dialog = new JDialog((Frame) null, "Dettaglio esercizio", true);
        dialog.setLayout(new GridLayout(0, 2, 8, 8));
        dialog.getRootPane().setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        dialog.add(new JLabel("Sessione:"));   dialog.add(new JLabel(s.titolo));
        dialog.add(new JLabel("Esercizio:"));  dialog.add(new JLabel(es.nome));
        dialog.add(new JLabel("Descrizione:")); dialog.add(new JLabel(es.descrizione));
        dialog.add(new JLabel("Durata:"));     dialog.add(new JLabel(es.durata));
        dialog.add(new JLabel("Ripetizioni:")); dialog.add(new JLabel(es.ripetizioni));

        // Qui puoi aggiungere campi editabili per inserire nuove info
        dialog.add(new JLabel("Note aggiuntive:"));
        JTextField noteField = new JTextField();
        dialog.add(noteField);

        JButton salva = new JButton("Salva");
        salva.addActionListener(ev -> {
            // TODO: salva noteField.getText() nel DB
            dialog.dispose();
        });
        dialog.add(new JLabel());
        dialog.add(salva);

        dialog.pack();
        dialog.setLocationRelativeTo(panel);
        dialog.setVisible(true);
    }

    // ----------------------------------------------------------------
    // Helpers
    // ----------------------------------------------------------------
    private Color coloreStato(String stato) {
        if (stato == null) return Color.GRAY;
        return switch (stato.toUpperCase()) {
            case "ASSEGNATA"  -> new Color(52, 120, 200);
            case "IN_CORSO"   -> new Color(200, 130, 30);
            case "COMPLETATA" -> new Color(40, 160, 80);
            default           -> Color.GRAY;
        };
    }

    // ----------------------------------------------------------------
    // Record / strutture dati locali
    // ----------------------------------------------------------------
    static class SessioneUI {
        String id, titolo, descrizione, data, durata, stato;
        List<EsercizioUI> esercizi = new ArrayList<>();
        SessioneUI(String id, String titolo, String descrizione,
                   String data, String durata, String stato) {
            this.id = id; this.titolo = titolo; this.descrizione = descrizione;
            this.data = data; this.durata = durata; this.stato = stato;
        }
    }

    static class EsercizioUI {
        String descrizione, nome, durata, ripetizioni;
        EsercizioUI(String descrizione, String nome, String durata, String ripetizioni) {
            this.descrizione = descrizione; this.nome = nome;
            this.durata = durata; this.ripetizioni = ripetizioni;
        }
    }

    // ----------------------------------------------------------------
    // Main
    // ----------------------------------------------------------------
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("IntesaSport – Sessioni");
            frame.setContentPane(new formCercaSessioniConFiltro().panel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
