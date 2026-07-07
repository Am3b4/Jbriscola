package controller;

import model.Carta;
import model.PartitaModel;
import model.StatoPartita;
import view.BriscolaView;
import view.CartaButton;

/**
 * Controller principale del gioco.
 * Gestisce l'interazione tra l'utente (View) e la logica di gioco (Model).
 */
@SuppressWarnings("deprecation")
public class GameController {
    
    private final PartitaModel modello;
    private final BriscolaView vista;

    public GameController(PartitaModel modello, BriscolaView vista) {
        this.modello = modello;
        this.vista = vista;
        
        inizializzaController();
    }

    private void inizializzaController() {
        // 1. Registra la View (GamePanel) come Observer del Model
        modello.addObserver(vista.getGamePanel());
        
        // 2. Collega i pulsanti del menu principale
        vista.getMenuPanel().getBtnNuovaPartita().addActionListener(e -> onNuovaPartita());
        vista.getMenuPanel().getBtnProfilo().addActionListener(e -> vista.mostraProfilo());
        vista.getMenuPanel().getBtnEsci().addActionListener(e -> System.exit(0));
        
        // 3. Collega le azioni per le carte di gioco [NOVITA']
        vista.getGamePanel().setCartaListener(e -> {
            // Estrae il bottone che è stato cliccato
            CartaButton btnCliccato = (CartaButton) e.getSource();
            // Chiama la logica passando la Carta logica
            onCartaSelezionata(btnCliccato.getCarta());
        });
    }

    public void onNuovaPartita() {
        System.out.println("Avvio nuova partita in corso...");
        modello.iniziaPartita();
        vista.mostraGioco();
    }

    /**
     * Metodo chiamato quando l'utente clicca su una carta della sua mano.
     * Valida la mossa e la invia al Model.
     * 
     * @param carta La carta logica selezionata.
     */
    public void onCartaSelezionata(Carta carta) {
        // Controllo di sicurezza: verifichiamo che il Model stia effettivamente
        // aspettando la mossa dell'utente umano.
        if (modello.getStato() == StatoPartita.ATTESA_GIOCATORE) {
            System.out.println("[CONTROLLER] L'utente vuole giocare: " + carta);
            
            // Il Model farà avanzare lo stato, rimuoverà la carta dalla mano 
            // e notificherà la View tramite il pattern Observer.
            modello.giocaCarta(carta);
            
        } else {
            System.out.println("[CONTROLLER] Click ignorato: Non è il turno del giocatore o animazione in corso.");
        }
    }

    /**
     * Metodo di utilità per agganciare i listener ai bottoni delle carte appena generati.
     * Deve essere chiamato dalla View (es. GamePanel) ogni volta che la mano viene ridisegnata.
     */
    public void collegaBottoniMano() {
        // Scorriamo tutti i CartaButton presenti attualmente nel ManoPanel
        for (CartaButton btn : vista.getGamePanel().getManoPanel().getCarteBottoni()) {
            
            // Rimuoviamo eventuali listener vecchi per evitare click duplicati
            for (var al : btn.getActionListeners()) {
                btn.removeActionListener(al);
            }
            
            // Aggiungiamo l'azione che chiama onCartaSelezionata passando la carta del bottone
            btn.addActionListener(e -> {
                Carta cartaCliccata = btn.getCarta();
                onCartaSelezionata(cartaCliccata);
            });
        }
    }
}