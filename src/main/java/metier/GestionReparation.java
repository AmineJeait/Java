package metier;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;


import dao.Reparation;
import exception.ObjectNotFound;

public class GestionReparation implements IGestionReparation{

	@Override
	public void ajouter(Reparation reparation) {
		EntityManager em = EntityManagerUtil.getEntityManager();
		EntityTransaction transaction = em.getTransaction();
		
		try {
			transaction.begin();
			em.persist(reparation);
			em.flush();
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
			Reparation reparation = rechercher(id);
			em.remove(reparation);
			transaction.commit();
		}catch (Exception E){
			transaction.rollback();
			E.printStackTrace();
		}
		
	}
	@Override
	public void modifier(Reparation reparation) {
		EntityManager em = EntityManagerUtil.getEntityManager();
		EntityTransaction transaction = em.getTransaction();
		try {
			transaction.begin();
			em.merge(reparation);
			transaction.commit();
		}catch (Exception E) {
			transaction.rollback();
			E.printStackTrace();
		}
		
	}

	@Override
	public Reparation rechercher(int id) throws ObjectNotFound {
		EntityManager em = EntityManagerUtil.getEntityManager();
		Reparation reparation = em.find(Reparation.class,id);
		if(reparation == null) {
			throw new ObjectNotFound("Reparation introuvable");
		}
		return reparation;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Reparation> lister() {
		EntityManager em = EntityManagerUtil.getEntityManager();
		Query request = em.createQuery("SELECT R from Reparation R");
		return request.getResultList();
	}
	
	@Override
	public List<Reparation> listerParUser(int userId) {
	    EntityManager em = EntityManagerUtil.getEntityManager();
	    try {
	        return em.createQuery(
	            "SELECT DISTINCT r FROM Reparation r " +
	            "LEFT JOIN FETCH r.appareils " +
	            "LEFT JOIN FETCH r.client " +
	            "WHERE r.user.id = :uid",
	            Reparation.class
	        )
	        .setParameter("uid", userId)
	        .getResultList();
	    } finally {
	        em.close();
	    }
	}

}


	

