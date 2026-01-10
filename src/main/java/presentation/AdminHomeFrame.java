package presentation;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class AdminHomeFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    // 🔐 Logged admin ID
    private int adminId;

    // ✅ Constructor called after login
    public AdminHomeFrame(int adminId) {
        this.adminId = adminId;
        initialize();
    }

    private void initialize() {
        setTitle("Administration - Repair Shop");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 950, 650);
        setLocationRelativeTo(null);

        // ===== contentPane =====
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        // ===== Title =====
        JLabel lblTitle = new JLabel("Tableau de bord - Administration");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setHorizontalAlignment(JLabel.CENTER);
        contentPane.add(lblTitle, BorderLayout.NORTH);

        // ===== Center =====
        JPanel panelCenter = new JPanel(new GridBagLayout());
        contentPane.add(panelCenter, BorderLayout.CENTER);

        JPanel panelMenu = new JPanel(new GridBagLayout());

        GridBagConstraints gbc_panelMenu = new GridBagConstraints();
        gbc_panelMenu.anchor = GridBagConstraints.CENTER;
        panelCenter.add(panelMenu, gbc_panelMenu);

        // ===== Buttons =====
        JButton btnUsers = createButton("👤 Gestion des utilisateurs");
        JButton btnRepairs = createButton("🛠 Gestion des réparations");
        JButton btnStats = createButton("📊 Statistiques");
        JButton btnLogout = createButton("🚪 Déconnexion");

        // ===== Layout =====
        addButton(panelMenu, btnUsers, 0);
        addButton(panelMenu, btnRepairs, 1);
        addButton(panelMenu, btnStats, 2);
        addButton(panelMenu, btnLogout, 3);

        // ===== Actions =====
        /*
        btnUsers.addActionListener(e -> openUserManagement());
        btnRepairs.addActionListener(e -> openRepairManagement());
        btnStats.addActionListener(e -> openStats());
        btnLogout.addActionListener(e -> logout());*/
    }

    /* =========================
       HELPERS
       ========================= */

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        return btn;
    }

    private void addButton(JPanel panel, JButton btn, int y) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 30, 10, 30);
        panel.add(btn, gbc);
    }

    /* =========================
       NAVIGATION
       ========================= */

    /*private void openUserManagement() {
        new UserManagementFrame(adminId).setVisible(true);
        setVisible(false);
    }

    private void openRepairManagement() {
        new AdminRepairListFrame(adminId).setVisible(true);
        setVisible(false);
    }

    private void openStats() {
        new AdminStatsFrame(adminId).setVisible(true);
        setVisible(false);
    }

    private void logout() {
        new LoginFrame().setVisible(true);
        dispose();
    }*/
}
