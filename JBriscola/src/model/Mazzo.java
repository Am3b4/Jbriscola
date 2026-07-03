package model;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import util.CartaFactory;

/**
 * Rappresenta il mazzo di carte della partita.
 */
public class Mazzo {
    
    // Lista delle carte attualmente presenti nel mazzo
    private final List<Carta> carte;

    /**
     * Costruttore: inizializza il mazzo utilizzando la CartaFactory
     * per generare le 40 carte necessarie.
     */
    public Mazzo() {
        this.carte = CartaFactory.creaMazzo();
    }

    /**
     * Mescola le carte presenti nel mazzo in ordine casuale.
     */
    public void mescola() {
        Collections.shuffle(carte);
    }

    /**
     * Pesca la prima carta dal mazzo, rimuovendola.
     * Utilizza Optional per gestire in modo sicuro il caso in cui il mazzo sia vuoto,
     * 
     * @return Un Optional contenente la carta pescata, oppure Optional.empty() se il mazzo è vuoto.
     */
    public Optional<Carta> pesca() {
        if (carte.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(carte.remove(0));
    }

    /**
     * Guarda la prima carta del mazzo senza rimuoverla (utile per vedere qual la prossima carta).
     * 
     * @return La carta in cima, o null se il mazzo è vuoto.
     */
    public Carta peek() {
        if (carte.isEmpty()) {
            return null;
        }
        return carte.get(0);
    }

    /**
     * Verifica se il mazzo è vuoto.
     * 
     * @return true se non ci sono più carte, false altrimenti.
     */
    public boolean isEmpty() {
        return carte.isEmpty();
    }

    /**
     * Restituisce il numero di carte rimanenti nel mazzo.
     * 
     * @return Il numero di carte.
     */
    public int size() {
        return carte.size();
    }

    /**
     * Espone uno Stream delle carte nel mazzo, come richiesto dal diagramma UML.
     * 
     * @return Uno stream delle carte.
     */
    public Stream<Carta> stream() {
        return carte.stream();
    }
}