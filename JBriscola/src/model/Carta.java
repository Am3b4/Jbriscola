package model;

/**
 * Rappresenta una singola carta da gioco della Briscola.
 */
public class Carta {
    private final Seme seme;
    private final ValoreCarta valore;

    /**
     * Costruttore della carta.
     * @param seme Il seme della carta (BASTONI, COPPE, DENARI, SPADE).
     * @param valore Il valore della carta (con i relativi punti e ordine).
     */
    public Carta(Seme seme, ValoreCarta valore) {
        this.seme = seme;
        this.valore = valore;
    }

    public Seme getSeme() {
        return seme;
    }

    public ValoreCarta getValore() {
        return valore;
    }

    /**
     * Restituisce i punti di questa carta.
     * @return Il valore in punti (es. 11 per l'Asso, 0 per un liscio).
     */
    public int getPunti() {
        return valore.getPunti();
    }

    /**
     * Determina se questa carta giocata batte la carta avversaria (la carta attualmente vincente sul tavolo).
     * 
     * @param avversaria La carta da battere.
     * @param briscola Il seme di briscola della partita corrente.
     * @return true se questa carta batte l'avversaria, false altrimenti.
     */
    public boolean batteCarta(Carta avversaria, Seme briscola) {
        // Se questa carta è briscola e l'avversaria no, questa batte l'avversaria
        if (this.seme == briscola && avversaria.getSeme() != briscola) {
            return true;
        }
        
        // Se l'avversaria è briscola e questa no, questa non può batterla
        if (avversaria.getSeme() == briscola && this.seme != briscola) {
            return false;
        }
        
        // Se hanno lo stesso seme (entrambe briscole o entrambe dello stesso seme non briscola),
        // vince quella con il valore di forza (ordine) maggiore.
        if (this.seme == avversaria.getSeme()) {
            return this.valore.getOrdine() > avversaria.getValore().getOrdine();
        }
        
        // Se hanno semi diversi e nessuna delle due è briscola, la seconda carta giocata 
        // (questa) non può battere la prima giocata (avversaria), poiché bisogna superare 
        // la carta di riferimento o usare una briscola.
        return false;
    }

    @Override
    public String toString() {
        return valore.name() + " di " + seme.name();
    }
}
