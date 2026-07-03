package model;

import java.util.List;
import java.util.Random;

/**
 * Implementazione della strategia AI che sceglie una carta in modo completamente casuale.
 */
public class StrategiaRandom implements StrategiaIA {
    
    private final Random random = new Random();

    @Override
    public Carta scegli(List<Carta> mano, List<Carta> tavolo, Seme briscola) {
        if (mano == null || mano.isEmpty()) {
            return null;
        }
        // Seleziona un indice casuale tra 0 e la dimensione della mano
        int indiceCasuale = random.nextInt(mano.size());
        return mano.get(indiceCasuale);
    }
}
