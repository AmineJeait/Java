package presentation;

import java.awt.*;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import dao.Appareil;
import dao.Reparation;
import exception.ObjectNotFound;
import metier.GestionReparation;

public class ReparationDetailsFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel contentPane;
    private JTable tableAppareils;
    private DefaultTableModel modelAppareils;

    private GestionReparation gestionReparation = new GestionReparation();
    private Reparation reparation;
    private ReparationListFrame listFrame;

    // ===== ETAT =====
    private JLabel lblEtatValue;
    private JComboBox<String> comboEtat;
    private JPanel etatCardPanel;
    private CardLayout etatCardLayout;

    private JButton btnEdit;
    private JButton btnSave;
    private JButton btnCancel;

    public ReparationDetailsFrame(
            int reparationId,
            ReparationListFrame listFrame
    ) throws ObjectNotFound {

        this.listFrame = listFrame;
        this.reparation = gestionReparation.rechercher(reparationId);

        initialize();
        loadAppareils();
    }

    private void initialize() {
        setTitle("Détails de la réparation");
        setBounds(100, 100, 900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);

        JLabel lblTitle = new JLabel("Détails de la réparation");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(lblTitle, BorderLayout.NORTH);

        JPanel panelInfo = new JPanel(new GridLayout(0, 2, 10, 10));

        panelInfo.add(new JLabel("Client :"));
        panelInfo.add(new JLabel(reparation.getClient().getTelephone()));

        panelInfo.add(new JLabel("État :"));

        etatCardLayout = new CardLayout();
        etatCardPanel = new JPanel(etatCardLayout);

        lblEtatValue = new JLabel(reparation.getState());

        comboEtat = new JComboBox<>(new String[]{
            "EN_ATTENTE",
            "EN_COURS",
            "TERMINEE",
            "ANNULEE"
        });
        comboEtat.setSelectedItem(reparation.getState());

        etatCardPanel.add(lblEtatValue, "VIEW");
        etatCardPanel.add(comboEtat, "EDIT");
        etatCardLayout.show(etatCardPanel, "VIEW");

        panelInfo.add(etatCardPanel);

        panelInfo.add(new JLabel("Prix :"));
        panelInfo.add(new JLabel(String.valueOf(reparation.getPrix())));

        panelInfo.add(new JLabel("Date dépôt :"));
        panelInfo.add(new JLabel(String.valueOf(reparation.getDateDepot())));

        panelInfo.add(new JLabel("Description :"));
        panelInfo.add(new JLabel(reparation.getDescription()));

        contentPane.add(panelInfo, BorderLayout.WEST);

        // ===== TABLE APPAREILS =====
        modelAppareils = new DefaultTableModel(
            new Object[]{"Marque", "Modèle", "IMEI", "Description"}, 0
        ) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        tableAppareils = new JTable(modelAppareils);
        tableAppareils.setRowHeight(24);

        contentPane.add(new JScrollPane(tableAppareils), BorderLayout.CENTER);

        // ===== BUTTONS =====
        btnEdit = new JButton("✏ Modifier");
        btnSave = new JButton("💾 Enregistrer");
        btnCancel = new JButton("✖ Annuler");
        JButton btnBack = new JButton("⬅ Retour");

        btnSave.setVisible(false);
        btnCancel.setVisible(false);

        btnEdit.addActionListener(e -> enableEditMode());
        btnCancel.addActionListener(e -> cancelEdit());
        btnSave.addActionListener(e -> saveEtat());

        btnBack.addActionListener(e -> {
            listFrame.setVisible(true);
            dispose();
        });

        JPanel panelButtons = new JPanel();
        panelButtons.add(btnEdit);
        panelButtons.add(btnSave);
        panelButtons.add(btnCancel);
        panelButtons.add(btnBack);

        contentPane.add(panelButtons, BorderLayout.SOUTH);
    }

    private void loadAppareils() {
        modelAppareils.setRowCount(0);

        List<Appareil> appareils = reparation.getAppareils();
        if (appareils != null) {
            for (Appareil a : appareils) {
                modelAppareils.addRow(new Object[]{
                    a.getMarque(),
                    a.getModele(),
                    a.getIemi(),
                    a.getDescription()
                });
            }
        }
    }

    private void enableEditMode() {
        etatCardLayout.show(etatCardPanel, "EDIT");
        btnEdit.setVisible(false);
        btnSave.setVisible(true);
        btnCancel.setVisible(true);
    }

    private void cancelEdit() {
        comboEtat.setSelectedItem(reparation.getState());
        etatCardLayout.show(etatCardPanel, "VIEW");
        btnEdit.setVisible(true);
        btnSave.setVisible(false);
        btnCancel.setVisible(false);
    }

    private void saveEtat() {
        String newState = (String) comboEtat.getSelectedItem();

        reparation.setState(newState);
        gestionReparation.modifier(reparation);

        lblEtatValue.setText(newState);

        etatCardLayout.show(etatCardPanel, "VIEW");
        btnEdit.setVisible(true);
        btnSave.setVisible(false);
        btnCancel.setVisible(false);
    }
}
