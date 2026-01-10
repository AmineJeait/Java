package dao;


import javax.persistence.Entity;

import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.GenerationType;

import java.time.Instant;

import java.util.List;

import javax.persistence.*;

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
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "role", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue(value="reparateur")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String nom;
	private String prenom;
	private String username;
	private String password;
	

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
        )
     private List<Reparation> reparations;
	
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
    		name = "magasin_id" , nullable = true
    		)
    private Magasin magasin;
    
    
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Caisse caisse;
    
    
    
    public boolean isAdmin() {
        return this instanceof Admin;
    }
    
    
	 @CreationTimestamp
	 private Instant createdOn;

	 @UpdateTimestamp
	 private Instant lastUpdatedOn;
}
