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
    private ManoAvversariaPanel botTopPanel;
    private ManoAvversariaPanel botLeftPanel;
    private ManoAvversariaPanel botRightPanel;
    private InfoPartitaPanel infoPartitaPanel;
    
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
        botTopPanel = new ManoAvversariaPanel(imageLoader, false);
        botLeftPanel = new ManoAvversariaPanel(imageLoader, true);
        botRightPanel = new ManoAvversariaPanel(imageLoader, true);
        
        // Creiamo un contenitore per la zona Nord che tenga sia le info che le carte avversarie
        JPanel nordContainer = new JPanel(new BorderLayout());
        nordContainer.setOpaque(false);
        
        // InfoPanel
        infoPartitaPanel = new InfoPartitaPanel();
        infoPartitaPanel.setOpaque(false);
        
        // Inseriamo info in alto e carte avversarie in basso nel contenitore Nord
        nordContainer.add(infoPartitaPanel, BorderLayout.NORTH);
        nordContainer.add(botTopPanel, BorderLayout.SOUTH);
        
        // Aggiungiamo i contenitori principali al GamePanel
        add(nordContainer, BorderLayout.NORTH);
        add(tavoloPanel, BorderLayout.CENTER);
        add(manoPanel, BorderLayout.SOUTH);
        add(botLeftPanel, BorderLayout.WEST);
        add(botRightPanel, BorderLayout.EAST);
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

        // 2. Aggiorna la mano del giocatore umano
        manoPanel.aggiornaMano(modello.getGiocatori().get(0).getMano());
        
        int numGiocatori = modello.getGiocatori().size();
        if (numGiocatori == 2) {
            // Modalità 1v1: Usa solo il pannello in alto (Indice 1)
            botTopPanel.aggiornaMano(modello.getGiocatori().get(1).getMano().size());
            botLeftPanel.setVisible(false);
            botRightPanel.setVisible(false);
        } else if (numGiocatori == 4) {
            // Modalità 2v2: Mostra tutti
            botLeftPanel.setVisible(true);
            botRightPanel.setVisible(true);
            
            // Ordine di gioco: 0=Tu, 1=Destra(Bot 1), 2=Alto(Socio), 3=Sinistra(Bot 3)
            botRightPanel.aggiornaMano(modello.getGiocatori().get(1).getMano().size());
            botTopPanel.aggiornaMano(modello.getGiocatori().get(2).getMano().size());
            botLeftPanel.aggiornaMano(modello.getGiocatori().get(3).getMano().size());
        }
        
        // 3. Riapplica il listener del Controller ai nuovi bottoni appena generati
        if (cartaListener != null) {
            for (CartaButton btn : manoPanel.getCarteBottoni()) {
                btn.addActionListener(cartaListener);
            }
        }

        // 4. Abilita/Disabilita la mano in base a di chi è il turno
        boolean isTurnoUmano = (modello.getStato() == StatoPartita.ATTESA_GIOCATORE);
        manoPanel.setAbilitata(isTurnoUmano);
        
        // 5. Aggiorna Info Partita (Punteggi a Squadre e Turno)
        
        String nomeTeam1 = "";
        int puntiTeam1 = 0;
        String nomeTeam2 = "";
        int puntiTeam2 = 0;
        
        if (numGiocatori == 2) {
            // Modalità 1 vs 1
            nomeTeam1 = modello.getGiocatori().get(0).getNome();
            puntiTeam1 = modello.getGiocatori().get(0).getPunteggio();
            
            nomeTeam2 = modello.getGiocatori().get(1).getNome();
            puntiTeam2 = modello.getGiocatori().get(1).getPunteggio();
            
        } else if (numGiocatori == 4) {
            // Modalità 2 vs 2 (Squadre: 0+2 contro 1+3)
            nomeTeam1 = "Noi (Tu + Socio)";
            puntiTeam1 = modello.getGiocatori().get(0).getPunteggio() + modello.getGiocatori().get(2).getPunteggio();
            
            nomeTeam2 = "Loro (Bot Sx/Dx)";
            puntiTeam2 = modello.getGiocatori().get(1).getPunteggio() + modello.getGiocatori().get(3).getPunteggio();
        }
        
        String nomeTurnoCorrente = modello.getGiocatoreCorrente().getNome();
        
        // Invia i dati aggregati alla barra in alto
        infoPartitaPanel.aggiornaInfo(
            nomeTeam1, puntiTeam1,
            nomeTeam2, puntiTeam2,
            nomeTurnoCorrente
        );
    }
    
    // Getter per esporre i pannelli se necessario
    public ManoPanel getManoPanel() {
        return manoPanel;
    }
    
    public TavoloPanel getTavoloPanel() {
        return tavoloPanel;
    }
}
