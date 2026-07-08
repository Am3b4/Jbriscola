package view;

import java.awt.FlowLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import util.ImageLoader;

/**
 * Pannello posizionato in alto che mostra le carte (coperte) dell'avversario.
 */
@SuppressWarnings("serial")
public class ManoAvversariaPanel extends JPanel {

    private ImageLoader imageLoader;

    public ManoAvversariaPanel(ImageLoader imageLoader, boolean verticale) {
        this.imageLoader = imageLoader;
        setOpaque(false); 
        
        if (verticale) {
            // Dispone le carte una sopra l'altra per i bordi laterali
            setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));
            setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));
        } else {
            // Disposizione orizzontale classica per il bot in alto
            setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        }
    }

    /**
     * Aggiorna visivamente la mano dell'avversario disegnando i dorsi.
     * * @param numeroCarte Il numero di carte attualmente in mano all'IA.
     */
    public void aggiornaMano(int numeroCarte) {
        removeAll(); // Pulisce le carte vecchie

        // Carica l'immagine del dorso usando il tuo traduttore
        ImageIcon dorso = imageLoader.getImmagineScalata(imageLoader.getPercorsoDorso(), 90, 135);

        // Disegna un dorso per ogni carta che l'avversario ha in mano
        for (int i = 0; i < numeroCarte; i++) {
            JLabel lblCartaCoperta = new JLabel();
            
            if (dorso != null && dorso.getIconWidth() > 0) {
                lblCartaCoperta.setIcon(dorso);
            } else {
                // Fallback: Se l'immagine fallisce, disegna una finta carta grigia
                lblCartaCoperta.setText("[ DORSO ]");
                lblCartaCoperta.setOpaque(true);
                lblCartaCoperta.setBackground(java.awt.Color.GRAY);
                lblCartaCoperta.setForeground(java.awt.Color.WHITE);
                lblCartaCoperta.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.BLACK));
                lblCartaCoperta.setPreferredSize(new java.awt.Dimension(90, 135));
                lblCartaCoperta.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            }
            add(lblCartaCoperta);
        }

        revalidate();
        repaint();
    }
}
