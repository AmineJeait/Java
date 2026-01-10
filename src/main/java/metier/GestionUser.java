package metier;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import dao.User;
import exception.ObjectNotFound;

public class GestionUser implements IGestionUser {
	
	public User login(String username, String password) {

        EntityManager em = EntityManagerUtil.getEntityManager();

        TypedQuery<User> q = em.createQuery(
            "SELECT u FROM User u WHERE u.username = :username AND u.password = :password",
            User.class
        );

        q.setParameter("username", username);
        q.setParameter("password", password);

        User user = null;

        try {
            user = q.getSingleResult();
        } catch (Exception e) {
            // no user found
        } finally {
            em.close();
        }

        return user; // null if login failed
    }

	

	@Override
	public void ajouter(User user) {
		EntityManager em = EntityManagerUtil.getEntityManager();
		EntityTransaction transaction = em.getTransaction();
		
		try {
			transaction.begin();
			em.persist(user);
			transaction.commit();
			
		}catch(Exception E){
			transaction.rollback();
			E.printStackTrace();
		}
		
	}

	@Override
	public void supprimer(int id) {
		EntityManager em = EntityManagerUtil.getEntityManager();
		EntityTransaction transaction = em.getTransaction();
		try {
			transaction.begin();
			User user = rechercher(id);
			em.remove(user);
			transaction.commit();
		}catch (Exception E){
			transaction.rollback();
			E.printStackTrace();
		}
		
	}

	@Override
	public void modifier(User user) {
		EntityManager em = EntityManagerUtil.getEntityManager();
		EntityTransaction transaction = em.getTransaction();
		try {
			transaction.begin();
			em.merge(user);
			transaction.commit();
		}catch (Exception E) {
			transaction.rollback();
			E.printStackTrace();
		}
		
	}

	@Override
	public User rechercher(int id) throws ObjectNotFound {
		EntityManager em = EntityManagerUtil.getEntityManager();
		User user = em.find(User.class,id);
		if(user == null) {
			throw new ObjectNotFound("Utilisateur introuvable");
		}
		return user;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<User> lister() {
		EntityManager em = EntityManagerUtil.getEntityManager();
		Query request = em.createQuery("SELECT U from User U");
		return request.getResultList();
	}

}
