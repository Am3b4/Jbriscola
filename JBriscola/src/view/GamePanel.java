package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.PartitaModel;

@SuppressWarnings("deprecation")
public class GamePanel extends JPanel implements Observer {
    
    public GamePanel() {
        // Impostiamo il layout a zone (Nord, Centro, Sud)
        setLayout(new BorderLayout());
        setBackground(new Color(34, 139, 34));
        
        // Scheletro delle 3 zone (info, tavolo, mano)
        add(new JLabel("ZONA INFO (Nord)"), BorderLayout.NORTH);
        add(new JLabel("ZONA TAVOLO (Centro)"), BorderLayout.CENTER);
        add(new JLabel("ZONA MANO (Sud)"), BorderLayout.SOUTH);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof PartitaModel) {
            PartitaModel modello = (PartitaModel) o;
            aggiornaVista(modello);
        }
    }

    /**
     * Metodo chiamato dall'Observer per aggiornare l'interfaccia.
     * Per ora stampa solo un log come richiesto dal checkpoint.
     */
    public void aggiornaVista(PartitaModel modello) {
        System.out.println("[CHECKPOINT FASE 2] GamePanel.update() ricevuto! Stato partita: " + modello.getStato());
    }
}
