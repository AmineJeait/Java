package presentation;

import java.awt.*;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import dao.Reparation;
import metier.GestionReparation;

public class UserReparationListFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private JTable table;
    private DefaultTableModel model;

    private final int userId;
    private final UserListFrame parent;

    private GestionReparation gestionReparation = new GestionReparation();

    public UserReparationListFrame(UserListFrame parent, int userId) {
        this.parent = parent;
        this.userId = userId;
        initialize();
        loadReparations();
    }

    private void initialize() {
        setTitle("Réparations de l'utilisateur");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);

        JPanel contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);

        JLabel title = new JLabel("Liste des réparations");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(title, BorderLayout.NORTH);

        model = new DefaultTableModel(
            new Object[]{"ID", "Client", "État", "Prix", "Date dépôt", "Nb appareils"},
            0
        ) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(26);

   
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);

        contentPane.add(new JScrollPane(table), BorderLayout.CENTER);

        // ===== BOUTONS =====
        JButton btnDelete = new JButton("Supprimer");
        JButton btnRefresh = new JButton("Actualiser");
        JButton btnBack = new JButton("⬅ Retour");

        btnDelete.addActionListener(e -> deleteReparation());
        btnRefresh.addActionListener(e -> loadReparations());
        btnBack.addActionListener(e -> back());

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        bottom.add(btnDelete);
        bottom.add(btnRefresh);
        bottom.add(btnBack);

        contentPane.add(bottom, BorderLayout.SOUTH);
    }

    private void loadReparations() {
        model.setRowCount(0);

        List<Reparation> reparations =
            gestionReparation.listerParUser(userId);

        for (Reparation r : reparations) {
            model.addRow(new Object[]{
                r.getId(),
                r.getClient() != null ? r.getClient().getNomComplet() : "—",
                r.getState(),
                r.getPrix(),
                r.getDateDepot(),
                r.getAppareils() != null ? r.getAppareils().size() : 0
            });
        }
    }

    private void deleteReparation() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(
                this,
                "Sélectionnez une réparation",
                "Attention",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Supprimer cette réparation ?\nCette action est irréversible.",
            "Confirmation",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            int reparationId = (int) model.getValueAt(row, 0);
            gestionReparation.supprimer(reparationId);
            loadReparations();
        }
    }

    private void back() {
        parent.setVisible(true);
        dispose();
    }
}
