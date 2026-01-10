package metier;

import java.util.List;

import dao.Magasin;
import exception.ObjectNotFound;

public interface IGestionMagasin {

	public void ajouter(Magasin magasin );
	public void supprimer(int id );
	public Magasin rechercher(int id) throws ObjectNotFound;
	public void modifier(Magasin magasin);
	public List<Magasin> lister();
}
