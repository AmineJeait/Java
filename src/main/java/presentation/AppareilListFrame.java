package presentation;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import dao.Appareil;
import metier.GestionAppareil;

public class AppareilListFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private JTable table;
    private DefaultTableModel model;
    private final UserHomeFrame homeFrame;

    private GestionAppareil gestionAppareil = new GestionAppareil();

    public AppareilListFrame(UserHomeFrame homeFrame) {
        this.homeFrame = homeFrame;
        initialize();
        loadAppareils();
    }

    private void initialize() {
        setTitle("Liste des appareils");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setBounds(100, 100, 800, 500);
        setLocationRelativeTo(null);

        JPanel contentPane = new JPanel(new BorderLayout(10,10));
        contentPane.setBorder(new EmptyBorder(10,10,10,10));
        setContentPane(contentPane);

        JLabel title = new JLabel("Liste des appareils");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(title, BorderLayout.NORTH);

        model = new DefaultTableModel(
            new Object[]{"ID","Marque","Modèle","IMEI"}, 0
        ) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(model);
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);

        contentPane.add(new JScrollPane(table), BorderLayout.CENTER);

        JButton btnBack = new JButton("⬅ Retour");
        btnBack.addActionListener(e -> back());

        JPanel bottom = new JPanel();
        bottom.add(btnBack);
        contentPane.add(bottom, BorderLayout.SOUTH);
    }

    private void loadAppareils() {
        model.setRowCount(0);
        List<Appareil> appareils = gestionAppareil.lister();

        for (Appareil a : appareils) {
            model.addRow(new Object[]{
                a.getId(),
                a.getMarque(),
                a.getModele(),
                a.getIemi()
            });
        }
    }

    private void back() {
        homeFrame.setVisible(true); // ✅ SAME HOME
        dispose();
    }
}
