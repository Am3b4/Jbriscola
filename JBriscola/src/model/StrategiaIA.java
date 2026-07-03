package model;

import java.util.List;

/**
 * Interfaccia per il pattern Strategy. 
 * Definisce l'algoritmo di scelta della carta per il giocatore AI.
 */
public interface StrategiaIA {
    
    /**
     * Sceglie quale carta giocare tra quelle disponibili nella mano.
     * 
     * @param mano La lista di carte attualmente in mano all'AI.
     * @param tavolo Le carte attualmente giocate sul tavolo nella presa corrente.
     * @param briscola Il seme di briscola della partita.
     * @return La carta selezionata per essere giocata.
     */
    Carta scegli(List<Carta> mano, List<Carta> tavolo, Seme briscola);
}
