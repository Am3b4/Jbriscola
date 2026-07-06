package view;

import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MainMenuPanel extends JPanel {
    
    private final JButton btnNuovaPartita;
    private final JButton btnProfilo;
    private final JButton btnEsci;

    public MainMenuPanel() {
        setBackground(new Color(245, 245, 220));
        
        btnNuovaPartita = new JButton("Nuova Partita");
        btnProfilo = new JButton("Profilo");
        btnEsci = new JButton("Esci");
        
        add(btnNuovaPartita);
        add(btnProfilo);
        add(btnEsci);
    }

    public JButton getBtnNuovaPartita() {
        return btnNuovaPartita;
    }

    public JButton getBtnProfilo() {
        return btnProfilo;
    }

    public JButton getBtnEsci() {
        return btnEsci;
    }
}
