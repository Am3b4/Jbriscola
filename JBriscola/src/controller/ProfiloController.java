package controller;

import model.ProfiloUtente;
import util.ProfiloMananger;
import view.BriscolaView;
import java.io.IOException;

public class ProfiloController {
    private BriscolaView vista;

    public ProfiloController(BriscolaView vista) {
        this.vista = vista;
        inizializzaListener();
    }

    private void inizializzaListener() {
        vista.getProfiloPanel().getBtnSalva().addActionListener(e -> {
            String nick = vista.getProfiloPanel().getNickname();
            String avatar = vista.getProfiloPanel().getPercorsoAvatar();
            
            try {
                // Salva
                ProfiloMananger.salvaProfilo(new model.ProfiloUtente(nick, avatar));
                
                // FEEDBACK VISIVO (JOptionPane mancante)
                javax.swing.JOptionPane.showMessageDialog(vista, 
                    "Profilo salvato correttamente!", 
                    "Successo", 
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);
                
                vista.mostraMenu();
            } catch (Exception ex) {
                javax.swing.JOptionPane.showMessageDialog(vista, 
                    "Errore durante il salvataggio: " + ex.getMessage(), 
                    "Errore", 
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public void salvaDati(String nickname, String percorsoAvatar) {
        try {
            ProfiloUtente p = new ProfiloUtente(nickname, percorsoAvatar);
            ProfiloMananger.salvaProfilo(p);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}