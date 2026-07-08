package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;


/**Classe del menu di scelta della modalita di gioco
 * */
@SuppressWarnings("serial")
public class SelezionaModalitaPanel extends JPanel {

    private final JButton btn2Giocatori;
    private final JButton btn4Giocatori;
    private final JButton btnIndietro;

    public SelezionaModalitaPanel() {
        setBackground(new Color(245, 245, 220));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel lblTitolo = new JLabel("Scegli la Modalità di Gioco");
        lblTitolo.setFont(new Font("Arial", Font.BOLD, 24));
        add(lblTitolo, gbc);

        gbc.gridy++;
        btn2Giocatori = new JButton("1 vs 1 (2 Giocatori)");
        btn2Giocatori.setFont(new Font("Arial", Font.PLAIN, 18));
        add(btn2Giocatori, gbc);

        gbc.gridy++;
        btn4Giocatori = new JButton("2 vs 2 (4 Giocatori)");
        btn4Giocatori.setFont(new Font("Arial", Font.PLAIN, 18));
        add(btn4Giocatori, gbc);

        gbc.gridy++;
        btnIndietro = new JButton("Indietro");
        add(btnIndietro, gbc);
    }
    // Getters
    public JButton getBtn2Giocatori() { return btn2Giocatori; }
    public JButton getBtn4Giocatori() { return btn4Giocatori; }
    public JButton getBtnIndietro() { return btnIndietro; }
}
