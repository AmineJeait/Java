package presentation;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import dao.Client;
import dao.Reparation;
import exception.ObjectNotFound;
import metier.GestionClient;
import metier.GestionReparation;

public class ClientDetailsFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private Client client;
    private final ClientListFrame parent;
    private GestionClient gestionClient = new GestionClient();
    @SuppressWarnings("unused")
	private GestionReparation gestionReparation = new GestionReparation();

    private JTable table;
    private DefaultTableModel model;

    public ClientDetailsFrame(int clientId, ClientListFrame parent) throws ObjectNotFound {
        this.parent = parent;
        try {
            client = gestionClient.rechercher(clientId);
        } catch (Exception e) {
            throw new ObjectNotFound("Client introuvable");
        }
        initialize();
        loadReparations();
    }

    private void initialize() {
        setTitle("Détails du client");
        setSize(800, 500);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);

        JLabel lblTitle = new JLabel("Client: " + client.getNomComplet());
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(lblTitle, BorderLayout.NORTH);

        model = new DefaultTableModel(
            new Object[]{"ID", "État", "Prix", "Date dépôt", "Nb appareils"}, 0
        ) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(26);
        contentPane.add(new JScrollPane(table), BorderLayout.CENTER);

        JButton btnBack = new JButton("⬅ Retour");
        btnBack.addActionListener(e -> back());

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(btnBack);
        contentPane.add(bottom, BorderLayout.SOUTH);
    }

    private void loadReparations() {
        model.setRowCount(0);
        List<Reparation> reparations = client.getReparations();
        if (reparations != null) {
            for (Reparation r : reparations) {
                model.addRow(new Object[]{
                    r.getId(),
                    r.getState(),
                    r.getPrix(),
                    r.getDateDepot(),
                    r.getAppareils() != null ? r.getAppareils().size() : 0
                });
            }
        }
    }

    private void back() {
        parent.setVisible(true);
        dispose();
    }
}
