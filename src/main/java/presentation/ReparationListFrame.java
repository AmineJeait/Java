package presentation;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import dao.Reparation;
import metier.GestionReparation;

public class ReparationListFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private JTable table;
    private DefaultTableModel model;

    private final int userId;
    private final UserHomeFrame homeFrame;

    private GestionReparation gestionReparation = new GestionReparation();

    public ReparationListFrame(int userId, UserHomeFrame homeFrame) {
        this.userId = userId;
        this.homeFrame = homeFrame;
        initialize();
        loadReparations();
    }

    private void initialize() {
        setTitle("Liste des réparations");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setBounds(100, 100, 1000, 600);
        setLocationRelativeTo(null);

        JPanel contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);

        JLabel title = new JLabel("Liste des réparations");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(title, BorderLayout.NORTH);

        model = new DefaultTableModel(
            new Object[]{"ID", "Client", "État", "Prix", "Date dépôt", "Nb appareils"}, 0
        ) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(26);

        // Hide ID column
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);

        // 👉 DOUBLE CLICK → DETAILS
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row != -1) {
                        int reparationId = (int) model.getValueAt(row, 0);

                        try {
                            ReparationDetailsFrame details =
                                new ReparationDetailsFrame(
                                    reparationId,
                                    ReparationListFrame.this
                                );

                            details.setVisible(true);
                            dispose();

                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(
                                ReparationListFrame.this,
                                ex.getMessage(),
                                "Erreur",
                                JOptionPane.ERROR_MESSAGE
                            );
                        }
                    }
                }
            }
        });

        contentPane.add(new JScrollPane(table), BorderLayout.CENTER);

        JButton btnAdd = new JButton("➕ Nouvelle réparation");
        JButton btnRefresh = new JButton("🔄 Actualiser");
        JButton btnBack = new JButton("⬅ Retour");

        btnAdd.addActionListener(e -> {
            new AddReparationFrame(userId, homeFrame).setVisible(true);
            dispose();
        });

        btnRefresh.addActionListener(e -> loadReparations());
        btnBack.addActionListener(e -> back());

        JPanel bottom = new JPanel();
        bottom.add(btnAdd);
        bottom.add(btnRefresh);
        bottom.add(btnBack);

        contentPane.add(bottom, BorderLayout.SOUTH);
        
        pack();
        revalidate();

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

    private void back() {
        homeFrame.setVisible(true);
        dispose();
    }
}
