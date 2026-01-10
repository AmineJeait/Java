package presentation;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;
import javax.swing.JOptionPane;

import dao.User;
import dao.Admin;
import metier.GestionUser;

public class LoginFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtUsername;
    private JPasswordField txtPassword;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                LoginFrame frame = new LoginFrame();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public LoginFrame() {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 700, 500);
        setLocationRelativeTo(null);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        contentPane.setLayout(new GridBagLayout());
        JPanel panel = new JPanel();
        GridBagConstraints gbc_panel = new GridBagConstraints();
        gbc_panel.gridx = 0;
        gbc_panel.gridy = 0;
        contentPane.add(panel, gbc_panel);
        panel.setLayout(new GridBagLayout());
        JLabel lblTitle = new JLabel("LOGIN");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        GridBagConstraints gbc_lblTitle = new GridBagConstraints();
        gbc_lblTitle.gridx = 0;
        gbc_lblTitle.gridy = 0;
        gbc_lblTitle.gridwidth = 2;
        gbc_lblTitle.insets = new Insets(10, 10, 20, 10);
        panel.add(lblTitle, gbc_lblTitle);
        JLabel lblUser = new JLabel("Nom d'utilisateur");
        GridBagConstraints gbc_lblUser = new GridBagConstraints();
        gbc_lblUser.gridx = 0;
        gbc_lblUser.gridy = 1;
        gbc_lblUser.anchor = GridBagConstraints.EAST;
        gbc_lblUser.insets = new Insets(5, 5, 5, 5);
        panel.add(lblUser, gbc_lblUser);
        txtUsername = new JTextField(18);
        GridBagConstraints gbc_txtUsername = new GridBagConstraints();
        gbc_txtUsername.gridx = 1;
        gbc_txtUsername.gridy = 1;
        gbc_txtUsername.anchor = GridBagConstraints.WEST;
        gbc_txtUsername.insets = new Insets(5, 5, 5, 5);
        panel.add(txtUsername, gbc_txtUsername);
        JLabel lblPass = new JLabel("Mot de passe");
        GridBagConstraints gbc_lblPass = new GridBagConstraints();
        gbc_lblPass.gridx = 0;
        gbc_lblPass.gridy = 2;
        gbc_lblPass.anchor = GridBagConstraints.EAST;
        gbc_lblPass.insets = new Insets(5, 5, 5, 5);
        panel.add(lblPass, gbc_lblPass);
        txtPassword = new JPasswordField(18);
        GridBagConstraints gbc_txtPassword = new GridBagConstraints();
        gbc_txtPassword.gridx = 1;
        gbc_txtPassword.gridy = 2;
        gbc_txtPassword.anchor = GridBagConstraints.WEST;
        gbc_txtPassword.insets = new Insets(5, 5, 5, 5);
        panel.add(txtPassword, gbc_txtPassword);
        JButton btnLogin = new JButton("Se connecter");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        GridBagConstraints gbc_btnLogin = new GridBagConstraints();
        gbc_btnLogin.gridx = 0;
        gbc_btnLogin.gridy = 3;
        gbc_btnLogin.gridwidth = 2;
        gbc_btnLogin.insets = new Insets(20, 10, 10, 10);
        panel.add(btnLogin, gbc_btnLogin);
        btnLogin.addActionListener(e -> {

            String username = txtUsername.getText().trim();
            String password = new String(txtPassword.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Veuillez remplir tous les champs",
                        "Erreur",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            GestionUser gu = new GestionUser();
            User user = gu.login(username, password);

            if (user != null) {
                dispose();

                // 
                if (user instanceof Admin) {
                    new AdminHomeFrame(user.getId()).setVisible(true);
                } else {
                    new UserHomeFrame(user.getId()).setVisible(true);
                }

            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Nom d'utilisateur ou mot de passe incorrect",
                        "Login échoué",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }
}
