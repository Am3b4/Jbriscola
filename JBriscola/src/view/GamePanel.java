package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.PartitaModel;
import model.StatoPartita;
import util.ImageLoader;

@SuppressWarnings("deprecation")
public class GamePanel extends JPanel implements Observer {
    
    private Image immagineSfondo;
    
    private TavoloPanel tavoloPanel;
    private ManoPanel manoPanel;
    private JPanel infoPartitaPanel; // Per ora lo lasciamo semplice
    
    private ImageLoader imageLoader;
    private ActionListener cartaListener;

    public GamePanel() {
        setLayout(new BorderLayout());
        
        // Carica immagine di sfondo
        try {
            ImageIcon icon = new ImageIcon("resources/img/tappeto_verde.jpg");
            immagineSfondo = icon.getImage();
        } catch (Exception e) {
            setBackground(new Color(0, 100, 0)); 
        }
        
        // Condividiamo lo stesso ImageLoader per ottimizzare la memoria
        imageLoader = new ImageLoader();
        
        // Inizializza i pannelli reali
        tavoloPanel = new TavoloPanel(imageLoader);
        manoPanel = new ManoPanel(imageLoader);
        
        // InfoPanel provvisorio
        infoPartitaPanel = new JPanel();
        infoPartitaPanel.setOpaque(false);
        JLabel lblInfo = new JLabel("=== INFO PARTITA (In costruzione) ===");
        lblInfo.setForeground(Color.WHITE);
        infoPartitaPanel.add(lblInfo);
        
        add(infoPartitaPanel, BorderLayout.NORTH);
        add(tavoloPanel, BorderLayout.CENTER);
        add(manoPanel, BorderLayout.SOUTH);
    }

    /**
     * Permette al Controller di registrare la sua azione sui bottoni delle carte.
     */
    public void setCartaListener(ActionListener listener) {
        this.cartaListener = listener;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (immagineSfondo != null) {
            g.drawImage(immagineSfondo, 0, 0, getWidth(), getHeight(), this);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof PartitaModel) {
            aggiornaVista((PartitaModel) o);
        }
    }

    /**
     * Il cuore dell'aggiornamento visivo. 
     * Prende i dati dal Model e li distribuisce ai pannelli specifici.
     */
    private void aggiornaVista(PartitaModel modello) {
        
        // --- INIZIO MODIFICA (Programmazione Difensiva) ---
        // Se la mano non è ancora stata creata nel model, usiamo una lista vuota
        // per evitare che il TavoloPanel o il GamePanel vadano in errore (NPE)
        java.util.List<model.Carta> carteDaMostrare = new java.util.ArrayList<>();
        if (modello.getManoCorrente() != null) {
            carteDaMostrare = modello.getManoCorrente().getCarteGiocate();
        }
        
        // 1. Aggiorna il tavolo
        tavoloPanel.aggiornaTavolo(
            carteDaMostrare,
            modello.getBriscola(), 
            modello.getMazzo().size()
        );
        // --- FINE MODIFICA ---

        // 2. Aggiorna la mano del giocatore umano...
        manoPanel.aggiornaMano(modello.getGiocatori().get(0).getMano());
        
        // 3. Riapplica il listener del Controller ai nuovi bottoni appena generati
        if (cartaListener != null) {
            for (CartaButton btn : manoPanel.getCarteBottoni()) {
                btn.addActionListener(cartaListener);
            }
        }

        // 4. Abilita/Disabilita la mano in base a di chi è il turno
        boolean isTurnoUmano = (modello.getStato() == StatoPartita.ATTESA_GIOCATORE);
        manoPanel.setAbilitata(isTurnoUmano);

        System.out.println("[VIEW] Schermata aggiornata. Stato: " + modello.getStato());
    }
    
    // Getter per esporre i pannelli se necessario
    public ManoPanel getManoPanel() {
        return manoPanel;
    }
    
    public TavoloPanel getTavoloPanel() {
        return tavoloPanel;
    }
}
