package util;

import java.awt.Image;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;

/**
 * Utilità per caricare e ridimensionare le immagini.
 * Implementa un sistema di caching tramite Map per ottimizzare le prestazioni.
 */
public class ImageLoader {

    // Cache che associa una stringa (percorso o percorso+dimensioni) all'immagine
    private final Map<String, ImageIcon> cache;

    public ImageLoader() {
        this.cache = new HashMap<>();
    }

    /**
     * Recupera un'immagine originale. Se non è in cache, la carica e la salva.
     * 
     * @param path Il percorso del file immagine.
     * @return L'ImageIcon caricata.
     */
    public ImageIcon getImmagine(String path) {
        // Se l'immagine è già stata caricata in precedenza, la peschiamo dalla RAM
        if (cache.containsKey(path)) {
            return cache.get(path);
        }

        // Altrimenti la carichiamo dal disco
        ImageIcon icona = new ImageIcon(path);
        
        // Impostiamo il percorso come descrizione (utile per la chiave di cache del ridimensionamento)
        icona.setDescription(path);
        
        // La salviamo in cache per la prossima volta
        cache.put(path, icona);
        
        return icona;
    }

    /**
     * Ridimensiona un'immagine (metodo specificato nell'UML).
     * Anche le immagini ridimensionate vengono salvate in cache per evitare
     * di fare calcoli grafici ripetuti sulla stessa carta.
     * 
     * @param iconaOriginale L'immagine da scalare.
     * @param larghezza La larghezza desiderata.
     * @param altezza L'altezza desiderata.
     * @return L'ImageIcon ridimensionata.
     */
    public ImageIcon scala(ImageIcon iconaOriginale, int larghezza, int altezza) {
        // Creiamo una chiave univoca per la cache unendo il nome del file e le dimensioni
        String pathOriginale = iconaOriginale.getDescription();
        String cacheKey = pathOriginale + "_" + larghezza + "x" + altezza;

        // Se abbiamo già scalato questa immagine a queste dimensioni, la restituiamo subito
        if (cache.containsKey(cacheKey)) {
            return cache.get(cacheKey);
        }

        // Altrimenti procediamo con l'operazione di ridimensionamento
        Image img = iconaOriginale.getImage();
        // SCALE_SMOOTH garantisce una buona qualità visiva per le carte
        Image imgScalata = img.getScaledInstance(larghezza, altezza, Image.SCALE_SMOOTH);
        ImageIcon iconaScalata = new ImageIcon(imgScalata);
        
        // Aggiorniamo la descrizione per coerenza
        iconaScalata.setDescription(cacheKey);

        // Salviamo la nuova immagine scalata nella cache
        cache.put(cacheKey, iconaScalata);
        
        return iconaScalata;
    }

    /**
     * Metodo di comodità (Helper) che unisce il caricamento e lo scaling in un solo passaggio.
     * Molto utile per creare rapidamente i pulsanti delle carte.
     */
    public ImageIcon getImmagineScalata(String path, int larghezza, int altezza) {
        ImageIcon iconaOriginale = getImmagine(path);
        return scala(iconaOriginale, larghezza, altezza);
    }
    
    /**
     * Svuota la cache. Utile se il gioco ha molte immagini e si vuole 
     * liberare memoria al termine di una partita.
     */
    public void pulisciCache() {
        cache.clear();
    }
    
    public String getPercorsoCarta(model.Carta carta) {
        // 1. Formatta il Seme (es. BASTONI -> Bastoni)
        String seme = carta.getSeme().name();
        seme = seme.substring(0, 1).toUpperCase() + seme.substring(1).toLowerCase();

        // 2. Traduce il Valore nel numero a due cifre
        String numero = "";
        switch (carta.getValore().name()) {
            case "ASSO": numero = "01"; break;
            case "DUE": numero = "02"; break;
            case "TRE": numero = "03"; break;
            case "QUATTRO": numero = "04"; break;
            case "CINQUE": numero = "05"; break;
            case "SEI": numero = "06"; break;
            case "SETTE": numero = "07"; break;
            case "FANTE": numero = "08"; break;
            case "CAVALLO": numero = "09"; break;
            case "RE": numero = "10"; break;
        }

        return "resources/img/cards/" + seme + numero + ".png";
    }

    /**
     * Restituisce il percorso esatto del dorso del mazzo
     */
    public String getPercorsoDorso() {
        return "resources/img/carte/_Dorso.png";
    }
}