package dao;




import javax.persistence.*;







@Entity
@DiscriminatorValue(value="admin")
public class Admin extends User {
	


}
