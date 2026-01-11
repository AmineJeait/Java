package presentation;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import dao.Transaction;
import exception.ObjectNotFound;
import metier.GestionTransaction;

public class TransactionDetailsFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private final Transaction transaction;
    private final JFrame parent;

    private GestionTransaction gestionTransaction = new GestionTransaction();

    public TransactionDetailsFrame(int transactionId, JFrame parent) throws ObjectNotFound {
        this.parent = parent;
        this.transaction = gestionTransaction.rechercher(transactionId);
        initialize();
    }

    private void initialize() {
        setTitle("Détails transaction");
        setSize(450, 300);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel contentPane = new JPanel(new GridLayout(4,2,10,10));
        contentPane.setBorder(new EmptyBorder(15,15,15,15));
        setContentPane(contentPane);

        contentPane.add(new JLabel("Montant :"));
        contentPane.add(new JLabel(String.valueOf(transaction.getMontant())));

        contentPane.add(new JLabel("Description :"));
        contentPane.add(new JLabel(transaction.getDescription()));

        contentPane.add(new JLabel("Date :"));
        contentPane.add(new JLabel(transaction.getDate().toString()));

        JButton btnClose = new JButton("Fermer");
        btnClose.addActionListener(e -> {
            parent.setVisible(true);
            dispose();
        });

        contentPane.add(new JLabel());
        contentPane.add(btnClose);
    }
}
