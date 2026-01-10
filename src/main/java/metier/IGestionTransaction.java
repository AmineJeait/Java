package metier;

import java.util.List;

import dao.Transaction;
import exception.ObjectNotFound;

public interface IGestionTransaction {
	public void ajouter(Transaction transact);
	public void supprimer(int id);
	public void modifier(Transaction transact);
	public Transaction rechercher(int id) throws ObjectNotFound ;
	public List<Transaction> lister();
}
