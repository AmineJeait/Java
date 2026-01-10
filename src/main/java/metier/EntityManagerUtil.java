package metier;

import javax.persistence.EntityManager;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;



public class EntityManagerUtil {
	
	private static EntityManagerFactory emf;
    private static EntityManager em;


    private EntityManagerUtil() { }


    public static EntityManager getEntityManager() {
        if (emf == null) {
            emf = Persistence.createEntityManagerFactory("UP_Prod"); 
        }
        if (em == null || !em.isOpen() ){
            em = emf.createEntityManager();
        }
        return em;
    }

    
    public static void close() {
        if (em != null && em.isOpen()) {
            em.close();
        }
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }

}
