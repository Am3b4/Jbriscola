package main;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import model.Carta;
import model.Giocatore;
import model.GiocatoreAI;
import model.GiocatoreUmano;
import model.Mano;
import model.Mazzo;
import model.PartitaModel;
import model.Seme;
import model.StatoPartita;
import model.StrategiaRandom;
import model.ValoreCarta;
import util.CartaFactory;

public class TestMain {

    static int testPassati = 0;
    static int testFalliti = 0;

    public static void main(String[] args) {
        System.out.println("=== SUITE DI TEST JBriscola - MODEL ===\n");

        testCartaFactory();
        testValoreCarta();
        testBatteCarta();
        testMazzo();
        testGiocatoreEPunteggio();
        testMano();
        testPartitaCompleta();
        testPartiteMultiple(100);

        System.out.println("=== RIEPILOGO ===");
        System.out.println("Test passati: " + testPassati);
        System.out.println("Test falliti: " + testFalliti);
        System.out.println(testFalliti == 0 ? "Tutto ok." : "Attenzione: alcuni test sono falliti (vedi sopra).");
    }

    static void check(String descrizione, boolean condizione) {
        if (condizione) {
            testPassati++;
        } else {
            testFalliti++;
            System.out.println("  [FALLITO] " + descrizione);
        }
    }

    // 1. CartaFactory: composizione del mazzo
    static void testCartaFactory() {
        System.out.println("--- CartaFactory ---");
        List<Carta> mazzo = CartaFactory.creaMazzo();
        check("crea esattamente 40 carte (trovate: " + mazzo.size() + ")", mazzo.size() == 40);
        int puntiTotali = mazzo.stream().mapToInt(Carta::getPunti).sum();
        check("il totale punti del mazzo e' 120 (trovato: " + puntiTotali + ")", puntiTotali == 120);
        for (Seme s : Seme.values()) {
            long conteggio = mazzo.stream().filter(c -> c.getSeme() == s).count();
            check("ci sono 10 carte di " + s + " (trovate: " + conteggio + ")", conteggio == 10);
        }
        System.out.println();
    }

    // 2. ValoreCarta: punti vs forza (ordine) - sono due scale diverse, facile confonderle
    static void testValoreCarta() {
        System.out.println("--- ValoreCarta (punti e forza) ---");
        check("Asso vale 11 punti", ValoreCarta.ASSO.getPunti() == 11);
        check("Tre vale 10 punti", ValoreCarta.TRE.getPunti() == 10);
        check("Re vale 4 punti", ValoreCarta.RE.getPunti() == 4);
        check("Cavallo vale 3 punti", ValoreCarta.CAVALLO.getPunti() == 3);
        check("Fante vale 2 punti", ValoreCarta.FANTE.getPunti() == 2);
        check("Sette vale 0 punti (liscio)", ValoreCarta.SETTE.getPunti() == 0);
        check("in forza il Tre batte il Re", ValoreCarta.TRE.getOrdine() > ValoreCarta.RE.getOrdine());
        check("in forza l'Asso batte il Tre", ValoreCarta.ASSO.getOrdine() > ValoreCarta.TRE.getOrdine());
        check("in forza il Re batte il Cavallo", ValoreCarta.RE.getOrdine() > ValoreCarta.CAVALLO.getOrdine());
        check("in forza il Due e' il piu' debole di tutti", ValoreCarta.DUE.getOrdine() < ValoreCarta.SETTE.getOrdine());
        System.out.println();
    }

    // 3. Carta.batteCarta(): tutte le combinazioni delle regole
    static void testBatteCarta() {
        System.out.println("--- Carta.batteCarta() ---");
        Seme briscola = Seme.SPADE;
        Carta tre = new Carta(Seme.COPPE, ValoreCarta.TRE);
        Carta re = new Carta(Seme.COPPE, ValoreCarta.RE);
        Carta asso = new Carta(Seme.COPPE, ValoreCarta.ASSO);
        Carta due = new Carta(Seme.COPPE, ValoreCarta.DUE);
        Carta settediBastoni = new Carta(Seme.BASTONI, ValoreCarta.SETTE);
        Carta briscolaBassa = new Carta(briscola, ValoreCarta.DUE);
        Carta briscolaAlta = new Carta(briscola, ValoreCarta.ASSO);

        check("stesso seme: Tre batte Re", tre.batteCarta(re, briscola));
        check("stesso seme: Re non batte Tre", !re.batteCarta(tre, briscola));
        check("stesso seme: Asso batte Tre", asso.batteCarta(tre, briscola));
        check("stesso seme: Due non batte Asso", !due.batteCarta(asso, briscola));
        check("seme diverso senza briscola: non batte chi ha aperto", !settediBastoni.batteCarta(tre, briscola));
        check("briscola bassa batte carta alta non-briscola", briscolaBassa.batteCarta(asso, briscola));
        check("carta non-briscola non batte una briscola", !asso.batteCarta(briscolaBassa, briscola));
        check("tra due briscole vince l'ordine", briscolaAlta.batteCarta(briscolaBassa, briscola));
        System.out.println();
    }

    // 4. Mazzo: operazioni di base
    static void testMazzo() {
        System.out.println("--- Mazzo ---");
        Mazzo mazzo = new Mazzo();
        check("il mazzo appena creato ha 40 carte", mazzo.size() == 40);
        mazzo.mescola();
        check("dopo mescola() ha ancora 40 carte", mazzo.size() == 40);
        Carta primaCarta = mazzo.peek();
        Optional<Carta> pescata = mazzo.pesca();
        check("peek() e pesca() restituiscono la stessa carta", pescata.isPresent() && pescata.get() == primaCarta);
        check("dopo una pesca il mazzo ha 39 carte (trovate: " + mazzo.size() + ")", mazzo.size() == 39);
        while (!mazzo.isEmpty()) {
            mazzo.pesca();
        }
        check("dopo aver pescato tutto, isEmpty() e' true", mazzo.isEmpty());
        check("pesca() su mazzo vuoto restituisce Optional vuoto", !mazzo.pesca().isPresent());
        System.out.println();
    }

    // 5. Giocatore: mano, prese, punteggio
    static void testGiocatoreEPunteggio() {
        System.out.println("--- Giocatore: mano, prese, punteggio ---");
        GiocatoreAI g = new GiocatoreAI("Test", new StrategiaRandom());
        Carta c1 = new Carta(Seme.DENARI, ValoreCarta.ASSO); // 11 pt
        Carta c2 = new Carta(Seme.DENARI, ValoreCarta.RE);   // 4 pt
        g.aggiungiCarta(c1);
        g.aggiungiCarta(c2);
        check("dopo 2 aggiungiCarta la mano ha 2 carte", g.getMano().size() == 2);
        g.rimuoviCarta(c1);
        check("dopo rimuoviCarta la mano ha 1 carta", g.getMano().size() == 1);
        g.aggiungiPresa(Arrays.asList(c1, c2));
        check("getPunteggio() somma correttamente (11+4=15, trovato: " + g.getPunteggio() + ")", g.getPunteggio() == 15);
        System.out.println();
    }

    // 6. Mano (una presa)
    static void testMano() {
        System.out.println("--- Mano (una presa) ---");
        Seme briscola = Seme.SPADE;
        GiocatoreAI g1 = new GiocatoreAI("G1", new StrategiaRandom());
        GiocatoreAI g2 = new GiocatoreAI("G2", new StrategiaRandom());
        Mano mano = new Mano(briscola, 2);
        check("una Mano nuova non e' completa", !mano.isCompleta());

        Carta c1 = new Carta(Seme.COPPE, ValoreCarta.RE); // apre a Coppe, non briscola
        Carta c2 = new Carta(briscola, ValoreCarta.DUE);  // briscola debole

        mano.gioca(g1, c1);
        check("dopo 1 carta su 2 non e' ancora completa", !mano.isCompleta());
        mano.gioca(g2, c2);
        check("dopo 2 carte su 2 e' completa", mano.isCompleta());
        check("il totale punti della presa e' corretto (4+0=4)", mano.getPuntiTotali() == 4);
        check("vince chi ha giocato la briscola, anche se debole", mano.determinaVincitore() == g2);
        System.out.println();
    }

    // 7. Una partita completa e dettagliata: 1 umano (simulato) + 3 AI, come da specifica
    static void testPartitaCompleta() {
        System.out.println("--- Partita completa (1 umano simulato + 3 AI) ---");

        GiocatoreUmano umano = new GiocatoreUmano("Player 1");
        GiocatoreAI bot1 = new GiocatoreAI("Bot 1", new StrategiaRandom());
        GiocatoreAI bot2 = new GiocatoreAI("Bot 2", new StrategiaRandom());
        GiocatoreAI bot3 = new GiocatoreAI("Bot 3", new StrategiaRandom());
        List<Giocatore> giocatori = Arrays.asList(umano, bot1, bot2, bot3);

        PartitaModel partita = new PartitaModel(giocatori);

        int[] notifiche = {0};
        partita.addObserver((o, arg) -> notifiche[0]++);

        partita.iniziaPartita();
        check("dopo iniziaPartita() ognuno ha 3 carte in mano",
                giocatori.stream().allMatch(g -> g.getMano().size() == 3));

        int mazzoDopoDistribuzione = partita.getMazzo().size();
        check("il mazzo dopo la distribuzione ha 28 carte (40 - 4x3), trovate: " + mazzoDopoDistribuzione,
                mazzoDopoDistribuzione == 28);
        check("lo stato iniziale e' coerente col primo giocatore (umano -> ATTESA_GIOCATORE), trovato: " + partita.getStato(),
                partita.getStato() == StatoPartita.ATTESA_GIOCATORE);

        int guardia = 0;
        while (!partita.isPartitaFinita() && guardia < 1000) {
            guardia++;
            Giocatore corrente = partita.getGiocatoreCorrente();
            List<Carta> tavolo = partita.getManoCorrente().getCarteGiocate();
            Carta scelta;
            if (corrente instanceof GiocatoreAI) {
                scelta = ((GiocatoreAI) corrente).scegliCarta(tavolo, partita.getBriscola().getSeme());
            } else {
                // il giocatore umano e' simulato qui: gioca semplicemente la prima carta disponibile
                scelta = corrente.getMano().get(0);
            }
            partita.giocaCarta(scelta);
        }

        check("la partita termina entro un numero ragionevole di mosse (mosse: " + guardia + ")", guardia < 1000);
        check("a fine partita il mazzo e' vuoto", partita.getMazzo().isEmpty());
        check("a fine partita tutte le mani sono vuote",
                giocatori.stream().allMatch(g -> g.getMano().isEmpty()));

        int totale = giocatori.stream().mapToInt(Giocatore::getPunteggio).sum();
        System.out.println("  Punteggi finali: " + giocatori.stream()
                .map(g -> g.getNome() + "=" + g.getPunteggio()).collect(Collectors.joining(", ")));
        check("il totale dei punteggi e' 120 (nessuna carta persa), trovato: " + totale, totale == 120);

        List<Giocatore> classifica = partita.calcolaClassifica();
        check("calcolaClassifica() e' ordinata per punteggio decrescente",
                classifica.get(0).getPunteggio() >= classifica.get(classifica.size() - 1).getPunteggio());
        check("l'observer e' stato notificato almeno una volta (notifiche: " + notifiche[0] + ")", notifiche[0] > 0);
        System.out.println();
    }

    // 8. Stress test: tante partite di fila, solo AI, per stanare bug legati allo shuffle casuale
    static void testPartiteMultiple(int numero) {
        System.out.println("--- Stress test: " + numero + " partite di fila (solo AI) ---");
        int crash = 0;
        int totaleErrato = 0;
        for (int i = 1; i <= numero; i++) {
            try {
                List<Giocatore> giocatori = Arrays.asList(
                        new GiocatoreAI("A", new StrategiaRandom()),
                        new GiocatoreAI("B", new StrategiaRandom()),
                        new GiocatoreAI("C", new StrategiaRandom()),
                        new GiocatoreAI("D", new StrategiaRandom()));
                PartitaModel p = new PartitaModel(giocatori);
                p.iniziaPartita();
                int guardia = 0;
                while (!p.isPartitaFinita() && guardia < 1000) {
                    guardia++;
                    GiocatoreAI corrente = (GiocatoreAI) p.getGiocatoreCorrente();
                    List<Carta> tavolo = p.getManoCorrente().getCarteGiocate();
                    Carta scelta = corrente.scegliCarta(tavolo, p.getBriscola().getSeme());
                    p.giocaCarta(scelta);
                }
                int totale = giocatori.stream().mapToInt(Giocatore::getPunteggio).sum();
                if (totale != 120) {
                    totaleErrato++;
                }
            } catch (Exception e) {
                crash++;
                if (crash <= 3) {
                    System.out.println("  partita " + i + " ha lanciato " + e.getClass().getSimpleName() + ": " + e.getMessage());
                }
            }
        }
        System.out.println("  Partite andate in crash: " + crash + "/" + numero);
        System.out.println("  Partite completate ma con totale != 120: " + totaleErrato + "/" + numero);
        check(numero + " partite di fila senza crash e sempre con totale 120", crash == 0 && totaleErrato == 0);
        System.out.println();
    }
}