package dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor


@Entity
public class Reparation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String state;
	private String description;
	private double prix;
	
	private LocalDate dateDepot;
	
	
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
	
	
	
	@OneToMany(
	        mappedBy = "reparation",
	        cascade = CascadeType.ALL,
	        orphanRemoval = true
	    )
	 private List<Appareil> appareils; 
	
	
	
	 @ManyToOne
	 @JoinColumn(name = "client_id", nullable = false)
	 private Client client;
	
	
	
	 @CreationTimestamp
	 private Instant createdOn;

	 @UpdateTimestamp
	 private Instant lastUpdatedOn;
	 
	 @Override
	 public String toString() {
	     return "Réparation #" + id;
	 }

}
