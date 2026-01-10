package metier;
import dao.User;


public class AdminUtil {

	public static void verifyAdmin(User user) {
		
	if (user == null || !user.isAdmin()) {
		throw new SecurityException("Admin rights required");
	        
	}
	    
	}
}


