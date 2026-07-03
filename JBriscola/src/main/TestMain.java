package main;

import java.util.List;

import model.Carta;
import model.Seme;
import model.ValoreCarta;
import util.CartaFactory;

public class TestMain {

    public static void main(String[] args) {
        
        // ==========================================
        // TEST 1: CREAZIONE DEL MAZZO
        // ==========================================
        System.out.println("--- TEST 1: CREAZIONE MAZZO ---");
        List<Carta> mazzo = CartaFactory.creaMazzo();
        
        
        System.out.println("Numero totale di carte create: " + mazzo.size()); // Dovrebbe essere 40
        System.out.println("Stampo le prime 5 carte del mazzo (per verificare l'ordine):");
        
        // Uso un piccolo stream per stampare solo le prime 5
        mazzo.stream().limit(5).forEach(carta -> System.out.println("- " + carta));
        
        
        // ==========================================
        // TEST 2: LOGICA 'BATTECARTA' E PRIMO SEGNO
        // ==========================================
        System.out.println("\n--- TEST 2: REGOLE DI PRESA ---");
        Seme briscola = Seme.DENARI;
        System.out.println("Seme di Briscola corrente: " + briscola);
        
        // Simuliamo la prima carta giocata sul tavolo (il "primo segno che regna")
        Carta cartaSulTavolo = new Carta(Seme.SPADE, ValoreCarta.QUATTRO);
        System.out.println("\nCarta dominante sul tavolo: " + cartaSulTavolo);
        
        // Scenario A: Gioco una carta dello stesso seme ma più forte
        Carta cartaStessoSeme = new Carta(Seme.SPADE, ValoreCarta.RE);
        boolean risultatoA = cartaStessoSeme.batteCarta(cartaSulTavolo, briscola);
        System.out.println("Gioco " + cartaStessoSeme + " -> Vince la presa? " + risultatoA); // Atteso: true
        
        // Scenario B: Gioco un Asso (carta fortissima) ma di un seme diverso che NON è briscola
        Carta cartaSemeDiverso = new Carta(Seme.COPPE, ValoreCarta.ASSO);
        boolean risultatoB = cartaSemeDiverso.batteCarta(cartaSulTavolo, briscola);
        System.out.println("Gioco " + cartaSemeDiverso + " -> Vince la presa? " + risultatoB); // Atteso: false
        
        // Scenario C: Gioco il 2 di Briscola (carta con 0 punti, ma è briscola)
        Carta piccolaBriscola = new Carta(Seme.DENARI, ValoreCarta.DUE);
        boolean risultatoC = piccolaBriscola.batteCarta(cartaSulTavolo, briscola);
        System.out.println("Gioco " + piccolaBriscola + " -> Vince la presa? " + risultatoC); // Atteso: true
        
        // ==========================================
        // TEST 4: TAVOLO CON BRISCOLA
        // ==========================================
        
        // Scenario D: La carta sul tavolo è già una briscola
        System.out.println("\n--- TEST 3: TAVOLO CON BRISCOLA ---");
        Carta briscolaSulTavolo = new Carta(Seme.DENARI, ValoreCarta.FANTE);
        System.out.println("Nuova carta dominante sul tavolo: " + briscolaSulTavolo);
        
        // Provo a battere il Fante di briscola con un Re di briscola
        Carta grandeBriscola = new Carta(Seme.DENARI, ValoreCarta.RE);
        boolean risultatoD = grandeBriscola.batteCarta(briscolaSulTavolo, briscola);
        System.out.println("Gioco " + grandeBriscola + " -> Vince la presa? " + risultatoD); // Atteso: true
    }
}
