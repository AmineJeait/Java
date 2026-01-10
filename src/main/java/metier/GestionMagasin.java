package metier;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import dao.Magasin;
import exception.ObjectNotFound;

public class GestionMagasin implements IGestionMagasin{

	@Override
	public void ajouter(Magasin magasin) {
		EntityManager em = EntityManagerUtil.getEntityManager();
		EntityTransaction transaction = em.getTransaction();
		
		try {
			transaction.begin();
			em.persist(magasin);
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
			Magasin magasin = rechercher(id);
			em.remove(magasin);
			transaction.commit();
		}catch (Exception E){
			transaction.rollback();
			E.printStackTrace();
		}
		
	}
	
	@Override
	public void modifier(Magasin magasin) {
		EntityManager em = EntityManagerUtil.getEntityManager();
		EntityTransaction transaction = em.getTransaction();
		try {
			transaction.begin();
			em.merge(magasin);
			transaction.commit();
		}catch (Exception E) {
			transaction.rollback();
			E.printStackTrace();
		}
		
	}

	
	
	@Override
	public Magasin rechercher(int id) throws ObjectNotFound {
		EntityManager em = EntityManagerUtil.getEntityManager();
		Magasin magasin = em.find(Magasin.class,id);
		if(magasin == null) {
			throw new ObjectNotFound("Magasin introuvable");
		}
		return magasin;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Magasin> lister() {
		EntityManager em = EntityManagerUtil.getEntityManager();
		Query request = em.createQuery("SELECT M from Magasin M");
		return request.getResultList();
	}

}
