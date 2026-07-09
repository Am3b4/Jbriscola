package util;

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * Gestore dell'audio di gioco tramite pattern Singleton.
 * Utilizza javax.sound.sampled.Clip come richiesto dalle specifiche.
 */
public class AudioMananger {

    // L'unica istanza statica della classe (Singleton)
    private static AudioMananger istanza;

    // Costruttore privato: impedisce di creare istanze con "new AudioManager()" da fuori
    private AudioMananger() {}

    /**
     * Metodo globale per ottenere l'unica istanza dell'AudioManager.
     */
    public static AudioMananger getInstance() {
        if (istanza == null) {
            istanza = new AudioMananger();
        }
        return istanza;
    }

    /**
     * Riproduce un file audio (.wav).
     * Usa un Thread separato per evitare che la GUI (finestra) si blocchi
     * mentre il suono viene caricato e riprodotto.
     * * @param nomeFile Il nome del file (es. "MettiCarta.wav")
     */
    public void riproduci(String nomeFile) {
        new Thread(() -> {
            try {
                File fileAudio = new File("resources/audio/" + nomeFile);
                if (fileAudio.exists()) {
                    AudioInputStream audioStream = AudioSystem.getAudioInputStream(fileAudio);
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioStream);
                    clip.start();
                    
                    // Nota: Non chiudiamo subito la clip, altrimenti il suono si interrompe istantaneamente.
                    // Java Garbage Collector pulirà la clip una volta terminata l'esecuzione.
                } else {
                    System.err.println("[AUDIO] File non trovato: " + nomeFile);
                }
            } catch (Exception e) {
                System.err.println("[AUDIO] Errore riproduzione " + nomeFile + ": " + e.getMessage());
            }
        }).start();
    }
}
