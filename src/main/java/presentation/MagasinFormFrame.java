package presentation;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import dao.Magasin;
import metier.GestionMagasin;

@SuppressWarnings("serial")
public class MagasinFormFrame extends JFrame {

    private JTextField txtNom;
    private JTextField txtAdresse;

    private final MagasinListFrame parent;
    private final Magasin magasin;

    private GestionMagasin gestionMagasin = new GestionMagasin();

    public MagasinFormFrame(MagasinListFrame parent, Magasin magasin) {
        this.parent = parent;
        this.magasin = magasin;
        initialize();
        fillForm();
    }

    private void initialize() {
        setTitle(magasin == null ? "Ajouter magasin" : "Modifier magasin");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);

        JPanel form = new JPanel(new GridLayout(2, 2, 10, 10));

        form.add(new JLabel("Nom :"));
        txtNom = new JTextField();
        form.add(txtNom);

        form.add(new JLabel("Adresse :"));
        txtAdresse = new JTextField();
        form.add(txtAdresse);

        contentPane.add(form, BorderLayout.CENTER);

        JButton btnSave = new JButton("Enregistrer");
        JButton btnCancel = new JButton("Annuler");

        btnSave.addActionListener(e -> save());
        btnCancel.addActionListener(e -> cancel());

        JPanel bottom = new JPanel();
        bottom.add(btnSave);
        bottom.add(btnCancel);

        contentPane.add(bottom, BorderLayout.SOUTH);
    }

    private void fillForm() {
        if (magasin != null) {
            txtNom.setText(magasin.getName());
            txtAdresse.setText(magasin.getAdresse());
        }
    }

    private void save() {
        String nom = txtNom.getText().trim();
        String adresse = txtAdresse.getText().trim();

        if (nom.isEmpty() || adresse.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tous les champs sont obligatoires");
            return;
        }

        if (magasin == null) {
            Magasin m = Magasin.builder()
                    .name(nom)
                    .adresse(adresse)
                    .build();
            gestionMagasin.ajouter(m);
        } else {
            magasin.setName(nom);
            magasin.setAdresse(adresse);
            gestionMagasin.modifier(magasin);
        }

        parent.refresh();
        dispose();
    }

    private void cancel() {
        parent.setVisible(true);
        dispose();
    }
}
