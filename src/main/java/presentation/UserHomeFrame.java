package presentation;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class UserHomeFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private final int userId;

    public UserHomeFrame(int userId) {
        this.userId = userId;
        initialize();
    }

    private void initialize() {
        setTitle("Tableau de bord - Réparateur");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 900, 600);
        setLocationRelativeTo(null);

        setJMenuBar(createMenuBar());

        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(contentPane);

        JLabel lblTitle = new JLabel("Tableau de bord – Réparateur");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblTitle.setHorizontalAlignment(JLabel.CENTER);
        contentPane.add(lblTitle, BorderLayout.NORTH);

        JPanel panelCenter = new JPanel(new GridBagLayout());
        JLabel lblWelcome = new JLabel(
            "<html><center>"
          + "Bienvenue dans le système de gestion du repair shop<br><br>"
          + "Utilisez le menu en haut pour accéder aux fonctionnalités."
          + "</center></html>"
        );
        lblWelcome.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        panelCenter.add(lblWelcome);

        contentPane.add(panelCenter, BorderLayout.CENTER);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu menuReparations = new JMenu("Réparations");
        JMenuItem miReparations = new JMenuItem("Liste des réparations");
        miReparations.addActionListener(e -> openReparations());
        menuReparations.add(miReparations);

        JMenu menuAppareils = new JMenu("Appareils");
        JMenuItem miAppareils = new JMenuItem("Liste des appareils");
        miAppareils.addActionListener(e -> openAppareils());
        menuAppareils.add(miAppareils);
        
        JMenu menuClients = new JMenu("Clients");
        JMenuItem miClients = new JMenuItem("Liste des clients");
        miClients.addActionListener(e -> openClients());
        menuClients.add(miClients);
        
        JMenu menuCaisse = new JMenu("Caisse");
        JMenuItem miCaisse = new JMenuItem("Liste des clients");
        miCaisse.addActionListener(e -> openCaisse());
        menuCaisse.add(miCaisse);
        
        JMenu menuAccount = new JMenu("Compte");
        JMenuItem miLogout = new JMenuItem("Déconnexion");
        miLogout.addActionListener(e -> logout());
        menuAccount.add(miLogout);


        menuBar.add(menuReparations);
        menuBar.add(menuAppareils);
        menuBar.add(menuClients);
        menuBar.add(menuCaisse);

        return menuBar;
    }

    private void openReparations() {
        new ReparationListFrame(userId, this).setVisible(true);
        setVisible(false); 
    }

    private void openAppareils() {
        new AppareilListFrame(this).setVisible(true);
        setVisible(false); 
    }
    
    private void openClients() {
    	new ClientListFrame(this).setVisible(true);
        setVisible(false); 
    }
    
    private void openCaisse() {
    	new CaisseDetailsFrame(userId,this).setVisible(true);
        setVisible(false); 
    }
    
    
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Voulez-vous vraiment vous déconnecter ?",
            "Déconnexion",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            new LoginFrame().setVisible(true);
            dispose(); 
        }
    }
}
