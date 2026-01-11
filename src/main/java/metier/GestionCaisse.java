package metier;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import dao.Caisse;
import exception.ObjectNotFound;

public class GestionCaisse implements IGestionCaisse{

	@Override
	public void ajouter(Caisse caisse) {
		EntityManager em = EntityManagerUtil.getEntityManager();
		EntityTransaction transaction = em.getTransaction();
		
		try {
			transaction.begin();
			em.persist(caisse);
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
			Caisse caisse = rechercher(id);
			em.remove(caisse);
			transaction.commit();
		}catch (Exception E){
			transaction.rollback();
			E.printStackTrace();
		}
		
	}
	@Override
	public void modifier(Caisse caisse) {
		EntityManager em = EntityManagerUtil.getEntityManager();
		EntityTransaction transaction = em.getTransaction();
		try {
			transaction.begin();
			em.merge(caisse);
			transaction.commit();
		}catch (Exception E) {
			transaction.rollback();
			E.printStackTrace();
		}
		
	}
	@Override
	public Caisse rechercher(int id) throws ObjectNotFound {
		EntityManager em = EntityManagerUtil.getEntityManager();
		Caisse caisse = em.find(Caisse.class,id);
		if(caisse == null) {
			throw new ObjectNotFound("Caisse introuvable");
		}
		return caisse;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Caisse> lister() {
		EntityManager em = EntityManagerUtil.getEntityManager();
		Query request = em.createQuery("SELECT C from Caisse C");
		return request.getResultList();
	}

	public Caisse rechercherParUser(int userId) throws ObjectNotFound {
	    EntityManager em = EntityManagerUtil.getEntityManager();
	    try {
	        return em.createQuery(
	            "SELECT c FROM Caisse c WHERE c.user.id = :uid",
	            Caisse.class
	        ).setParameter("uid", userId)
	         .getSingleResult();
	    } catch (Exception e) {
	        throw new ObjectNotFound("Caisse introuvable");
	    }
	}

}
