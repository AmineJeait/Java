package presentation;

import java.awt.*;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import dao.Caisse;
import dao.User;
import metier.GestionCaisse;
import metier.GestionUser;

@SuppressWarnings("serial")
public class CaisseFormFrame extends JFrame {

    private JComboBox<User> cbUsers;
    private JTextField txtMontantReel;
    private JTextField txtMontantReparation;

    private final CaisseListFrame parent;
    private final Caisse caisse;

    private GestionCaisse gestionCaisse = new GestionCaisse();
    private GestionUser gestionUser = new GestionUser();

    public CaisseFormFrame(CaisseListFrame parent, Caisse caisse) {
        this.parent = parent;
        this.caisse = caisse;
        initialize();
        fillForm();
    }

    private void initialize() {
        setTitle(caisse == null ? "Ajouter caisse" : "Modifier caisse");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);

        JPanel form = new JPanel(new GridLayout(3, 2, 10, 10));

        form.add(new JLabel("Propriétaire :"));
        cbUsers = new JComboBox<>();
        loadUsers();
        form.add(cbUsers);

        form.add(new JLabel("Montant réel :"));
        txtMontantReel = new JTextField();
        form.add(txtMontantReel);

        form.add(new JLabel("Montant réparations :"));
        txtMontantReparation = new JTextField();
        form.add(txtMontantReparation);

        contentPane.add(form, BorderLayout.CENTER);

        JButton btnSave = new JButton("Enregistrer");
        JButton btnCancel = new JButton("Annuler");

        btnSave.addActionListener(e -> save());
        btnCancel.addActionListener(e -> cancel());

        JPanel bottom = new JPanel();
        bottom.add(btnSave);
        bottom.add(btnCancel);

        contentPane.add(bottom, BorderLayout.SOUTH);
    }

    private void loadUsers() {
        List<User> users = gestionUser.lister();
        for (User u : users) {
            cbUsers.addItem(u);
        }
    }

    private void fillForm() {
        if (caisse != null) {
            cbUsers.setSelectedItem(caisse.getUser());
            txtMontantReel.setText(String.valueOf(caisse.getMontantReel()));
            txtMontantReparation.setText(String.valueOf(caisse.getMontantReparation()));
            cbUsers.setEnabled(false); 
        }
    }

    private void save() {
        User user = (User) cbUsers.getSelectedItem();

        if (user == null) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un utilisateur");
            return;
        }

        try {
            double reel = Double.parseDouble(txtMontantReel.getText());
            double rep = Double.parseDouble(txtMontantReparation.getText());

            if (caisse == null) {
                Caisse c = new Caisse();
                c.setUser(user);
                c.setMontantReel(reel);
                c.setMontantReparation(rep);
                gestionCaisse.ajouter(c);
            } else {
                caisse.setMontantReel(reel);
                caisse.setMontantReparation(rep);
                gestionCaisse.modifier(caisse);
            }

            parent.refresh();
            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Montants invalides");
        }
    }

    private void cancel() {
        parent.setVisible(true);
        dispose();
    }
}
