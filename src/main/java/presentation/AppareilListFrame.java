package presentation;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import dao.Appareil;
import exception.ObjectNotFound;
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

        JPanel contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);

        JLabel title = new JLabel("Liste des appareils");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(title, BorderLayout.NORTH);

        model = new DefaultTableModel(
            new Object[]{"ID", "Marque", "Modèle", "IMEI"}, 0
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

   
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row != -1) {
                        int appareilId = (int) model.getValueAt(row, 0);
                        try {
							new AppareilDetailsFrame(appareilId, AppareilListFrame.this)
							    .setVisible(true);
						} catch (ObjectNotFound e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
                        dispose();
                    }
                }
            }
        });

        contentPane.add(new JScrollPane(table), BorderLayout.CENTER);
        
        

 
        JButton btnAdd = new JButton("➕ Ajouter appareil");
        JButton btnDelete = new JButton("🗑 Supprimer");
        JButton btnBack = new JButton("⬅ Retour");

        btnAdd.addActionListener(e -> {
            new AddAppareilFrame(this).setVisible(true);
            dispose();
        });

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

    public void loadAppareils() {
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

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(
                this,
                "Veuillez sélectionner un appareil",
                "Attention",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int appareilId = (int) model.getValueAt(row, 0);
        Appareil a;
        try {
            a = gestionAppareil.rechercher(appareilId);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                this,
                "Appareil introuvable",
                "Erreur",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }


        if (a.getReparation() != null) {
            String state = a.getReparation().getState();
            if ("EN_COURS".equals(state) || "TERMINEE".equals(state)) {
                JOptionPane.showMessageDialog(
                    this,
                    "Impossible de supprimer cet appareil car sa réparation est " + state,
                    "Suppression interdite",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            } else if ("EN_ATTENTE".equals(state)) {
                int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Cet appareil est lié à une réparation en attente. Supprimer quand même ?",
                    "Confirmer suppression",
                    JOptionPane.YES_NO_OPTION
                );
                if (confirm != JOptionPane.YES_OPTION) return;
            }
        }

 
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Supprimer définitivement cet appareil ?",
            "Confirmation",
            JOptionPane.YES_NO_OPTION
        );
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            gestionAppareil.supprimer(appareilId);
            loadAppareils();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                this,
                "Impossible de supprimer l'appareil",
                "Erreur",
                JOptionPane.ERROR_MESSAGE
            );
            ex.printStackTrace();
        }
    }


    private void back() {
        homeFrame.setVisible(true);
        dispose();
    }
}
