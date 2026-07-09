package view;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import model.ProfiloUtente;

@SuppressWarnings("serial")
public class ProfiloPanel extends JPanel {
    private JTextField txtNickname = new JTextField(15);
    private JLabel lblAvatarPreview = new JLabel("Nessun avatar", SwingConstants.CENTER);
    private String percorsoAvatar = "default.png";
    private JButton btnSalva = new JButton("Salva Profilo");

    public ProfiloPanel() {
        setLayout(new GridLayout(5, 1, 10, 10));
        JButton btnCarica = new JButton("Scegli Avatar");

        add(new JLabel("Nickname:"));
        add(txtNickname);
        add(btnCarica);
        add(lblAvatarPreview);
        add(btnSalva);

        btnCarica.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File f = chooser.getSelectedFile();
                this.percorsoAvatar = f.getAbsolutePath();
                lblAvatarPreview.setText(f.getName());
                // Qui potresti anche aggiungere l'anteprima dell'immagine
            }
        });
    }

    public void aggiornaView(ProfiloUtente p) {
        txtNickname.setText(p.getNickname());
        this.percorsoAvatar = p.getPercorsoAvatar();
        lblAvatarPreview.setText(new File(p.getPercorsoAvatar()).getName());
    }

    // Getters per il Controller
    public JButton getBtnSalva() { return btnSalva; }
    public String getNickname() { return txtNickname.getText(); }
    public String getPercorsoAvatar() { return percorsoAvatar; }
}