package controller;

import model.PartitaModel;
import view.BriscolaView;

/**
 * Controller principale del gioco.
 * Gestisce l'interazione tra l'utente (View) e la logica di gioco (Model).
 */
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
        
        // 2. Collega i pulsanti alle azioni
        vista.getMenuPanel().getBtnNuovaPartita().addActionListener(e -> onNuovaPartita());
        vista.getMenuPanel().getBtnProfilo().addActionListener(e -> vista.mostraProfilo());
        vista.getMenuPanel().getBtnEsci().addActionListener(e -> System.exit(0));
    }

    /**
     * Azione eseguita quando si clicca "Nuova Partita".
     */
    public void onNuovaPartita() {
        System.out.println("Avvio nuova partita in corso...");
        modello.iniziaPartita();
        vista.mostraGioco();
    }
}
