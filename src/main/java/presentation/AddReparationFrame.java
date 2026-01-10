package presentation;

import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import dao.Appareil;
import dao.Client;
import dao.Reparation;
import metier.GestionAppareil;
import metier.GestionClient;
import metier.GestionReparation;
import metier.GestionUser;

public class AddReparationFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private JComboBox<Client> comboClient;
    private JComboBox<String> comboEtat;
    private JTextArea txtDescription;
    private JTextField txtPrix;

    private JComboBox<Appareil> comboAppareil;
    private JTextField txtImei;

    private JTable tableAppareils;
    private DefaultTableModel appareilModel;

    private final int userId;
    private final UserHomeFrame homeFrame;

    private GestionReparation gestionReparation = new GestionReparation();
    private GestionClient gestionClient = new GestionClient();
    private GestionUser gestionUser = new GestionUser();
    private GestionAppareil gestionAppareil = new GestionAppareil();

    private List<Appareil> appareilsSelectionnes = new ArrayList<>();

    // ✅ MATCHES ReparationListFrame
    public AddReparationFrame(int userId, UserHomeFrame homeFrame) {
        this.userId = userId;
        this.homeFrame = homeFrame;
        initialize();
        loadData();
    }

    private void initialize() {
        setTitle("Nouvelle réparation");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);

        JLabel lblTitle = new JLabel("Nouvelle réparation");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(lblTitle, BorderLayout.NORTH);

        /* ================= LEFT FORM ================= */
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createTitledBorder("Informations réparation"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int y = 0;

        gbc.gridx = 0; gbc.gridy = y;
        panelForm.add(new JLabel("Client"), gbc);

        gbc.gridx = 1;
        comboClient = new JComboBox<>();
        panelForm.add(comboClient, gbc);

        gbc.gridx = 0; gbc.gridy = ++y;
        panelForm.add(new JLabel("État"), gbc);

        gbc.gridx = 1;
        comboEtat = new JComboBox<>(new String[]{
            "EN_ATTENTE", "EN_COURS", "TERMINEE"
        });
        panelForm.add(comboEtat, gbc);

        gbc.gridx = 0; gbc.gridy = ++y;
        panelForm.add(new JLabel("Description"), gbc);

        gbc.gridx = 1;
        txtDescription = new JTextArea(3, 20);
        panelForm.add(new JScrollPane(txtDescription), gbc);

        gbc.gridx = 0; gbc.gridy = ++y;
        panelForm.add(new JLabel("Prix"), gbc);

        gbc.gridx = 1;
        txtPrix = new JTextField();
        panelForm.add(txtPrix, gbc);

        contentPane.add(panelForm, BorderLayout.WEST);

        /* ================= APPAREILS ================= */
        JPanel panelApp = new JPanel(new BorderLayout(5, 5));
        panelApp.setBorder(BorderFactory.createTitledBorder("Appareils"));

        JPanel panelAdd = new JPanel(new FlowLayout(FlowLayout.LEFT));

        comboAppareil = new JComboBox<>();
        comboAppareil.setPreferredSize(new Dimension(220, 25));
        txtImei = new JTextField(15);

        JButton btnAdd = new JButton("➕ Ajouter");
        JButton btnDelete = new JButton("🗑 Supprimer");

        panelAdd.add(new JLabel("Modèle"));
        panelAdd.add(comboAppareil);
        panelAdd.add(new JLabel("IMEI"));
        panelAdd.add(txtImei);
        panelAdd.add(btnAdd);
        panelAdd.add(btnDelete);

        panelApp.add(panelAdd, BorderLayout.NORTH);

        appareilModel = new DefaultTableModel(
            new Object[]{"Marque", "Modèle", "IMEI"}, 0
        );
        tableAppareils = new JTable(appareilModel);

        panelApp.add(new JScrollPane(tableAppareils), BorderLayout.CENTER);
        contentPane.add(panelApp, BorderLayout.CENTER);

        /* ================= BUTTONS ================= */
        JPanel panelButtons = new JPanel();
        JButton btnSave = new JButton("💾 Enregistrer");
        JButton btnCancel = new JButton("❌ Annuler");

        panelButtons.add(btnSave);
        panelButtons.add(btnCancel);
        contentPane.add(panelButtons, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> ajouterAppareil());
        btnDelete.addActionListener(e -> supprimerAppareil());
        btnSave.addActionListener(e -> save());
        btnCancel.addActionListener(e -> {
            new ReparationListFrame(userId, homeFrame).setVisible(true);
            dispose();
        });
        
        
        pack();
        revalidate();
    }

    private void loadData() {
        gestionClient.lister().forEach(comboClient::addItem);

        Set<String> seen = new HashSet<>();
        for (Appareil a : gestionAppareil.lister()) {
            String key = a.getMarque() + "|" + a.getModele();
            if (seen.add(key)) {
                comboAppareil.addItem(a);
            }
        }
    }

    private void ajouterAppareil() {
        Appareil modele = (Appareil) comboAppareil.getSelectedItem();
        String imei = txtImei.getText().trim();

        if (modele == null || imei.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Modèle et IMEI obligatoires");
            return;
        }

        for (Appareil a : appareilsSelectionnes) {
            if (a.getIemi().equals(imei)) {
                JOptionPane.showMessageDialog(this, "IMEI déjà ajouté");
                return;
            }
        }

        Appareil a = new Appareil();
        a.setMarque(modele.getMarque());
        a.setModele(modele.getModele());
        a.setDescription(modele.getDescription());
        a.setIemi(imei);

        appareilsSelectionnes.add(a);
        appareilModel.addRow(new Object[]{
            a.getMarque(), a.getModele(), a.getIemi()
        });

        txtImei.setText("");
    }

    private void supprimerAppareil() {
        int row = tableAppareils.getSelectedRow();
        if (row != -1) {
            appareilsSelectionnes.remove(row);
            appareilModel.removeRow(row);
        }
    }

    private void save() {
        try {
            Reparation r = new Reparation();
            r.setClient((Client) comboClient.getSelectedItem());
            r.setUser(gestionUser.rechercher(userId));
            r.setState((String) comboEtat.getSelectedItem());
            r.setDescription(txtDescription.getText());
            r.setPrix(Double.parseDouble(txtPrix.getText()));
            r.setDateDepot(LocalDate.now());

            // 🔴 CHECK AGAINST ALL APPAREILS IN DB
            for (Appareil a : appareilsSelectionnes) {
                String imei = a.getIemi();

                if (gestionAppareil.existsByImei(imei)) {
                    JOptionPane.showMessageDialog(
                        this,
                        "❌ IMEI déjà existant dans la base : " + imei,
                        "Erreur IMEI",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return; // ⛔ stop everything
                }

                a.setReparation(r);
            }

            r.setAppareils(appareilsSelectionnes);

            gestionReparation.ajouter(r);

            JOptionPane.showMessageDialog(
                this,
                "✅ Réparation enregistrée avec succès",
                "Succès",
                JOptionPane.INFORMATION_MESSAGE
            );

            new ReparationListFrame(userId, homeFrame).setVisible(true);
            dispose();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                this,
                "❌ Erreur lors de l'enregistrement",
                "Erreur",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

}
