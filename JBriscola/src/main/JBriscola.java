package main; // Oppure il package di default

import java.util.Arrays;
import java.util.List;

import javax.swing.SwingUtilities;

import controller.GameController;
import model.Giocatore;
import model.GiocatoreAI;
import model.GiocatoreUmano;
import model.PartitaModel;
import model.StrategiaRandom;
import view.BriscolaView;

public class JBriscola {

    public static void main(String[] args) {
        
        // Avvia l'interfaccia grafica nel thread dedicato di Swing
        SwingUtilities.invokeLater(() -> {
            
            // 1. Inizializza i dati base per il Model
            GiocatoreUmano umano = new GiocatoreUmano("Player");
            GiocatoreAI bot = new GiocatoreAI("Bot CPU", new StrategiaRandom());
            List<Giocatore> giocatori = Arrays.asList(umano, bot);
            
            // 2. Crea il Model
            PartitaModel modello = new PartitaModel(giocatori);
            
            // 3. Crea la View
            BriscolaView vista = new BriscolaView();
            
            // 4. Crea il Controller (che collega Model e View)
            GameController controller = new GameController(modello, vista);
            
            // 5. Mostra la finestra iniziale
            vista.setVisible(true);
            
            System.out.println("Applicazione avviata. Checkpoint 2 raggiunto!");
        });
    }
}
