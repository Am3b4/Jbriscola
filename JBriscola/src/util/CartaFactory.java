package util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import model.Carta;
import model.Seme;
import model.ValoreCarta;

public class CartaFactory {

    public static List<Carta> creaMazzo() {
        return Arrays.stream(Seme.values())
            
            // Per ogni 'seme', creiamo uno stream dei possibili 'valori'
            .flatMap(seme -> Arrays.stream(ValoreCarta.values())
                     
                     // Combiniamo il seme e il valore per creare una nuova Carta
                     .map(valore -> new Carta(seme, valore))
            )
            
            // Infine, raccogliamo tutte le carte generate in una Lista
            .collect(Collectors.toList());
    }
}
