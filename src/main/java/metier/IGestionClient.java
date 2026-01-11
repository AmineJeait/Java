package metier;

import java.util.List;

import dao.Client;
import exception.ObjectNotFound;

public interface IGestionClient {
	public void ajouter(Client client);
	public void supprimer(int id);
	public void modifier(Client client);
	public Client rechercher(int id) throws ObjectNotFound ;
	public List<Client> lister();
	public boolean existsByTelephone(String telephone);
}
