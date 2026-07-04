package model;

import java.util.List;

/**
 * Rappresenta un giocatore controllato dall'Intelligenza Artificiale.
 * Utilizza il pattern Strategy per determinare quale carta giocare.
 */
public class GiocatoreAI extends Giocatore {

    // Riferimento all'interfaccia Strategy per la scelta della carta
    private StrategiaIA strategia;

    /**
     * Costruttore con iniezione della dipendenza (Dependency Injection).
     * 
     * @param nome Il nome del bot (es. "CPU 1").
     * @param strategia L'algoritmo (StrategiaAI) che questo bot utilizzera per giocare.
     */
    public GiocatoreAI(String nome, StrategiaIA strategia) {
        super(nome);
        this.strategia = strategia;
    }

    /**
     * Metodo per permettere al bot di scegliere quale carta giocare, 
     * delegando la decisione alla strategia iniettata.
     * 
     * @param tavolo Le carte attualmente giocate sul tavolo in questa presa.
     * @param briscola Il seme di briscola della partita corrente.
     * @return La carta selezionata dall'AI.
     */
    public Carta scegliCarta(List<Carta> tavolo, Seme briscola) {
        // Delega la scelta alla strategia, passandogli la mano attuale (ereditata da Giocatore),
        // lo stato del tavolo e la briscola.
        return strategia.scegli(this.mano, tavolo, briscola);
    }

    /**
     * Permette di cambiare la strategia del bot a runtime (opzionale ma utile 
     * se si vuole aumentare la difficoltà durante il gioco).
     * 
     * @param nuovaStrategia La nuova strategia da applicare.
     */
    public void setStrategia(StrategiaIA nuovaStrategia) {
        this.strategia = nuovaStrategia;
    }
}
