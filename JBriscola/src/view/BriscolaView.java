package view;

import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * La finestra principale dell'applicazione JBriscola.
 * Utilizza un CardLayout per navigare fluidamente tra le diverse schermate di gioco.
 */
@SuppressWarnings("serial")
public class BriscolaView extends JFrame {

    private final CardLayout cardLayout;
    private final JPanel pannelloPrincipale;

    // Costanti per identificare le "carte" del layout
    public static final String SCHERMATA_MENU = "MENU";
    public static final String SCHERMATA_GIOCO = "GIOCO";
    public static final String SCHERMATA_PROFILO = "PROFILO";
    public static final String SCHERMATA_MODALITA = "MODALITA";
    public static final String SCHERMATA_FINE_PARTITA = "FINE_PARTITA";

    // Riferimenti ai vari pannelli (le schermate vere e proprie)
    private final MainMenuPanel menuPanel;
    private final GamePanel gamePanel;
    private final SelezionaModalitaPanel modalitaPanel;
    private final FinePartitaPanel finePartitaPanel;
    private final ProfiloPanel profiloPanel;

    public BriscolaView() {
        super("JBriscola - Metodologie di Programmazione");

        // 1. Setup di base della finestra
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 900); // Risoluzione iniziale (regolabile in seguito)
        setLocationRelativeTo(null); // Centra la finestra al centro dello schermo
        setResizable(false); // Blocca il ridimensionamento per non rompere il layout grafico

        // 2. Inizializzazione del Layout e del pannello contenitore
        cardLayout = new CardLayout();
        pannelloPrincipale = new JPanel(cardLayout);
        modalitaPanel = new SelezionaModalitaPanel();

        // 3. Istanziazione dei pannelli specifici
        menuPanel = new MainMenuPanel();
        gamePanel = new GamePanel();
        profiloPanel = new ProfiloPanel();
        finePartitaPanel = new FinePartitaPanel();

        // 4. Aggiunta dei pannelli al CardLayout con la loro etichetta identificativa
        pannelloPrincipale.add(menuPanel, SCHERMATA_MENU);
        pannelloPrincipale.add(gamePanel, SCHERMATA_GIOCO);
        pannelloPrincipale.add(profiloPanel, SCHERMATA_PROFILO);
        pannelloPrincipale.add(modalitaPanel, SCHERMATA_MODALITA);
        pannelloPrincipale.add(finePartitaPanel, SCHERMATA_FINE_PARTITA);

        // 5. Aggiunge il contenitore principale al JFrame
        add(pannelloPrincipale);
    }

    // --- METODI PER LA NAVIGAZIONE (Saranno chiamati dal Controller) ---

    public void mostraMenu() {
        cardLayout.show(pannelloPrincipale, SCHERMATA_MENU);
    }

    public void mostraGioco() {
        cardLayout.show(pannelloPrincipale, SCHERMATA_GIOCO);
    }

    public void mostraProfilo() {
        cardLayout.show(pannelloPrincipale, SCHERMATA_PROFILO);
    }
    public void mostraModalita() {
        cardLayout.show(pannelloPrincipale, SCHERMATA_MODALITA);
    }
    public void mostraFinePartita() {
        cardLayout.show(pannelloPrincipale, SCHERMATA_FINE_PARTITA);
    }

    // Getter per esporre i pannelli al Controller (utile per aggiungere gli ActionListener)
    public MainMenuPanel getMenuPanel() {
        return menuPanel;
    }
    public GamePanel getGamePanel() {
        return gamePanel;
    }
    public SelezionaModalitaPanel getModalitaPanel() {
        return modalitaPanel;
    }

    public FinePartitaPanel getFinePartitaPanel() {
        return finePartitaPanel;
    }
    
    public ProfiloPanel getProfiloPanel() {
    	return profiloPanel;
    }
}