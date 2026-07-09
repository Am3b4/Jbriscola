package util;

import java.util.Properties;
import java.io.*;
import model.ProfiloUtente;

public class ProfiloMananger {
    private static final String FILE_PROFILO = "profilo.properties";

    public static void salvaProfilo(model.ProfiloUtente p) throws IOException {
        Properties props = new Properties();
        props.setProperty("nickname", p.getNickname());
        props.setProperty("avatar", p.getPercorsoAvatar());
        // Salva su file
        props.store(new FileOutputStream(FILE_PROFILO), "Profilo Utente");
    }

    public static model.ProfiloUtente caricaProfilo() {
        Properties props = new Properties();
        try {
            props.load(new FileInputStream(FILE_PROFILO));
            return new model.ProfiloUtente(props.getProperty("nickname"), props.getProperty("avatar"));
        } catch (IOException e) {
            return new model.ProfiloUtente("Giocatore", "default.png"); // Default se non esiste
        }
    }
    
    public static void aggiornaStatistiche(boolean vittoria) {
        ProfiloUtente p = caricaProfilo();
        p.incrementaPartiteGiocate();
        if (vittoria) p.incrementaVinte();
        else p.incrementaPerse();
        
        try {
            salvaProfilo(p);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
