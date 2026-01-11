package metier;

import java.time.Instant;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import dao.Caisse;
import dao.Reparation;
import dao.Transaction;
import dao.User;
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

	public void terminerReparation(int reparationId) {
	    EntityManager em = EntityManagerUtil.getEntityManager();
	    EntityTransaction tx = em.getTransaction();

	    try {
	        tx.begin();

	        Reparation r = em.find(Reparation.class, reparationId);
	        if (r == null) {
	            throw new RuntimeException("Réparation introuvable");
	        }

	        // ⚠️ Éviter double alimentation
	        if ("TERMINEE".equals(r.getState())) {
	            tx.rollback();
	            return;
	        }

	        // 1️⃣ Marquer terminée
	        r.setState("TERMINEE");

	        // 2️⃣ Charger la caisse du user
	        User user = r.getUser();

	        TypedQuery<Caisse> q = em.createQuery(
	            "SELECT c FROM Caisse c WHERE c.user = :u", Caisse.class
	        );
	        q.setParameter("u", user);

	        Caisse caisse = q.getSingleResult();

	        // 3️⃣ Alimenter la caisse réparation
	        caisse.setMontantReparation(
	            caisse.getMontantReparation() + r.getPrix()
	        );

	        // 4️⃣ Créer la transaction
	        Transaction t = new Transaction();
	        t.setCaisse(caisse);
	        t.setMontant(r.getPrix());
	        t.setDescription("Réparation #" + r.getId());
	        t.setDate(Instant.now());

	        em.persist(t);

	        tx.commit();

	    } catch (Exception e) {
	        if (tx.isActive()) tx.rollback();
	        e.printStackTrace();
	    }
	}

}


	

