package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe astratta che rapresenta un generico giocatore di Briscola
 */

public abstract class Giocatore {
	protected String nome;
	protected List<Carta> mano;
	protected List<Carta> cartePrese;
	
	/**
     * Costruttore base per un giocatore.
     * @param nome Il nome del giocatore (es. il nickname dell'utente o il nome del bot).
     */
	public Giocatore(String nome) {
		this.nome = nome;
		this.mano = new ArrayList<>();
		this.cartePrese = new ArrayList<>();
	}
	
	public abstract Carta scegliCarta(List<Carta> tavolo, Seme briscola);
	
	/**
     * Aggiunge una carta alla mano del giocatore (es. dopo aver pescato).
     * @param carta La carta da aggiungere.
     */
	public void aggiungiCarta(Carta carta) {
		if (carta != null) { mano.add(carta);}
	}
	
	/**
     * Rimuove una carta dalla mano del giocatore (es. quando viene giocata).
     * @param carta La carta da rimuovere.
     */
    public void rimuoviCarta(Carta carta) {
        mano.remove(carta);
    }
    
    /**
     * Aggiunge le carte vinte in un turno (presa) al mazzetto delle carte prese dal giocatore.
     * @param carte La lista di carte vinte nella mano corrente.
     */
    public void aggiungiPresa(List<Carta> carte) {
        if (carte != null) {
            cartePrese.addAll(carte);
        }
    }

    /**
     * Restituisce la mano attuale del giocatore.
     * @return La lista delle carte in mano.
     */
    public List<Carta> getMano() {
        return mano;
    }
    
    public List<Carta> getCartePrese() {
        return cartePrese;
    }

    public String getNome() {
        return nome;
    }
    
    /**
     * Calcola il punteggio totale del giocatore sommando i punti di tutte le carte presenti 
     * in 'cartePrese', utilizzando le Java Stream API come da specifiche.
     * 
     * @return Il punteggio totale accumulato.
     */
    public int getPunteggio() {
        return cartePrese.stream()
                         .mapToInt(Carta::getPunti)
                         .sum();
    }
	
}
