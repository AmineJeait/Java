package metier;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import dao.Appareil;
import exception.ObjectNotFound;

public class GestionAppareil implements IGestionAppareil{

	@Override
	public void ajouter(Appareil appareil) {
		EntityManager em = EntityManagerUtil.getEntityManager();
		EntityTransaction transaction = em.getTransaction();
		
		try {
			transaction.begin();
			em.persist(appareil);
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
			Appareil appareil = rechercher(id);
			em.remove(appareil);
			transaction.commit();
		}catch (Exception E){
			transaction.rollback();
			E.printStackTrace();
		}
		
	}
	@Override
	public void modifier(Appareil appareil) {
		EntityManager em = EntityManagerUtil.getEntityManager();
		EntityTransaction transaction = em.getTransaction();
		try {
			transaction.begin();
			em.merge(appareil);
			transaction.commit();
		}catch (Exception E) {
			transaction.rollback();
			E.printStackTrace();
		}
		
	}
	@Override
	public Appareil rechercher(int id) throws ObjectNotFound {
		EntityManager em = EntityManagerUtil.getEntityManager();
		Appareil appareil = em.find(Appareil.class,id);
		if(appareil == null) {
			throw new ObjectNotFound("Appareil introuvable");
		}
		return appareil;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Appareil> lister() {
		EntityManager em = EntityManagerUtil.getEntityManager();
		Query request = em.createQuery("SELECT A from Appareil A");
		return request.getResultList();
	}
	
	public boolean existsByImei(String imei) {
		EntityManager em = EntityManagerUtil.getEntityManager();
	    return em.createQuery(
	        "SELECT COUNT(a) FROM Appareil a WHERE a.iemi = :imei",
	        Long.class
	    )
	    .setParameter("imei", imei)
	    .getSingleResult() > 0;
	}

}
