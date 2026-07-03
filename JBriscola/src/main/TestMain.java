package main;

import java.util.List;

import model.Carta;
import model.Mazzo;
import model.Seme;
import model.ValoreCarta;
import util.CartaFactory;

public class TestMain {

    public static void main(String[] args) {
        
        // ==========================================
        // TEST 4: GESTIONE DEL MAZZO
        // ==========================================
        System.out.println("\n--- TEST 4: GESTIONE MAZZO ---");
        Mazzo mazzo = new Mazzo();
        System.out.println("Mazzo creato. Carte iniziali: " + mazzo.size());

        mazzo.mescola();
        System.out.println("Mazzo mescolato. Peschiamo 3 carte:");

        for (int i = 0; i < 3; i++) {
        	// Usiamo ifPresentOrElse per gestire l'Optional in modo pulito
        	mazzo.pesca().ifPresent(c -> System.out.println("Pescata: " + c));
        }

        System.out.println("Carte rimanenti: " + mazzo.size());

        for (int i = 0; i < 35; i++) {
        	mazzo.pesca();
        }

        System.out.println("Carte rimanenti: " + mazzo.size());
        
    	mazzo.pesca().ifPresent(c -> System.out.println("Pescata: " + c));
    	mazzo.pesca().ifPresent(c -> System.out.println("Pescata: " + c));
    	mazzo.pesca().ifPresent(c -> System.out.println("Pescata: " + c)); // Non dovrebbe printare
        
    }
}
