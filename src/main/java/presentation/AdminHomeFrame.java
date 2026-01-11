package presentation;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class AdminHomeFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private final int adminId;
    private JPanel contentPane;

    public AdminHomeFrame(int adminId) {
        this.adminId = adminId;
        initialize();
    }

    public int getAdminId() {
        return adminId;
    }

    private void initialize() {
        setTitle("Administration");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        contentPane = new JPanel(new BorderLayout(20, 20));
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(contentPane);

        JLabel lblTitle = new JLabel("Espace Administrateur");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(lblTitle, BorderLayout.NORTH);

        JPanel panelCenter = new JPanel(new GridLayout(2, 2, 20, 20));

        JButton btnUsers = createBigButton("Utilisateurs");
        JButton btnCaisses = createBigButton("Caisses");
        JButton btnMagasins = createBigButton("Magasins");
        JButton btnLogout = createBigButton("Déconnexion");

        panelCenter.add(btnUsers);
        panelCenter.add(btnCaisses);
        panelCenter.add(btnMagasins);
        panelCenter.add(btnLogout);

        contentPane.add(panelCenter, BorderLayout.CENTER);

 
        btnUsers.addActionListener(e -> {
            new UserListFrame(this).setVisible(true);
            setVisible(false);
        });

        btnCaisses.addActionListener(e -> {
        	new CaisseListFrame(this).setVisible(true);
            setVisible(false);
        });

        btnMagasins.addActionListener(e -> {
        	new MagasinListFrame(this).setVisible(true);
            setVisible(false);
        });

        btnLogout.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
    }

    private JButton createBigButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(200, 100));
        return btn;
    }
}
