package presentation;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import dao.Client;
import metier.GestionClient;

public class AddClientFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel contentPane;
    private JTextField txtNom;
    private JTextField txtTelephone;
    private JTextField txtAdresse;

    private GestionClient gestionClient = new GestionClient();
    private ClientListFrame parent;
    public AddClientFrame(ClientListFrame parent) {
        this.parent = parent;
        initialize();
    }

    private void initialize() {
        setTitle("Ajouter un client");
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);

        JLabel lblTitle = new JLabel("Ajouter un client");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(lblTitle, BorderLayout.NORTH);

        JPanel panelForm = new JPanel(new GridBagLayout());
        contentPane.add(panelForm, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;


        gbc.gridx = 0; gbc.gridy = 0;
        panelForm.add(new JLabel("Nom complet"), gbc);

        gbc.gridx = 1;
        txtNom = new JTextField(20);
        panelForm.add(txtNom, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panelForm.add(new JLabel("Téléphone"), gbc);

        gbc.gridx = 1;
        txtTelephone = new JTextField(20);
        panelForm.add(txtTelephone, gbc);


        gbc.gridx = 0; gbc.gridy++;
        panelForm.add(new JLabel("Adresse"), gbc);

        gbc.gridx = 1;
        txtAdresse = new JTextField(20);
        panelForm.add(txtAdresse, gbc);

        JPanel panelButtons = new JPanel();

        JButton btnSave = new JButton("💾 Enregistrer");
        JButton btnCancel = new JButton("❌ Annuler");

        panelButtons.add(btnSave);
        panelButtons.add(btnCancel);

        contentPane.add(panelButtons, BorderLayout.SOUTH);

        btnSave.addActionListener(e -> save());
        btnCancel.addActionListener(e -> dispose());
    }

    private void save() {
        String nom = txtNom.getText().trim();
        String telephone = txtTelephone.getText().trim();
        String adresse = txtAdresse.getText().trim();

        if (nom.isEmpty() || telephone.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Nom et téléphone sont obligatoires",
                    "Validation",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (telephone.length() > 10) {
            JOptionPane.showMessageDialog(this,
                    "Téléphone invalide (max 10 caractères)",
                    "Validation",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            if (gestionClient.existsByTelephone(telephone)) {
                JOptionPane.showMessageDialog(this,
                        "Ce numéro de téléphone existe déjà",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            Client c = new Client();
            c.setNomComplet(nom);
            c.setTelephone(telephone);
            c.setAdresse(adresse);

            gestionClient.ajouter(c);

  
            parent.loadClients();

            JOptionPane.showMessageDialog(this, "Client ajouté avec succès");
            dispose();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Erreur lors de l'ajout du client",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
