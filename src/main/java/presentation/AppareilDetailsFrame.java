package presentation;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import dao.Appareil;
import dao.Reparation;
import exception.ObjectNotFound;
import metier.GestionAppareil;

public class AppareilDetailsFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private final int appareilId;
    private final AppareilListFrame parent;

    private GestionAppareil gestionAppareil = new GestionAppareil();

    public AppareilDetailsFrame(int appareilId, AppareilListFrame parent) throws ObjectNotFound {
        this.appareilId = appareilId;
        this.parent = parent;
        initialize();
        loadData();
    }

    private JLabel lblMarque, lblModele, lblImei, lblRepaEtat, lblRepaPrix, lblRepaDate;
    private JTextArea txtDescription;

    private void initialize() {
        setTitle("Détails appareil");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setBounds(100, 100, 600, 400);
        setLocationRelativeTo(null);

        JPanel contentPane = new JPanel(new BorderLayout(10,10));
        contentPane.setBorder(new EmptyBorder(10,10,10,10));
        setContentPane(contentPane);

        JLabel title = new JLabel("Détails de l’appareil");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(title, BorderLayout.NORTH);

        JPanel info = new JPanel(new GridBagLayout());
        info.setBorder(BorderFactory.createTitledBorder("Informations"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6,6,6,6);
        gbc.anchor = GridBagConstraints.WEST;

        int y = 0;

        lblMarque = new JLabel();
        lblModele = new JLabel();
        lblImei = new JLabel();
        txtDescription = new JTextArea(3, 25);
        txtDescription.setEditable(false);
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);

        gbc.gridx = 0; gbc.gridy = y;
        info.add(new JLabel("Marque:"), gbc);
        gbc.gridx = 1;
        info.add(lblMarque, gbc);

        gbc.gridx = 0; gbc.gridy = ++y;
        info.add(new JLabel("Modèle:"), gbc);
        gbc.gridx = 1;
        info.add(lblModele, gbc);

        gbc.gridx = 0; gbc.gridy = ++y;
        info.add(new JLabel("IMEI:"), gbc);
        gbc.gridx = 1;
        info.add(lblImei, gbc);

        gbc.gridx = 0; gbc.gridy = ++y;
        info.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        info.add(new JScrollPane(txtDescription), gbc);
        gbc.gridx = 0; gbc.gridy = ++y;
        info.add(new JLabel("Réparation (si existe):"), gbc);

        JPanel repaPanel = new JPanel(new GridLayout(3,1));
        lblRepaEtat = new JLabel();
        lblRepaPrix = new JLabel();
        lblRepaDate = new JLabel();
        repaPanel.add(lblRepaEtat);
        repaPanel.add(lblRepaPrix);
        repaPanel.add(lblRepaDate);

        gbc.gridx = 1;
        info.add(repaPanel, gbc);

        contentPane.add(info, BorderLayout.CENTER);

        JButton btnBack = new JButton("⬅ Retour");
        btnBack.addActionListener(e -> back());
        JPanel bottom = new JPanel();
        bottom.add(btnBack);
        contentPane.add(bottom, BorderLayout.SOUTH);
    }

    private void loadData() throws ObjectNotFound {
        Appareil a = gestionAppareil.rechercher(appareilId);
        if(a != null){
            lblMarque.setText(a.getMarque());
            lblModele.setText(a.getModele());
            lblImei.setText(a.getIemi());
            txtDescription.setText(a.getDescription() != null ? a.getDescription() : "—");

            Reparation r = a.getReparation();
            if(r != null){
                lblRepaEtat.setText("État: " + r.getState());
                lblRepaPrix.setText("Prix: " + r.getPrix());
                lblRepaDate.setText("Date dépôt: " + r.getDateDepot());
            } else {
                lblRepaEtat.setText("—");
                lblRepaPrix.setText("—");
                lblRepaDate.setText("—");
            }
        }
    }

    private void back() {
        parent.setVisible(true);
        parent.loadAppareils();
        dispose();
    }
}
