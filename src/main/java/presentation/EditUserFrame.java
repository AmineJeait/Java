package presentation;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import dao.Admin;
import dao.User;
import exception.ObjectNotFound;
import dao.Magasin;
import metier.GestionUser;
import metier.GestionMagasin;

public class EditUserFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private JTextField txtNom;
    private JTextField txtPrenom;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JComboBox<String> cbRole;
    private JComboBox<Magasin> cbMagasin;

    private User user;
    private UserListFrame parent;

    private GestionUser gestionUser = new GestionUser();
    private GestionMagasin gestionMagasin = new GestionMagasin();

    public EditUserFrame(UserListFrame parent, int userId) throws ObjectNotFound {
        this.parent = parent;
        this.user = gestionUser.rechercher(userId);
        initialize();
        loadMagasins();
        fillForm();
    }

    private void initialize() {
        setTitle("Modifier utilisateur");
        setSize(450, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel contentPane = new JPanel(new BorderLayout(15, 15));
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(contentPane);

        JLabel lblTitle = new JLabel("Modifier utilisateur");
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
        txtUsername.setEnabled(false); 
        form.add(txtUsername);

        form.add(new JLabel("Nouveau mot de passe :"));
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

        JButton btnCancel = new JButton("Annuler");
        JButton btnSave = new JButton("Enregistrer");

        panelButtons.add(btnCancel);
        panelButtons.add(btnSave);

        contentPane.add(panelButtons, BorderLayout.SOUTH);

        btnCancel.addActionListener(e -> dispose());
        btnSave.addActionListener(e -> updateUser());
    }

    private void loadMagasins() {
        cbMagasin.addItem(null);
        List<Magasin> magasins = gestionMagasin.lister();
        for (Magasin m : magasins) {
            cbMagasin.addItem(m);
        }
    }

    private void fillForm() {
        txtNom.setText(user.getNom());
        txtPrenom.setText(user.getPrenom());
        txtUsername.setText(user.getUsername());

        if (user instanceof Admin) {
            cbRole.setSelectedItem("ADMIN");
        } else {
            cbRole.setSelectedItem("REPARATEUR");
        }

        cbMagasin.setSelectedItem(user.getMagasin());
    }

    private void updateUser() {

        if (txtNom.getText().trim().isEmpty()
                || txtPrenom.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this,
                    "Nom et prénom sont obligatoires");
            return;
        }

        boolean wasAdmin = user instanceof Admin;
        boolean willBeAdmin = "ADMIN".equals(cbRole.getSelectedItem());

        if (wasAdmin != willBeAdmin) {
            if (willBeAdmin) {
                Admin admin = new Admin();
                copyCommonFields(user, admin);
                user = admin;
            } else {
                User reparateur = new User();
                copyCommonFields(user, reparateur);
                user = reparateur;
            }
        }

        user.setNom(txtNom.getText());
        user.setPrenom(txtPrenom.getText());
        user.setMagasin((Magasin) cbMagasin.getSelectedItem());

        if (txtPassword.getPassword().length > 0) {
            user.setPassword(new String(txtPassword.getPassword())); 
        }

        gestionUser.modifier(user);

        JOptionPane.showMessageDialog(this,
                "Utilisateur modifié avec succès");

        parent.loadUsers();
        parent.setVisible(true);
        dispose();
    }

    private void copyCommonFields(User src, User dest) {
        dest.setId(src.getId());
        dest.setNom(src.getNom());
        dest.setPrenom(src.getPrenom());
        dest.setUsername(src.getUsername());
        dest.setPassword(src.getPassword());
        dest.setMagasin(src.getMagasin());
    }
}
