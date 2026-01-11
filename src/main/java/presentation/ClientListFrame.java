package presentation;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import dao.Client;
import exception.ObjectNotFound;
import metier.GestionClient;

public class ClientListFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private JTable table;
    private DefaultTableModel model;
    private final JFrame parentFrame;
    private GestionClient gestionClient = new GestionClient();

    public ClientListFrame(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        initialize();
        loadClients();
    }

    private void initialize() {
        setTitle("Liste des clients");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(parentFrame);

        JPanel contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);

        JLabel title = new JLabel("Liste des clients");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(title, BorderLayout.NORTH);

        model = new DefaultTableModel(
            new Object[]{"ID", "Nom complet", "Téléphone", "Adresse"}, 0
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

        JButton btnAdd = new JButton("➕ Ajouter client");
        JButton btnDelete = new JButton("🗑 Supprimer client");
        JButton btnBack = new JButton("⬅ Retour");

        btnAdd.addActionListener(e -> new AddClientFrame(this).setVisible(true));
        btnDelete.addActionListener(e -> deleteSelected());
        btnBack.addActionListener(e -> back());

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(btnAdd);
        bottom.add(btnDelete);
        bottom.add(btnBack);

        contentPane.add(bottom, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        revalidate();
        repaint();
    }

    public void loadClients() {
        model.setRowCount(0);
        List<Client> clients = gestionClient.lister();

        for (Client c : clients) {
            model.addRow(new Object[]{
                c.getId(),
                c.getNomComplet(),
                c.getTelephone(),
                c.getAdresse()
            });
        }
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                "Veuillez sélectionner un client",
                "Attention",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int clientId = (int) model.getValueAt(row, 0);
        Client c;
        try {
            c = gestionClient.rechercher(clientId);
        } catch (ObjectNotFound ex) {
            JOptionPane.showMessageDialog(this,
                "Client introuvable",
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (c.getReparations() != null && !c.getReparations().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Impossible de supprimer ce client car il a des réparations enregistrées",
                "Suppression interdite",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Supprimer ce client ?",
            "Confirmation",
            JOptionPane.YES_NO_OPTION
        );
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            gestionClient.supprimer(clientId);
            loadClients();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Erreur lors de la suppression du client",
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void back() {
        parentFrame.setVisible(true);
        dispose();
    }
}
