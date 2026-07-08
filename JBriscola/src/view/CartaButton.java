package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import model.Carta;

/**
 * Rappresenta visivamente una carta sul tavolo o nella mano del giocatore.
 * Estende JButton per poter essere cliccata e gestisce un effetto di "hover".
 */
@SuppressWarnings("serial")
public class CartaButton extends JButton {

    private Carta carta;
    private ImageIcon imageScalata;
    private boolean evidenziata;

    /**
     * Costruttore del bottone carta.
     * 
     * @param carta La carta logica associata a questo bottone.
     * @param imageScalata L'immagine già ridimensionata dall'ImageLoader.
     */
    public CartaButton(Carta carta, ImageIcon imageScalata) {
        this.carta = carta;
        this.imageScalata = imageScalata;
        this.evidenziata = false;

        // 1. Configurazione grafica di base
        setIcon(this.imageScalata);
        setContentAreaFilled(false); // Rimuove lo sfondo grigio del bottone
        setFocusPainted(false);      // Rimuove il tratteggio quando cliccato
        setBorderPainted(false);     // Rimuove il bordo 3D di default di Windows/Mac

        // 2. Aggiunta del listener per l'effetto hover
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                // Evidenzia la carta solo se il bottone è abilitato (es. tocca all'utente)
                if (isEnabled()) {
                    setEvidenziata(true);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setEvidenziata(false);
            }
        });
    }

    public Carta getCarta() {
        return carta;
    }

    public void setEvidenziata(boolean evidenziata) {
        this.evidenziata = evidenziata;
        repaint(); // Forza Swing a ridisegnare il componente per mostrare l'effetto
    }

    /**
     * Sovrascriviamo il metodo di disegno per aggiungere l'highlight visivo.
     */
    @Override
    protected void paintComponent(Graphics g) {
        // Disegna prima la carta normale (l'icona che abbiamo passato al costruttore)
        super.paintComponent(g);

        // Se il mouse è sopra la carta, disegniamo un "bagliore" giallo
        if (evidenziata) {
            // Un rettangolo giallo semi-trasparente (valore alpha 80 su 255)
            g.setColor(new Color(255, 255, 0, 80));
            g.fillRect(0, 0, getWidth(), getHeight());
            
            // Un bordino giallo solido per risaltarla ancora di più
            g.setColor(Color.YELLOW);
            g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
            g.drawRect(1, 1, getWidth() - 3, getHeight() - 3); // Bordo un po' più spesso
        }
    }
}
