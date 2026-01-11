package metier;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import dao.Caisse;
import dao.Transaction;
import exception.ObjectNotFound;

public class GestionTransaction implements IGestionTransaction {

	@Override
	public void ajouter(Transaction transact) {
		EntityManager em = EntityManagerUtil.getEntityManager();
		EntityTransaction transaction = em.getTransaction();
		
		try {
			transaction.begin();
			em.persist(transact);
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
			Transaction transact = rechercher(id);
			em.remove(transact);
			transaction.commit();
		}catch (Exception E){
			transaction.rollback();
			E.printStackTrace();
		}
		
	}

	@Override
	public void modifier(Transaction transact) {
		EntityManager em = EntityManagerUtil.getEntityManager();
		EntityTransaction transaction = em.getTransaction();
		try {
			transaction.begin();
			em.merge(transact);
			transaction.commit();
		}catch (Exception E) {
			transaction.rollback();
			E.printStackTrace();
		}
		
	}

	@Override
	public Transaction rechercher(int id) throws ObjectNotFound {
		EntityManager em = EntityManagerUtil.getEntityManager();
		Transaction transact = em.find(Transaction.class,id);
		if(transact == null) {
			throw new ObjectNotFound("Utilisateur introuvable");
		}
		return transact;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Transaction> lister() {
		EntityManager em = EntityManagerUtil.getEntityManager();
		Query request = em.createQuery("SELECT T from Transaction T");
		return request.getResultList();
	}
	
	 public List<Transaction> findByCaisse(Caisse caisse) {
		 EntityManager em = EntityManagerUtil.getEntityManager();

	        TypedQuery<Transaction> q = em.createQuery(
	            "SELECT t FROM Transaction t WHERE t.caisse = :caisse ORDER BY t.date DESC",
	            Transaction.class
	        );
	        q.setParameter("caisse", caisse);

	        List<Transaction> list = q.getResultList();
	        em.close();
	        return list;
	    }

}
