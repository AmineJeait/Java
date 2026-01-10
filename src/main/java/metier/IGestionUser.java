package metier;



import java.util.List;

import dao.User;
import exception.ObjectNotFound;

public interface IGestionUser {
	public void ajouter(User user);
	public void supprimer(int id);
	public void modifier(User user);
	public User rechercher(int id) throws ObjectNotFound ;
	public List<User> lister();
	public User login(String username, String password);
}	
