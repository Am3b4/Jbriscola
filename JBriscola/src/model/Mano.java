package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Rappresenta una singola "mano" o presa all'interno di una partita di Briscola.
 */
public class Mano {
    
    // Lista delle carte giocate in questa mano (l'ordine di inserimento è l'ordine di gioco)
    private final List<Carta> carteGiocate;
    
    // Lista dei giocatori corrispondenti alle carte giocate (stesso indice di carteGiocate)
    private final List<Giocatore> giocatori;
    
    // Il seme di briscola valido per questa presa
    private final Seme briscola;
    
    // Numero di giocatori totali previsti per questa mano (es. 2 o 4)
    private final int numeroGiocatori;

    /**
     * Costruttore della Mano.
     * 
     * @param briscola Il seme di briscola della partita in corso.
     * @param numeroGiocatori Il numero di giocatori che partecipano alla presa (es. 2 o 4).
     */
    public Mano(Seme briscola, int numeroGiocatori) {
        this.briscola = briscola;
        this.numeroGiocatori = numeroGiocatori;
        this.carteGiocate = new ArrayList<>();
        this.giocatori = new ArrayList<>();
    }

    /**
     * Registra la giocata di un giocatore.
     * 
     * @param giocatore Il giocatore che effettua la mossa.
     * @param carta La carta giocata.
     */
    public void gioca(Giocatore giocatore, Carta carta) {
        if (!isCompleta()) {
            giocatori.add(giocatore);
            carteGiocate.add(carta);
        }
    }

    /**
     * Determina chi ha vinto la presa applicando le regole complete della briscola.
     * Sfrutta il metodo batteCarta() precedentemente implementato nella classe Carta.
     * 
     * @return Il Giocatore che ha vinto la mano, oppure null se non è stata giocata alcuna carta.
     */
    public Giocatore determinaVincitore() {
        if (carteGiocate.isEmpty()) {
            return null;
        }

        // La prima carta giocata stabilisce il "seme di mano" (il primo segno che regna)
        Carta cartaVincente = carteGiocate.get(0);
        Giocatore giocatoreVincitore = giocatori.get(0);

        // Confrontiamo la carta vincente attuale con le carte giocate successivamente
        for (int i = 1; i < carteGiocate.size(); i++) {
            Carta cartaCorrente = carteGiocate.get(i);
            
            // Se la nuova carta giocata batte la carta che stava vincendo finora, 
            // diventa la nuova carta vincente
            if (cartaCorrente.batteCarta(cartaVincente, briscola)) {
                cartaVincente = cartaCorrente;
                giocatoreVincitore = giocatori.get(i);
            }
        }

        return giocatoreVincitore;
    }

    /**
     * Calcola i punti totali delle carte presenti in questa mano.
     * Il documento richiede esplicitamente l'uso degli Stream per questo metodo.
     * 
     * @return La somma dei punti delle carte sul tavolo.
     */
    public int getPuntiTotali() {
        return carteGiocate.stream()
                           .mapToInt(Carta::getPunti)
                           .sum();
    }

    /**
     * Verifica se tutti i giocatori previsti hanno giocato la loro carta.
     * 
     * @return true se la presa è completa, false altrimenti.
     */
    public boolean isCompleta() {
        return carteGiocate.size() == numeroGiocatori;
    }
    
    /**
     * Espone le carte attualmente sul tavolo (utile per l'IA o per la View).
     * 
     * @return La lista delle carte giocate finora.
     */
    public List<Carta> getCarteGiocate() {
        return carteGiocate;
    }
}