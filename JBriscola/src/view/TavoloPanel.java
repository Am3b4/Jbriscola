package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.Carta;
import util.ImageLoader;

/**
 * Pannello centrale che mostra le carte giocate nella mano corrente,
 * la carta di briscola e il mazzo con le carte rimanenti.
 */
@SuppressWarnings("serial")
public class TavoloPanel extends JPanel {

    private JPanel carteGiocatePanel;
    private JLabel lblBriscola;
    private JLabel lblContatoreMazzo;
    private JLabel lblRetroMazzo;
    
    private ImageLoader imageLoader;

    public TavoloPanel(ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
        
        setLayout(new BorderLayout());
        setOpaque(false); // Trasparente per far vedere il tappeto verde sotto

        // --- 1. ZONA MAZZO E BRISCOLA (Posizionata a sinistra) ---
        JPanel areaMazzo = new JPanel();
        // BoxLayout verticale per impilare contatore, mazzo e briscola
        areaMazzo.setLayout(new BoxLayout(areaMazzo, BoxLayout.Y_AXIS));
        areaMazzo.setOpaque(false);
        areaMazzo.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Contatore
        lblContatoreMazzo = new JLabel("Mazzo: 40");
        lblContatoreMazzo.setForeground(Color.WHITE);
        lblContatoreMazzo.setFont(new Font("Arial", Font.BOLD, 16));

        // Immagine del retro del mazzo
        lblRetroMazzo = new JLabel();
        try {
            // Nota: assicurati di avere un'immagine chiamata "retro.png" nella cartella delle carte!
            ImageIcon retroIcon = imageLoader.getImmagineScalata("resources/img/cards/_Dorso.png", 90, 135);
            lblRetroMazzo.setIcon(retroIcon);
        } catch (Exception e) {
            lblRetroMazzo.setText("[DORSO MAZZO]");
            lblRetroMazzo.setForeground(Color.WHITE);
        }

        // Immagine della Briscola (scoperta sotto il mazzo)
        lblBriscola = new JLabel(); 
        
        areaMazzo.add(lblContatoreMazzo);
        areaMazzo.add(lblRetroMazzo);
        areaMazzo.add(lblBriscola);

        // --- 2. ZONA CARTE SUL TAVOLO (Posizionata al centro) ---
        carteGiocatePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
        carteGiocatePanel.setOpaque(false);

        add(areaMazzo, BorderLayout.WEST);
        add(carteGiocatePanel, BorderLayout.CENTER);
    }

    /**
     * Aggiorna la grafica del tavolo (carte calate, briscola, mazzo).
     * 
     * @param carteSulTavolo La lista delle carte giocate nella mano corrente.
     * @param briscola La carta di briscola.
     * @param carteRimanenti Il numero di carte rimaste nel mazzo.
     */
    public void aggiornaTavolo(List<Carta> carteSulTavolo, Carta briscola, int carteRimanenti) {
        
        // 1. Aggiorna il contatore testuale
        lblContatoreMazzo.setText("Mazzo: " + carteRimanenti);

        // 2. Aggiorna l'area del Mazzo e della Briscola
        // Se ci sono carte nel mazzo, la briscola è ancora visibile sotto
        if (carteRimanenti > 0 && briscola != null) {
        	String percorsoBriscola = imageLoader.getPercorsoCarta(briscola);
            lblBriscola.setIcon(imageLoader.getImmagineScalata(percorsoBriscola, 80, 120)); 
            lblRetroMazzo.setVisible(true);
        } else {
            // Svuota le immagini quando il mazzo si esaurisce (ultime 3 mani)
            lblBriscola.setIcon(null);
            lblRetroMazzo.setVisible(false);
        }

        // 3. Aggiorna le carte giocate al centro del tavolo
        carteGiocatePanel.removeAll();
        for (Carta c : carteSulTavolo) {
        	String percorsoImg = imageLoader.getPercorsoCarta(c);
            ImageIcon icona = imageLoader.getImmagineScalata(percorsoImg, 100, 150);
            
            // Usiamo delle semplici JLabel per le carte sul tavolo, perché non devono essere cliccabili
            JLabel lblCartaGiocata = new JLabel(icona);
            carteGiocatePanel.add(lblCartaGiocata);
        }

        // Ridisegna il pannello
        revalidate();
        repaint();
    }
}
