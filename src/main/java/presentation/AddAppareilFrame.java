package presentation;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import dao.Appareil;
import metier.GestionAppareil;

public class AddAppareilFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel contentPane;
    private JTextField txtMarque;
    private JTextField txtModele;
    private JTextField txtIemi;
    private JTextArea txtDescription;

    private GestionAppareil gestionAppareil = new GestionAppareil();
    private AddReparationFrame parent;

    /* ================= CONSTRUCTOR ================= */
    public AddAppareilFrame(AddReparationFrame parent) {
        this.parent = parent;
        initialize();
    }

    /* ================= UI ================= */
    private void initialize() {
        setTitle("Nouvel appareil");
        setSize(480, 380);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);

        JLabel lblTitle = new JLabel("Nouvel appareil");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(lblTitle, BorderLayout.NORTH);

        JPanel panelForm = new JPanel(new GridBagLayout());
        contentPane.add(panelForm, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        /* ===== Marque ===== */
        gbc.gridx = 0; gbc.gridy = 0;
        panelForm.add(new JLabel("Marque"), gbc);

        gbc.gridx = 1;
        txtMarque = new JTextField(20);
        panelForm.add(txtMarque, gbc);

        /* ===== Modèle ===== */
        gbc.gridx = 0; gbc.gridy++;
        panelForm.add(new JLabel("Modèle"), gbc);

        gbc.gridx = 1;
        txtModele = new JTextField(20);
        panelForm.add(txtModele, gbc);

        /* ===== IMEI ===== */
        gbc.gridx = 0; gbc.gridy++;
        panelForm.add(new JLabel("IMEI"), gbc);

        gbc.gridx = 1;
        txtIemi = new JTextField(20);
        panelForm.add(txtIemi, gbc);

        /* ===== Description ===== */
        gbc.gridx = 0; gbc.gridy++;
        panelForm.add(new JLabel("Description"), gbc);

        gbc.gridx = 1;
        txtDescription = new JTextArea(3, 20);
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);
        panelForm.add(new JScrollPane(txtDescription), gbc);

        /* ===== Buttons ===== */
        JPanel panelButtons = new JPanel();

        JButton btnSave = new JButton("💾 Enregistrer");
        JButton btnCancel = new JButton("❌ Annuler");

        panelButtons.add(btnSave);
        panelButtons.add(btnCancel);

        contentPane.add(panelButtons, BorderLayout.SOUTH);

        btnSave.addActionListener(e -> save());
        btnCancel.addActionListener(e -> dispose());
    }

    /* ================= LOGIC ================= */
    private void save() {
        String marque = txtMarque.getText().trim();
        String modele = txtModele.getText().trim();
        String iemi = txtIemi.getText().trim();

        if (marque.isEmpty() || modele.isEmpty() || iemi.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Marque, modèle et IMEI sont obligatoires",
                    "Validation",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (iemi.length() > 15) {
            JOptionPane.showMessageDialog(this,
                    "IMEI invalide (max 15 caractères)",
                    "Validation",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Appareil a = new Appareil();
            a.setMarque(marque);
            a.setModele(modele);
            a.setIemi(iemi);
            a.setDescription(txtDescription.getText().trim());

            gestionAppareil.ajouter(a);

            // 🔁 Return to parent frame
            parent.AppareilListFrame(a);

            JOptionPane.showMessageDialog(this, "Appareil ajouté avec succès");
            dispose();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "IMEI déjà existant ou erreur lors de l'enregistrement",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
