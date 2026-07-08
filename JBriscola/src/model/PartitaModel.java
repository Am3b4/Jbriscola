package model;

import java.util.Comparator;
import java.util.List;
import java.util.Observable;
import java.util.stream.Collectors;

/**
 * Classe principale del Model che gestisce lo stato dell'intera partita.
 * Estende Observable per notificare la View (es. GamePanel) dei cambiamenti.
 */
@SuppressWarnings("deprecation")
public class PartitaModel extends Observable {
	
	private final Mazzo mazzo;
	private final List<Giocatore> giocatori;
	private Carta briscola;
	private Mano manoAttuale;
	private int turnoIndex;
	private StatoPartita stato;
	
	/**
     * Costruttore: inizializza una nuova partita con i giocatori forniti.
     * 
     * @param giocatori La lista dei giocatori 
     */
    public PartitaModel(List<Giocatore> giocatori) {
    	this.giocatori = new java.util.ArrayList<>(giocatori);
        this.mazzo = new Mazzo();
        this.turnoIndex = 0;
    }
    
    // Getters
    public StatoPartita getStato() { return stato; }
    public Mano getManoCorrente() { return manoAttuale; }
    public Carta getBriscola() { return briscola; }
    public Mazzo getMazzo() { return mazzo; }
    public List<Giocatore> getGiocatori() { return giocatori; }
    
    /**
     * Avvia la partita: mescola il mazzo, pesca la briscola e distribuisce le carte iniziali.
     */
    public void iniziaPartita() {
        // 1. Preparazione del mazzo
        mazzo.mescola();
        this.briscola = mazzo.peekUltima();  
        
        // 2. Pulizia dello stato dei giocatori (fondamentale per le partite successive alla prima)
        for (Giocatore g : giocatori) {
            g.getMano().clear();
            g.getCartePrese().clear();
        }
        
        // 3. Distribuzione iniziale (3 carte a testa)
        distribuisciCarte();
        
        // 4. Inizializza la primissima mano (presa) della partita
        manoAttuale = new Mano(briscola.getSeme(), giocatori.size());
        
        // 5. Determina a chi tocca e aggiorna lo stato
        if (getGiocatoreCorrente() instanceof GiocatoreAI) {
            impostaStato(StatoPartita.AI_IN_GIOCO);
        } else {
            impostaStato(StatoPartita.ATTESA_GIOCATORE);
        }
        
        // 6. FONDAMENTALE: Forza la notifica alla View che tutto è pronto
        setChanged();
        notifyObservers();
    }

    /**
     * Distribuisce le carte iniziali (3 a testa) a tutti i giocatori.
     */
    public void distribuisciCarte() {
        for (int i = 0; i < 3; i++) {
            for (Giocatore g : giocatori) {
                mazzo.pesca().ifPresent(g::aggiungiCarta);
            }
        }
        notificaCambiamento();
    }
    
    /**
     * Sostituisce la lista dei giocatori attuale con una nuova.
     * Necessario per cambiare tra modalità 1v1 e 2v2.
     */
    public void impostaGiocatori(List<Giocatore> nuoviGiocatori) {
        this.giocatori.clear();
        this.giocatori.addAll(nuoviGiocatori);
        this.turnoIndex = 0;
    }
    

    /**
     * Registra la mossa del giocatore corrente e fa avanzare lo stato del gioco.
     * 
     * @param carta La carta giocata dal giocatore.
     */
    public void giocaCarta(model.Carta c) {
        Giocatore corrente = getGiocatoreCorrente();
        
        // 1. Il giocatore cala la carta sul tavolo e la toglie dalla sua mano
        manoAttuale.gioca(corrente, c);
        corrente.rimuoviCarta(c);

        // 2. Controllo: La presa è finita?
        if (manoAttuale.isCompleta()) {
            impostaStato(StatoPartita.FINE_MANO);
            setChanged();
            notifyObservers(); 
            return;
        }

        // 3. Se la presa NON è finita, il turno passa al prossimo giocatore
        // (0 diventa 1, 1 diventa 0)
        turnoIndex = (turnoIndex + 1) % giocatori.size();

        // 4. Aggiorniamo lo stato in base a chi tocca ora
        if (getGiocatoreCorrente() instanceof GiocatoreAI) {
            impostaStato(StatoPartita.AI_IN_GIOCO);
        } else {
            impostaStato(StatoPartita.ATTESA_GIOCATORE);
        }

        // 5. Notifichiamo la View per far aggiornare il tavolo
        setChanged();
        notifyObservers();
    }

    /**
     * Passa il turno al giocatore successivo.
     */
    public void avanzaTurno() {
        // 1. Determina chi ha vinto la presa
        Giocatore vincitore = manoAttuale.determinaVincitore();
        System.out.println("[MODEL] Presa vinta da: " + vincitore.getNome());
        
        // 2. Assegna le carte sul tavolo al vincitore
        vincitore.aggiungiPresa(manoAttuale.getCarteGiocate());
        
        // 3. Il vincitore sarà il primo a giocare nella prossima mano
        turnoIndex = giocatori.indexOf(vincitore);
        
        // 4. Pesca nuove carte dal mazzo
        pescaCarte();
        
        // 5. Controlla se la partita è finita
        if (isPartitaFinita()) {
            impostaStato(StatoPartita.FINE_PARTITA);
        } else {
            // Prepara una nuova mano pulita sul tavolo
            manoAttuale = new Mano(briscola.getSeme(), giocatori.size());
            
            // Ripristina lo stato in base a chi ha vinto (e quindi chi inizia la nuova mano)
            if (getGiocatoreCorrente() instanceof GiocatoreAI) {
                impostaStato(StatoPartita.AI_IN_GIOCO);
            } else {
                impostaStato(StatoPartita.ATTESA_GIOCATORE);
            }
        }
    }

    /**
     * Gestisce la logica di fine mano: determina il vincitore, assegna le carte,
     * fa pescare le nuove carte e controlla se la partita è finita.
     */
    private void gestisciFineMano() {
        // 1. Determina chi ha vinto la presa
        Giocatore vincitore = manoAttuale.determinaVincitore();
        
        // 2. Assegna le carte sul tavolo al vincitore
        vincitore.aggiungiPresa(manoAttuale.getCarteGiocate());
        
        // 3. Il vincitore sarà il primo a giocare nella prossima mano
        turnoIndex = giocatori.indexOf(vincitore);
        
        // 4. Pesca nuove carte dal mazzo
        pescaCarte();
        
        // 5. Controlla se la partita è finita
        if (isPartitaFinita()) {
            impostaStato(StatoPartita.FINE_PARTITA);
        } else {
            // Prepara una nuova mano
        	manoAttuale = new Mano(briscola.getSeme(), giocatori.size());
            
            // Ripristina lo stato in base a chi inizia la nuova mano
            if (getGiocatoreCorrente() instanceof GiocatoreAI) {
                impostaStato(StatoPartita.AI_IN_GIOCO);
            } else {
                impostaStato(StatoPartita.ATTESA_GIOCATORE);
            }
        }
    }

    /**
     * Ogni giocatore pesca una carta dal mazzo, partendo dal vincitore della mano precedente.
     */
    public void pescaCarte() {
        // Partiamo dall'indice di chi ha vinto (l'attuale turnoIndex)
        for (int i = 0; i < giocatori.size(); i++) {
            int indicePesca = (turnoIndex + i) % giocatori.size();
            Giocatore g = giocatori.get(indicePesca);
            
            mazzo.pesca().ifPresent(g::aggiungiCarta);
            // NOTA: Se il mazzo è vuoto, pesca() restituisce Optional.empty, 
            // quindi non viene aggiunta nessuna carta (corretto per le ultime 3 mani).
        }
        notificaCambiamento();
    }

    /**
     * Verifica se la partita è terminata (nessuno ha più carte in mano).
     * 
     * @return true se la partita è finita.
     */
    public boolean isPartitaFinita() {
        // La partita finisce se il mazzo è vuoto E tutti i giocatori hanno la mano vuota
        return mazzo.isEmpty() && giocatori.stream().allMatch(g -> g.getMano().isEmpty());
    }

    /**
     * Restituisce una lista dei giocatori ordinata in base al punteggio decrescente.
     * Implementazione richiesta dal documento usando gli Stream.
     * 
     * @return Lista di giocatori ordinata (il primo è il vincitore). X 	
     */
    public List<Giocatore> calcolaClassifica() {
        return giocatori.stream()
                        .sorted(Comparator.comparingInt(Giocatore::getPunteggio).reversed())
                        .collect(Collectors.toList());
    }

    /**
     * Restituisce il giocatore a cui tocca fare la mossa.
     */
    public Giocatore getGiocatoreCorrente() {
        return giocatori.get(turnoIndex);
    }

    /**
     * Cambia lo stato della partita e notifica gli Observer.
     */
    private void impostaStato(StatoPartita nuovoStato) {
        this.stato = nuovoStato;
        notificaCambiamento();
    }

    /**
     * Metodo di supporto per notificare la View dei cambiamenti di stato.
     * Usa i metodi di java.util.Observable.
     */
    private void notificaCambiamento() {
        setChanged();       // Segna l'oggetto come modificato
        notifyObservers();  // Invia la notifica (es. al GamePanel che implementa Observer)
    }
}