package metier;

import java.util.List;

import dao.Caisse;
import exception.ObjectNotFound;

public interface IGestionCaisse {
	public void ajouter(Caisse caisse);
	public void supprimer(int id);
	public void modifier(Caisse caisse);
	public Caisse rechercher(int id) throws ObjectNotFound ;
	public List<Caisse> lister();
}
