package model;
import java.util.List;


/** * Rappresenta l'utente fisico che gioca la partita.
 */

public class GiocatoreUmano extends Giocatore {

    /**
     * Costruttore per il giocatore umano.
     * @param nome Il nome o nickname dell'utente.
     */
    public GiocatoreUmano(String nome) {
        super(nome);
    }
    
    @Override
    public Carta scegliCarta(List<Carta> tavolo, Seme briscola) {
        throw new UnsupportedOperationException(
            "La scelta della carta per il giocatore umano arriva dal Controller tramite eventi GUI.");
    }
}
