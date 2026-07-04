package main;

import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import model.Carta;
import model.Giocatore;
import model.GiocatoreAI;
import model.GiocatoreUmano;
import model.PartitaModel;
import model.StrategiaRandom;

@SuppressWarnings("deprecation")
public class TestMain {

    public static void main(String[] args) {
        
        System.out.println("=== TEST PARTITA MODEL ===");

        // 1. Setup dei Giocatori
        GiocatoreUmano umano = new GiocatoreUmano("Player 1");
        // Creiamo un bot con una strategia random per semplicità di test
        GiocatoreAI bot = new GiocatoreAI("Bot Facile", new StrategiaRandom());
        
        List<Giocatore> giocatori = Arrays.asList(umano, bot);

        // 2. Creazione del Model
        PartitaModel partita = new PartitaModel(giocatori);

        // 3. Registrazione di un Observer fittizio (simula quello che farà la View)
        partita.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                System.out.println("[NOTIFICA OBSERVER] Lo stato è cambiato in: " + partita.getStato());
            }
        });

        // 4. Inizio Partita
        System.out.println("\n--- AVVIO PARTITA ---");
        partita.iniziaPartita();

        System.out.println("\nBriscola della partita: " + partita.getBriscola());
        System.out.println("Carte nel mazzo dopo la distribuzione: " + partita.getMazzo().size());
        System.out.println("Mano Umano: " + umano.getMano());
        System.out.println("Mano Bot: " + bot.getMano());

        // 5. Simulazione della Prima Mano
        System.out.println("\n--- SIMULAZIONE PRIMA PRESA ---");
        
        // Turno 1
        Giocatore turno1 = partita.getGiocatoreCorrente();
        System.out.println("Tocca a: " + turno1.getNome());
        
        // Il primo giocatore gioca la prima carta che ha in mano
        Carta cartaGiocata1 = turno1.getMano().get(0);
        System.out.println(turno1.getNome() + " gioca " + cartaGiocata1);
        partita.giocaCarta(cartaGiocata1);

        // Turno 2
        Giocatore turno2 = partita.getGiocatoreCorrente();
        System.out.println("Tocca a: " + turno2.getNome());
        
        // Il secondo giocatore gioca la prima carta che ha in mano
        Carta cartaGiocata2 = turno2.getMano().get(0);
        System.out.println(turno2.getNome() + " gioca " + cartaGiocata2);
        partita.giocaCarta(cartaGiocata2);

        // 6. Resoconto Post-Mano
        System.out.println("\n--- RISULTATO PRIMA MANO ---");
        System.out.println("Punteggio " + umano.getNome() + ": " + umano.getPunteggio());
        System.out.println("Punteggio " + bot.getNome() + ": " + bot.getPunteggio());
        
        System.out.println("\nCarte attuali in mano dopo aver pescato:");
        System.out.println("Mano Umano: " + umano.getMano());
        System.out.println("Mano Bot: " + bot.getMano());
        
        System.out.println("\nIl prossimo a giocare (il vincitore della mano) è: " + partita.getGiocatoreCorrente().getNome());
    }
}