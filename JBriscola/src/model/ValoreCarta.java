package model;

public enum ValoreCarta {
    // Ordine crescente di forza per facilitare il confronto
    DUE(0, 1),
    QUATTRO(0, 2),
    CINQUE(0, 3),
    SEI(0, 4),
    SETTE(0, 5),
    FANTE(2, 6),   
    CAVALLO(3, 7), 
    RE(4, 8),      
    TRE(10, 9),    
    ASSO(11, 10);  

    private final int punti;
    private final int ordine;

    ValoreCarta(int punti, int ordine) {
        this.punti = punti;
        this.ordine = ordine;
    }

    public int getPunti() {
        return punti;
    }
    
    public int getOrdine() {
        return ordine;
    }
}
