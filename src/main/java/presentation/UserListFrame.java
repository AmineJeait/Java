package presentation;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import dao.User;
import exception.ObjectNotFound;
import dao.Admin;
import dao.Magasin;
import metier.GestionUser;

public class UserListFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel contentPane;
    private JTable table;
    private DefaultTableModel model;

    private AdminHomeFrame parent;
    private GestionUser gestionUser = new GestionUser();

    public UserListFrame(AdminHomeFrame parent) {
        this.parent = parent;
        initialize();
        loadUsers();
    }

    private void initialize() {
        setTitle("Gestion des utilisateurs");
        setSize(1000, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        contentPane = new JPanel(new BorderLayout(15, 15));
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(contentPane);


        JLabel lblTitle = new JLabel("Liste des utilisateurs");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(lblTitle, BorderLayout.NORTH);

 
        model = new DefaultTableModel(
                new Object[]{"ID", "Nom", "Prénom", "Username", "Rôle", "Magasin"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(26);
        JScrollPane scrollPane = new JScrollPane(table);
        contentPane.add(scrollPane, BorderLayout.CENTER);


        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        JButton btnBack = new JButton("⬅ Retour");
        JButton btnAdd = new JButton("➕ Ajouter");
        JButton btnEdit = new JButton("✏ Modifier");
        JButton btnDelete = new JButton("🗑 Supprimer");
        JButton btnReparations = new JButton("🔧 Réparations");

        panelButtons.add(btnBack);
        panelButtons.add(btnAdd);
        panelButtons.add(btnEdit);
        panelButtons.add(btnDelete);
        panelButtons.add(btnReparations);

        contentPane.add(panelButtons, BorderLayout.SOUTH);

        btnBack.addActionListener(e -> {
            parent.setVisible(true);
            dispose();
        });

        btnAdd.addActionListener(e -> {
            new AddUserFrame(this).setVisible(true);
            setVisible(false);
        });

        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Sélectionnez un utilisateur");
                return;
            }

            int userId = (int) model.getValueAt(row, 0);

            try {
                new EditUserFrame(this, userId).setVisible(true);
                setVisible(false);
            } catch (ObjectNotFound ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Sélectionnez un utilisateur");
                return;
            }

            String role = model.getValueAt(row, 4).toString();
            if ("ADMIN".equals(role)) {
                JOptionPane.showMessageDialog(this,
                        "Impossible de supprimer un administrateur");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Supprimer cet utilisateur ?",
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                int userId = (int) model.getValueAt(row, 0);
                gestionUser.supprimer(userId);
                loadUsers();
            }
        });

 
        btnReparations.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this,
                        "Sélectionnez un utilisateur");
                return;
            }

            int userId = (int) model.getValueAt(row, 0);

            new UserReparationListFrame(this, userId).setVisible(true);
            setVisible(false);
        });
    }

    void loadUsers() {
        model.setRowCount(0);

        List<User> users = gestionUser.lister();
        for (User u : users) {

            String role = (u instanceof Admin) ? "ADMIN" : "REPARATEUR";

            Magasin m = u.getMagasin();
            String magasinNom = (m != null) ? m.getName() : "-";

            model.addRow(new Object[]{
                    u.getId(),
                    u.getNom(),
                    u.getPrenom(),
                    u.getUsername(),
                    role,
                    magasinNom
            });
        }
    }
}
