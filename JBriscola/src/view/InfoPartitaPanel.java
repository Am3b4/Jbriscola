package view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Pannello che mostra le informazioni di stato della partita:
 * punteggi parziali dei giocatori e di chi è il turno.
 */
@SuppressWarnings("serial")
public class InfoPartitaPanel extends JPanel {

    private JLabel lblPunteggioUmano;
    private JLabel lblTurno;
    private JLabel lblPunteggioAvversario;

    public InfoPartitaPanel() {
        // FlowLayout con 50 pixel di spazio orizzontale tra gli elementi
        setLayout(new FlowLayout(FlowLayout.CENTER, 50, 10));
        setOpaque(false); // Trasparente per mostrare il tavolo

        Font font = new Font("Arial", Font.BOLD, 18);

        lblPunteggioUmano = new JLabel("Giocatore 1: 0 pt");
        lblPunteggioUmano.setForeground(Color.WHITE);
        lblPunteggioUmano.setFont(font);

        lblTurno = new JLabel("Turno: ...");
        lblTurno.setForeground(Color.YELLOW); // Per fare contrasto
        lblTurno.setFont(font);

        lblPunteggioAvversario = new JLabel("Avversario: 0 pt");
        lblPunteggioAvversario.setForeground(Color.WHITE);
        lblPunteggioAvversario.setFont(font);

        add(lblPunteggioUmano);
        add(lblTurno);
        add(lblPunteggioAvversario);
    }

    /**
     * Aggiorna i testi del pannello con i dati attuali del Model.
     */
    public void aggiornaInfo(String nomeUmano, int puntiUmano, String nomeAvversario, int puntiAvversario, String giocatoreCorrente) {
        lblPunteggioUmano.setText(nomeUmano + ": " + puntiUmano + " pt");
        lblPunteggioAvversario.setText(nomeAvversario + ": " + puntiAvversario + " pt");
        lblTurno.setText("TURNO: " + giocatoreCorrente.toUpperCase());
    }
}