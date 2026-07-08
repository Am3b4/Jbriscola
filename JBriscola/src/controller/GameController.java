package controller;

import model.Carta;
import model.PartitaModel;
import model.StatoPartita;
import model.StrategiaRandom;
import view.BriscolaView;
import view.CartaButton;
import javax.swing.Timer;

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
        vista.getMenuPanel().getBtnNuovaPartita().addActionListener(e -> vista.mostraModalita());
        vista.getMenuPanel().getBtnProfilo().addActionListener(e -> vista.mostraProfilo());
        vista.getMenuPanel().getBtnEsci().addActionListener(e -> System.exit(0));
        
        // 3. Schermata Selezione Modalità
        vista.getModalitaPanel().getBtn2Giocatori().addActionListener(e -> onNuovaPartita(2));
        vista.getModalitaPanel().getBtn4Giocatori().addActionListener(e -> onNuovaPartita(4));
        vista.getModalitaPanel().getBtnIndietro().addActionListener(e -> vista.mostraMenu());
        
        // 4. Collega le azioni per le carte di gioco
        vista.getGamePanel().setCartaListener(e -> {
            // Estrae il bottone che è stato cliccato
            CartaButton btnCliccato = (CartaButton) e.getSource();
            // Chiama la logica passando la Carta logica
            onCartaSelezionata(btnCliccato.getCarta());
        });
    }

    /**
     * Prepara il Model con i giocatori scelti e avvia il gioco.
     */
    private void onNuovaPartita(int numeroGiocatori) {
        System.out.println("Avvio nuova partita a " + numeroGiocatori + " giocatori...");
        StrategiaRandom strategia = new StrategiaRandom();
        
        java.util.List<model.Giocatore> nuoviGiocatori = new java.util.ArrayList<>();
        
        // Indice 0: Giocatore Umano (Sud)
        nuoviGiocatori.add(new model.GiocatoreUmano("Tu"));
        
        if (numeroGiocatori == 2) {
            // Indice 1: Avversario (Nord)
            nuoviGiocatori.add(new model.GiocatoreAI("Bot CPU", strategia));
        } else if (numeroGiocatori == 4) {
            // In senso antiorario: 1 Destra, 2 Fronte (Socio), 3 Sinistra
            nuoviGiocatori.add(new model.GiocatoreAI("Bot Destra", strategia)); 
            nuoviGiocatori.add(new model.GiocatoreAI("Bot Socio", strategia)); 
            nuoviGiocatori.add(new model.GiocatoreAI("Bot Sinistra",strategia)); 
        }

        modello.impostaGiocatori(nuoviGiocatori);
        modello.iniziaPartita();
        vista.mostraGioco();
    }
   
    /**
     * Metodo chiamato quando l'utente clicca su una carta della sua mano.
     * Valida la mossa e la invia al Model.
     * 
     * @param carta La carta logica selezionata.
     */
    public void onCartaSelezionata(model.Carta carta) {
        if (modello.getStato() == StatoPartita.ATTESA_GIOCATORE) {
            System.out.println("[CONTROLLER] L'utente gioca: " + carta);
            
            modello.giocaCarta(carta);
            
            verificaStatoPartita();
            
        } else {
            //System.out.println("[CONTROLLER] Click ignorato: Non è il tuo turno.");
        }
    }

    /**
     * Smista il flusso del gioco in base allo stato attuale del Model.
     */
    public void verificaStatoPartita() {
        StatoPartita stato = modello.getStato();
        
        if (stato == StatoPartita.AI_IN_GIOCO) {
            eseguiTurnoAI();
        } else if (stato == StatoPartita.FINE_MANO) {
            gestisciFineMano();
        } else if (stato == StatoPartita.FINE_PARTITA) {
            gestisciFinePartita();
        }
    }
    
    /**
     * Esegue il turno del bot con un ritardo visivo per simulare il "pensiero".
     */
    public void eseguiTurnoAI() {
        System.out.println("[CONTROLLER] L'IA sta pensando...");
        
        // Ritardo di 2500 millisecondi (2.5 secondi)
        javax.swing.Timer timer = new javax.swing.Timer(2500, e -> {
            
            // Controllo di sicurezza
            if (modello.getStato() == StatoPartita.AI_IN_GIOCO) {
                
                model.Giocatore corrente = modello.getGiocatoreCorrente();
                
                if (corrente instanceof model.GiocatoreAI) {
                    model.GiocatoreAI bot = (model.GiocatoreAI) corrente;
                    
                    // 1. L'IA analizza il tavolo e sceglie la carta tramite la sua Strategia
                    model.Carta scelta = bot.scegliCarta(
                        modello.getManoCorrente().getCarteGiocate(), 
                        modello.getBriscola().getSeme()
                    );
                    
                    System.out.println("[CONTROLLER] Il Bot " + bot.getNome() + " gioca: " + scelta);
                    
                    // 2. Applichiamo la giocata
                    modello.giocaCarta(scelta);
                    
                    // 3. Loop: Controlliamo di nuovo lo stato. 
                    // (Se giochiamo in 4, potrebbe toccare a un altro Bot. Altrimenti si passa a FINE_MANO).
                    verificaStatoPartita();
                }
            }
        });
        
        timer.setRepeats(false); // Il timer deve scattare una volta sola
        timer.start();
    }
    
    public void gestisciFineMano() {
        System.out.println("[CONTROLLER] Fine presa! Le carte restano sul tavolo per 3 secondi...");
        
        // Crea un timer di 3 secondi (3000 ms) per far vedere la giocata
        javax.swing.Timer timer = new javax.swing.Timer(3000, e -> {
            
            // Dopo 3 secondi, diciamo al Model di risolvere la mano e pulire il tavolo
            modello.avanzaTurno(); 
            
            // Controlla chi deve giocare la prossima carta
            verificaStatoPartita(); 
        });
        
        timer.setRepeats(false);
        timer.start();
    }

    public void gestisciFinePartita() {
        System.out.println("[CONTROLLER] Partita conclusa. (Da implementare)");
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