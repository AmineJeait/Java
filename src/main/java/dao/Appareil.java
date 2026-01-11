package dao;

import java.time.Instant;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder


@Entity
public class Appareil {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	
	private String marque;
	private String modele;
	private String description;
	
	@Column(length = 15, unique = true, nullable = false)
	private String iemi;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reparation_id")
    private Reparation reparation;
	
	
	
	
	
	 @CreationTimestamp
	 private Instant createdOn;

	 @UpdateTimestamp
	 private Instant lastUpdatedOn;
	
	 @Override
	 public String toString() {
	     return marque + " " + modele;
	 }

}
