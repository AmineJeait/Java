package dao;

import java.time.Instant;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

@AllArgsConstructor
@Builder


@Entity
public class Magasin {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String name;
	
	private String adresse;
	
	@OneToMany(
			mappedBy = "magasin",
			cascade = CascadeType.ALL,
            orphanRemoval = true	
	)
	private List <User> reparateurs;
	
	
	
	@OneToOne
    private Admin manager;
	
	@CreationTimestamp
	 private Instant createdOn;

	 @UpdateTimestamp
	 private Instant lastUpdatedOn;
}
