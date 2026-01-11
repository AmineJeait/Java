package presentation;

import java.awt.*;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import dao.Caisse;
import exception.ObjectNotFound;
import metier.GestionCaisse;

@SuppressWarnings("serial")
public class CaisseListFrame extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    private final JFrame parent;
    private GestionCaisse gestionCaisse = new GestionCaisse();

    public CaisseListFrame(JFrame parent) {
        this.parent = parent;
        initialize();
        loadCaisses();
    }

    private void initialize() {
        setTitle("Gestion des caisses");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);

        JLabel title = new JLabel("Liste des caisses");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(title, BorderLayout.NORTH);

        model = new DefaultTableModel(
            new Object[]{"ID", "Propriétaire", "Montant réel", "Montant réparations"},
            0
        ) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(26);

        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);

        contentPane.add(new JScrollPane(table), BorderLayout.CENTER);

        JButton btnAdd = new JButton("➕ Ajouter");
        JButton btnEdit = new JButton("✏ Modifier");
        JButton btnDelete = new JButton("🗑 Supprimer");
        JButton btnBack = new JButton("⬅ Retour");

        btnAdd.addActionListener(e -> {
            new CaisseFormFrame(this, null).setVisible(true);
            setVisible(false);
        });

        btnEdit.addActionListener(e -> {
			try {
				edit();
			} catch (ObjectNotFound e1) {

				e1.printStackTrace();
			}
		});
        btnDelete.addActionListener(e -> delete());
        btnBack.addActionListener(e -> back());

        JPanel bottom = new JPanel();
        bottom.add(btnAdd);
        bottom.add(btnEdit);
        bottom.add(btnDelete);
        bottom.add(btnBack);

        contentPane.add(bottom, BorderLayout.SOUTH);
    }

    private void loadCaisses() {
        model.setRowCount(0);

        List<Caisse> list = gestionCaisse.lister();
        for (Caisse c : list) {
            model.addRow(new Object[]{
                c.getId(),
                c.getUser().getNom(),
                c.getMontantReel(),
                c.getMontantReparation()
            });
        }
    }

    private void edit() throws ObjectNotFound {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez une caisse");
            return;
        }

        int id = (int) model.getValueAt(row, 0);
        Caisse c = gestionCaisse.rechercher(id);

        new CaisseFormFrame(this, c).setVisible(true);
        setVisible(false);
    }

    private void delete() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez une caisse");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Supprimer cette caisse ?",
            "Confirmation",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            int id = (int) model.getValueAt(row, 0);
            gestionCaisse.supprimer(id);
            loadCaisses();
        }
    }

    private void back() {
        parent.setVisible(true);
        dispose();
    }

    public void refresh() {
        loadCaisses();
        setVisible(true);
    }
}
