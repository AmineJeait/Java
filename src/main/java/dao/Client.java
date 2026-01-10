package dao;


import java.time.Instant;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor


@Entity
public class Client {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(length = 10, unique = true, nullable = false)
	private String telephone;
	private String adresse;
	private String nomComplet;
	
	
	
	 @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
	 private List<Reparation> reparations;
	
	 
	 
	 @CreationTimestamp
	 private Instant createdOn;

	 @UpdateTimestamp
	 private Instant lastUpdatedOn;
	 
	 @Override
	 public String toString() {
	     return nomComplet;
	 }

}
