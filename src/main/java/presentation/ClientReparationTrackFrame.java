package presentation;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import dao.Reparation;
import exception.ObjectNotFound;
import metier.GestionReparation;

public class ClientReparationTrackFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private final int reparationId;
    private final LoginFrame parent;

    private GestionReparation gestionReparation = new GestionReparation();

    public ClientReparationTrackFrame(int reparationId, LoginFrame parent) {
        this.reparationId = reparationId;
        this.parent = parent;
        initialize();
    }

    private void initialize() {
        setTitle("Suivi de réparation");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(new EmptyBorder(15, 15, 15, 15));
        setContentPane(contentPane);

        JLabel title = new JLabel("Suivi de votre réparation");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(title, BorderLayout.NORTH);

        JPanel panelInfo = new JPanel(new GridLayout(0, 2, 10, 10));

        try {
            Reparation r = gestionReparation.rechercher(reparationId);

            panelInfo.add(new JLabel("ID réparation :"));
            panelInfo.add(new JLabel(String.valueOf(r.getId())));

            panelInfo.add(new JLabel("État :"));
            panelInfo.add(new JLabel(r.getState()));

            panelInfo.add(new JLabel("Prix :"));
            panelInfo.add(new JLabel(String.valueOf(r.getPrix())));

            panelInfo.add(new JLabel("Date dépôt :"));
            panelInfo.add(new JLabel(String.valueOf(r.getDateDepot())));

            panelInfo.add(new JLabel("Nombre d'appareils :"));
            panelInfo.add(new JLabel(
                r.getAppareils() != null
                    ? String.valueOf(r.getAppareils().size())
                    : "0"
            ));

        } catch (ObjectNotFound e) {
            JOptionPane.showMessageDialog(
                this,
                "Aucune réparation trouvée avec cet ID",
                "Erreur",
                JOptionPane.ERROR_MESSAGE
            );
            back();
            return;
        }

        contentPane.add(panelInfo, BorderLayout.CENTER);

        JButton btnBack = new JButton("⬅ Retour");
        btnBack.addActionListener(e -> back());

        JPanel bottom = new JPanel();
        bottom.add(btnBack);

        contentPane.add(bottom, BorderLayout.SOUTH);
    }

    private void back() {
        parent.setVisible(true);
        dispose();
    }
}
