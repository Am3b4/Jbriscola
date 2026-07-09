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
    private JLabel[] slotCarte;
    
    private ImageLoader imageLoader;

    public TavoloPanel(ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
        
        setLayout(new BorderLayout());
        setOpaque(false); // Trasparente per far vedere il tappeto verde sotto

        //ZONA MAZZO E BRISCOLA (Posizionata a sinistra)
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
            // Fallback: Se l'immagine fallisce, disegna una finta carta grigia
        	lblRetroMazzo.setText("[ DORSO ]");
        	lblRetroMazzo.setOpaque(true);
        	lblRetroMazzo.setBackground(java.awt.Color.GRAY);
        	lblRetroMazzo.setForeground(java.awt.Color.WHITE);
        	lblRetroMazzo.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.BLACK));
        	lblRetroMazzo.setPreferredSize(new java.awt.Dimension(90, 135));
        	lblRetroMazzo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        }

        // Immagine della Briscola (scoperta sotto il mazzo) con effetto Highlight dorato SPESSO
        lblBriscola = new JLabel() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                if (getIcon() != null) {
                    java.awt.Graphics2D g2d = (java.awt.Graphics2D) g;
                    // Attiviamo l'antialiasing per rendere i bordi curvi
                    g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    // 1. Disegna il bagliore solido e molto più spesso (es. 6 pixel)
                    g2d.setStroke(new java.awt.BasicStroke(6.0f)); 
                    g2d.setColor(Color.YELLOW);
                    // Rimpiccioliamo le coordinate di 3 pixel per non "tagliare" il bordo fuori dalla JLabel
                    g2d.drawRoundRect(3, 3, getWidth() - 6, getHeight() - 6, 15, 15);
                    
                    // 2. Riempe il centro con un giallo semi-trasparente
                    g2d.setColor(new Color(255, 215, 0, 100)); 
                    g2d.fillRoundRect(3, 3, getWidth() - 6, getHeight() - 6, 15, 15);
                }
                super.paintComponent(g); // Disegna l'immagine vera e propria SOPRA il bagliore
            }
        };
        // FONDAMENTALE: Aggiungiamo uno spazio vuoto di 8 pixel attorno alla carta 
        // così il bordo giallo ha lo spazio fisico per essere disegnato senza essere coperto
        lblBriscola.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        lblBriscola.setHorizontalAlignment(JLabel.CENTER);
        lblBriscola.setVerticalAlignment(JLabel.CENTER);
        
        areaMazzo.add(lblContatoreMazzo);
        areaMazzo.add(lblRetroMazzo);
        areaMazzo.add(lblBriscola);

        //ZONA CARTE SUL TAVOLO (Posizionata al centro)
        // 1. IL WRAPPER: Questo assicura che la croce stia sempre in mezzo al tavolo vuoto
        JPanel centerWrapper = new JPanel(new java.awt.GridBagLayout());
        centerWrapper.setOpaque(false);
        
        // 2. LA CROCE: Layout assoluto con dimensioni fisse
        carteGiocatePanel = new JPanel();
        carteGiocatePanel.setLayout(null); 
        carteGiocatePanel.setPreferredSize(new java.awt.Dimension(400, 380));
        carteGiocatePanel.setOpaque(false);
        
        // 3. Creiamo 4 slot fissi e li aggiungiamo alla croce
        slotCarte = new JLabel[4];
        for (int i = 0; i < 4; i++) {
            slotCarte[i] = new JLabel();
            carteGiocatePanel.add(slotCarte[i]);
        }
        
        // 4. Assegniamo le coordinate esatte (x, y, larghezza, altezza) per formare una croce
        slotCarte[0].setBounds(120, 230, 100, 150); // SUD (Giocatore 0 - Tu)
        slotCarte[1].setBounds(240, 115, 100, 150); // EST (Giocatore 1 - Bot Destra)
        slotCarte[2].setBounds(120, 0, 100, 150);   // NORD (Giocatore 2 - Bot CPU / Socio)
        slotCarte[3].setBounds(0, 115, 100, 150);  // OVEST (Giocatore 3 - Bot Sinistra)

        centerWrapper.add(carteGiocatePanel);
        
        add(areaMazzo, BorderLayout.WEST);
        add(centerWrapper, BorderLayout.CENTER);
    }

    /**
     * Aggiorna la grafica del tavolo (carte calate, briscola, mazzo).
     * 
     * @param carteSulTavolo La lista delle carte giocate nella mano corrente.
     * @param briscola La carta di briscola.
     * @param carteRimanenti Il numero di carte rimaste nel mazzo.
     */
    public void aggiornaTavolo(List<Carta> carteSulTavolo, Carta briscola, int carteRimanenti, int numGiocatori, int turnoAttualeIndex) {
        
        // 1. Aggiorna il contatore e la Briscola
        lblContatoreMazzo.setText("Mazzo: " + carteRimanenti);
        if (carteRimanenti > 0 && briscola != null) {
            String percorsoBriscola = imageLoader.getPercorsoCarta(briscola);
            ImageIcon iconaBriscola = imageLoader.getImmagineScalata(percorsoBriscola, 100, 150);
            
            if (iconaBriscola != null && iconaBriscola.getIconWidth() > 0) {
                lblBriscola.setIcon(iconaBriscola);
                lblBriscola.setText(""); 
            } else {
                lblBriscola.setText("[" + briscola.getValore() + " " + briscola.getSeme() + "]");
                lblBriscola.setForeground(Color.WHITE);
            }
            lblRetroMazzo.setVisible(true);
        } else {
            lblBriscola.setIcon(null);
            lblRetroMazzo.setVisible(false);
        }

        // 2. Svuota tutti gli slot del tavolo preventivamente
        for (int i = 0; i < 4; i++) {
            slotCarte[i].setIcon(null); 
        }

        // 3. Calcola di chi è la carta e la mette nel suo posto fisso
        int carteGiocateNum = carteSulTavolo.size();
        int indicePartenza;
        
        if (carteGiocateNum == numGiocatori && carteGiocateNum > 0) {
            // Chi ha giocato l'ultima carta è l'attuale turnoAttualeIndex.
            // Di conseguenza, chi ha *iniziato* la mano è il giocatore successivo (ciclicamente).
            indicePartenza = (turnoAttualeIndex + 1) % numGiocatori;
        } else {
            // Mano in corso: il turno è avanzato regolarmente
            indicePartenza = (turnoAttualeIndex - carteGiocateNum + numGiocatori) % numGiocatori;
        }

        for (int i = 0; i < carteGiocateNum; i++) {
            String percorsoImg = imageLoader.getPercorsoCarta(carteSulTavolo.get(i));
            ImageIcon icona = imageLoader.getImmagineScalata(percorsoImg, 100, 150);
            
            // Trova l'indice assoluto del giocatore che ha giocato questa specifica carta
            int indiceGiocatore = (indicePartenza + i) % numGiocatori;
            
            if (numGiocatori == 2) {
                // In 1v1: L'indice 0 (Tu) va a Sud(0), l'indice 1 (Bot) va a Nord(2)
                int slotIndex = (indiceGiocatore == 0) ? 0 : 2;
                slotCarte[slotIndex].setIcon(icona);
            } else {
                // In 2v2: Gli indici (0,1,2,3) corrispondono perfettamente agli slot (Sud, Est, Nord, Ovest)
                slotCarte[indiceGiocatore].setIcon(icona);
            }
        }

        revalidate();
        repaint();
    }
    
    public JLabel[] getSlotCarte() { return slotCarte; }
}
