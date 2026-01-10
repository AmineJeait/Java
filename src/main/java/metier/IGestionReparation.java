package metier;

import java.util.List;

import dao.Reparation;
import exception.ObjectNotFound;

public interface IGestionReparation {
	public void ajouter(Reparation reparation);
	public void supprimer(int id);
	public void modifier(Reparation reparation);
	public Reparation rechercher(int id) throws ObjectNotFound ;
	public List<Reparation> lister();
	public List<Reparation> listerParUser(int userId);
}
