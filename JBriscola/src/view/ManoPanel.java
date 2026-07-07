package view;

import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

import model.Carta;
import util.ImageLoader;

/**
 * Pannello che mostra le carte in mano al giocatore umano.
 * Gestisce la creazione e l'aggiornamento dei CartaButton.
 */
@SuppressWarnings("serial")
public class ManoPanel extends JPanel {

    private List<CartaButton> carteBottoni;
    private boolean abilitata;
    private ImageLoader imageLoader;

    /**
     * Costruttore del pannello.
     * 
     * @param imageLoader L'istanza condivisa per caricare le immagini.
     */
    public ManoPanel(ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
        this.carteBottoni = new ArrayList<>();
        this.abilitata = false;

        // FlowLayout dispone le carte orizzontalmente, centrate.
        // I parametri 15, 10 sono lo spazio orizzontale e verticale tra le carte.
        setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        
        // Rende il pannello trasparente per mostrare lo sfondo del GamePanel
        setOpaque(false); 
    }

    /**
     * Aggiorna visivamente le carte in mano.
     * Rimuove i vecchi bottoni e ne crea di nuovi in base alla lista fornita.
     * 
     * @param carteInMano La lista di carte logiche provenienti dal Model.
     */
    public void aggiornaMano(List<Carta> carteInMano) {
        // 1. Pulisce la GUI e la lista interna
        removeAll();
        carteBottoni.clear();

        // 2. Ricrea un bottone per ogni carta in mano (massimo 3)
        for (Carta carta : carteInMano) {
            
            // COSTRUZIONE DEL PERCORSO IMMAGINE:
            // Assumiamo che le tue immagini si chiamino "Seme_Valore.png" (es. "BASTONI_ASSO.png").
            // Adatta questa stringa se hai nominato i file diversamente (es. in minuscolo).
        	String percorsoImg = imageLoader.getPercorsoCarta(carta);
            
            // Ridimensiona l'immagine (100x150 è una buona proporzione per le carte)
            var iconaScalata = imageLoader.getImmagineScalata(percorsoImg, 100, 150);
            
            // Crea il bottone grafico
            CartaButton btn = new CartaButton(carta, iconaScalata);
            btn.setEnabled(abilitata); // Abilita o disabilita il click
            
            // Lo aggiunge al pannello e alla lista di controllo
            carteBottoni.add(btn);
            add(btn);
        }

        // 3. Comunica a Swing di ricalcolare il layout e ridisegnare il pannello
        revalidate();
        repaint();
    }

    /**
     * Abilita o disabilita il click su tutte le carte della mano.
     * Molto utile per impedire all'utente di giocare durante il turno del Bot.
     * 
     * @param abilitata true per permettere di cliccare, false per bloccare.
     */
    public void setAbilitata(boolean abilitata) {
        this.abilitata = abilitata;
        for (CartaButton btn : carteBottoni) {
            btn.setEnabled(abilitata);
        }
    }

    /**
     * Restituisce i bottoni per permettere al Controller di agganciarvi i listener.
     */
    public List<CartaButton> getCarteBottoni() {
        return carteBottoni;
    }
}
