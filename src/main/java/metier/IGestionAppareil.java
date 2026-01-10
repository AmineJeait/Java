package metier;

import java.util.List;

import dao.Appareil;

import exception.ObjectNotFound;

public interface IGestionAppareil {
	public void ajouter(Appareil appareil);
	public void supprimer(int id);
	public void modifier(Appareil appareil);
	public Appareil rechercher(int id) throws ObjectNotFound ;
	public List<Appareil> lister();
	public boolean existsByImei(String imei);
}	
