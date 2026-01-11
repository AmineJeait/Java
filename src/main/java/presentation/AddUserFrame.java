package presentation;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import dao.Admin;
import dao.User;
import dao.Magasin;
import metier.GestionUser;
import metier.GestionMagasin;

public class AddUserFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private JTextField txtNom;
    private JTextField txtPrenom;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JComboBox<String> cbRole;
    private JComboBox<Magasin> cbMagasin;

    private UserListFrame parent;
    private GestionUser gestionUser = new GestionUser();
    private GestionMagasin gestionMagasin = new GestionMagasin();

    public AddUserFrame(UserListFrame parent) {
        this.parent = parent;
        initialize();
        loadMagasins();
    }

    private void initialize() {
        setTitle("Ajouter un utilisateur");
        setSize(450, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel contentPane = new JPanel(new BorderLayout(15, 15));
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(contentPane);


        JLabel lblTitle = new JLabel("Nouvel utilisateur");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(lblTitle, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(0, 2, 10, 10));

        form.add(new JLabel("Nom :"));
        txtNom = new JTextField();
        form.add(txtNom);

        form.add(new JLabel("Prénom :"));
        txtPrenom = new JTextField();
        form.add(txtPrenom);

        form.add(new JLabel("Username :"));
        txtUsername = new JTextField();
        form.add(txtUsername);

        form.add(new JLabel("Mot de passe :"));
        txtPassword = new JPasswordField();
        form.add(txtPassword);

        form.add(new JLabel("Rôle :"));
        cbRole = new JComboBox<>(new String[]{"REPARATEUR", "ADMIN"});
        form.add(cbRole);

        form.add(new JLabel("Magasin :"));
        cbMagasin = new JComboBox<>();
        form.add(cbMagasin);

        contentPane.add(form, BorderLayout.CENTER);

  
        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton btnCancel = new JButton("⬅ Annuler");
        JButton btnSave = new JButton("💾 Enregistrer");

        panelButtons.add(btnCancel);
        panelButtons.add(btnSave);

        contentPane.add(panelButtons, BorderLayout.SOUTH);

  
        btnCancel.addActionListener(e -> dispose());
        btnSave.addActionListener(e -> saveUser());
    }

    private void loadMagasins() {
        cbMagasin.addItem(null);

        List<Magasin> magasins = gestionMagasin.lister();
        for (Magasin m : magasins) {
            cbMagasin.addItem(m);
        }
    }

    private void saveUser() {

        if (txtNom.getText().trim().isEmpty()
                || txtPrenom.getText().trim().isEmpty()
                || txtUsername.getText().trim().isEmpty()
                || txtPassword.getPassword().length == 0) {

            JOptionPane.showMessageDialog(this,
                    "Tous les champs sont obligatoires");
            return;
        }

        String role = cbRole.getSelectedItem().toString();

        User user = "ADMIN".equals(role) ? new Admin() : new User();

        user.setNom(txtNom.getText());
        user.setPrenom(txtPrenom.getText());
        user.setUsername(txtUsername.getText());
        user.setPassword(new String(txtPassword.getPassword())); 
        user.setMagasin((Magasin) cbMagasin.getSelectedItem());

        gestionUser.ajouter(user);

        JOptionPane.showMessageDialog(this,
                "Utilisateur ajouté avec succès");


        parent.loadUsers();
        parent.setVisible(true);
        dispose();
    }
}
