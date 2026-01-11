package presentation;

import java.awt.*;
import java.time.Instant;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import dao.Caisse;
import dao.Transaction;
import exception.ObjectNotFound;
import metier.GestionCaisse;
import metier.GestionTransaction;

public class CaisseDetailsFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private final int userId;
    private final UserHomeFrame homeFrame;

    private JTable table;
    private DefaultTableModel model;

    private JLabel lblMontantReel;
    private JLabel lblMontantReparation;
    private JLabel lblTotalPretRecu;
    private JLabel lblTotalPretDonne;

    private Caisse caisse;

    private GestionCaisse gestionCaisse = new GestionCaisse();
    private GestionTransaction gestionTransaction = new GestionTransaction();

    public CaisseDetailsFrame(int userId, UserHomeFrame homeFrame) {
        this.userId = userId;
        this.homeFrame = homeFrame;
        initialize();
        loadCaisse();
    }

 
    private void initialize() {
        setTitle("Détails de la caisse");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);

        JPanel header = new JPanel(new BorderLayout());

        JLabel title = new JLabel("Caisse");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        header.add(title, BorderLayout.NORTH);

        JPanel panelInfo = new JPanel(new GridLayout(2, 4, 10, 5));

        panelInfo.add(new JLabel("Montant réel :"));
        lblMontantReel = new JLabel("0");
        panelInfo.add(lblMontantReel);

        panelInfo.add(new JLabel("Montant réparation :"));
        lblMontantReparation = new JLabel("0");
        panelInfo.add(lblMontantReparation);

        panelInfo.add(new JLabel("Total prêts reçus :"));
        lblTotalPretRecu = new JLabel("0");
        panelInfo.add(lblTotalPretRecu);

        panelInfo.add(new JLabel("Total prêts donnés :"));
        lblTotalPretDonne = new JLabel("0");
        panelInfo.add(lblTotalPretDonne);

        header.add(panelInfo, BorderLayout.SOUTH);
        contentPane.add(header, BorderLayout.NORTH);

        model = new DefaultTableModel(
            new Object[]{"ID", "Montant", "Description", "Date"}, 0
        ) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(26);

        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);

        table.getColumnModel().getColumn(1)
             .setCellRenderer(new MontantRenderer());

        contentPane.add(new JScrollPane(table), BorderLayout.CENTER);

 
        JButton btnPrendrePret = new JButton("➕ Prendre un prêt");
        JButton btnDonnerPret = new JButton("➖ Donner un prêt");
        JButton btnRecupererPret = new JButton("🔄 Récupérer un prêt");
        JButton btnBack = new JButton("⬅ Retour");

        btnPrendrePret.addActionListener(e -> pret(true));
        btnDonnerPret.addActionListener(e -> pret(false));
        btnRecupererPret.addActionListener(e -> recupererPret());
        btnBack.addActionListener(e -> back());

        JPanel bottom = new JPanel(new BorderLayout());

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT));
        left.add(btnPrendrePret);
        left.add(btnDonnerPret);
        left.add(btnRecupererPret);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        right.add(btnBack);

        bottom.add(left, BorderLayout.WEST);
        bottom.add(right, BorderLayout.EAST);

        contentPane.add(bottom, BorderLayout.SOUTH);
    }


    private void loadCaisse() {
        try {
            caisse = gestionCaisse.rechercherParUser(userId);
            refreshHeader();
            loadTransactions();
        } catch (ObjectNotFound e) {
            JOptionPane.showMessageDialog(this, "Aucune caisse trouvée");
            back();
        }
    }

    private void refreshHeader() {
        lblMontantReel.setText(String.valueOf(caisse.getMontantReel()));
        lblMontantReparation.setText(String.valueOf(caisse.getMontantReparation()));
    }

    private void loadTransactions() {
        model.setRowCount(0);

        double totalRecu = 0;
        double totalDonne = 0;

        List<Transaction> transactions =
                gestionTransaction.findByCaisse(caisse);

        for (Transaction t : transactions) {
            model.addRow(new Object[]{
                t.getId(),
                t.getMontant(),
                t.getDescription(),
                t.getDate()
            });

            if ("Prêt reçu".equals(t.getDescription())) {
                totalRecu += t.getMontant();
            }

            if ("Prêt donné".equals(t.getDescription())) {
                totalDonne += Math.abs(t.getMontant());
            }
        }

        lblTotalPretRecu.setText(String.valueOf(totalRecu));
        lblTotalPretDonne.setText(String.valueOf(totalDonne));
    }

    private void pret(boolean isPrendre) {
        String input = JOptionPane.showInputDialog(
            this,
            isPrendre ? "Montant du prêt reçu :" : "Montant du prêt donné :",
            "Prêt",
            JOptionPane.PLAIN_MESSAGE
        );

        if (input == null) return;

        try {
            double montant = Double.parseDouble(input);
            if (!isPrendre) montant = -montant;

            Transaction t = new Transaction();
            t.setCaisse(caisse);
            t.setMontant(montant);
            t.setDescription(isPrendre ? "Prêt reçu" : "Prêt donné");
            t.setDate(Instant.now());

            gestionTransaction.ajouter(t);

            caisse.setMontantReel(caisse.getMontantReel() + montant);
            gestionCaisse.modifier(caisse);

            refreshHeader();
            loadTransactions();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Montant invalide");
        }
    }

    private void recupererPret() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionne un prêt");
            return;
        }

        int transactionId = (int) model.getValueAt(row, 0);
        double montant = (double) model.getValueAt(row, 1);
        String desc = (String) model.getValueAt(row, 2);

        if (!desc.startsWith("Prêt")) {
            JOptionPane.showMessageDialog(this, "Ce n'est pas un prêt");
            return;
        }

        if (pretDejaRecupere(transactionId)) {
            JOptionPane.showMessageDialog(
                this,
                "Ce prêt a déjà été récupéré",
                "Action interdite",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        Transaction t = new Transaction();
        t.setCaisse(caisse);
        t.setMontant(-montant);
        t.setDescription("Récupération #" + transactionId);
        t.setDate(Instant.now());

        gestionTransaction.ajouter(t);

        caisse.setMontantReel(caisse.getMontantReel() - montant);
        gestionCaisse.modifier(caisse);

        refreshHeader();
        loadTransactions();
    }

    private boolean pretDejaRecupere(int transactionId) {
        List<Transaction> transactions =
                gestionTransaction.findByCaisse(caisse);

        for (Transaction t : transactions) {
            if (t.getDescription() != null
                && t.getDescription().contains("Récupération #" + transactionId)) {
                return true;
            }
        }
        return false;
    }



    private void back() {
        homeFrame.setVisible(true);
        dispose();
    }

 

    @SuppressWarnings("serial")
	private static class MontantRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {

            Component c = super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);

            double montant = (double) value;
            c.setForeground(montant >= 0 ? new Color(0, 130, 0) : Color.RED);

            return c;
        }
    }
}
