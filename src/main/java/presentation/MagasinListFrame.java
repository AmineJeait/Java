package presentation;

import java.awt.*;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import dao.Magasin;
import exception.ObjectNotFound;
import metier.GestionMagasin;

@SuppressWarnings("serial")
public class MagasinListFrame extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    private final JFrame parent;
    private GestionMagasin gestionMagasin = new GestionMagasin();

    public MagasinListFrame(JFrame parent) {
        this.parent = parent;
        initialize();
        loadMagasins();
    }

    private void initialize() {
        setTitle("Gestion des magasins");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);

        JLabel title = new JLabel("Liste des magasins");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(title, BorderLayout.NORTH);

        model = new DefaultTableModel(
            new Object[]{"ID", "Nom", "Adresse"},
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
            new MagasinFormFrame(this, null).setVisible(true);
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

    private void loadMagasins() {
        model.setRowCount(0);
        List<Magasin> list = gestionMagasin.lister();

        for (Magasin m : list) {
            model.addRow(new Object[]{
                m.getId(),
                m.getName(),
                m.getAdresse()
            });
        }
    }

    private void edit() throws ObjectNotFound {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un magasin");
            return;
        }

        int id = (int) model.getValueAt(row, 0);
        Magasin m = gestionMagasin.rechercher(id);

        new MagasinFormFrame(this, m).setVisible(true);
        setVisible(false);
    }

    private void delete() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un magasin");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Supprimer ce magasin ?",
            "Confirmation",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            int id = (int) model.getValueAt(row, 0);
            gestionMagasin.supprimer(id);
            loadMagasins();
        }
    }

    private void back() {
        parent.setVisible(true);
        dispose();
    }

    public void refresh() {
        loadMagasins();
        setVisible(true);
    }
}
