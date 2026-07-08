package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Schermata di riepilogo mostrata alla fine della partita.
 */
@SuppressWarnings("serial")
public class FinePartitaPanel extends JPanel {

    private JLabel lblTitolo;
    private JLabel lblRisultato;
    private JLabel lblPunteggi;
    private JButton btnTornaMenu;

    public FinePartitaPanel() {
        setBackground(new Color(245, 245, 220)); // Stesso colore del menu
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.gridx = 0;
        gbc.gridy = 0;

        lblTitolo = new JLabel("Partita Conclusa!");
        lblTitolo.setFont(new Font("Arial", Font.BOLD, 36));
        add(lblTitolo, gbc);

        gbc.gridy++;
        lblRisultato = new JLabel("Vincitore: ...");
        lblRisultato.setFont(new Font("Arial", Font.BOLD, 28));
        lblRisultato.setForeground(new Color(0, 153, 0)); // Verde scuro
        add(lblRisultato, gbc);

        gbc.gridy++;
        lblPunteggi = new JLabel("Punti: 0 - 0");
        lblPunteggi.setFont(new Font("Arial", Font.PLAIN, 22));
        add(lblPunteggi, gbc);

        gbc.gridy++;
        btnTornaMenu = new JButton("Torna al Menu Principale");
        btnTornaMenu.setFont(new Font("Arial", Font.PLAIN, 18));
        add(btnTornaMenu, gbc);
    }

    /**
     * Aggiorna i testi del pannello con il risultato finale.
     */
    public void impostaRisultato(String risultato, String punteggi) {
        lblRisultato.setText(risultato);
        lblPunteggi.setText(punteggi);
    }

    public JButton getBtnTornaMenu() { 
        return btnTornaMenu; 
    }
}
