package model;

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
    
    // Non è necessario implementare un metodo di "scelta automatica" della carta qui,
    // poiché l'azione è interattiva e verrà gestita tramite eventi (click sui bottoni) 
    // catturati e processati dal Controller.
}
