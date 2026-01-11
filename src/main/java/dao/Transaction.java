package dao;

import java.time.Instant;


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
public class Transaction {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	
	@ManyToOne
    @JoinColumn(name = "caisse_id", nullable = false)
    private Caisse caisse;
	
	
	private double montant;  // positive = deposit, negative = withdrawal
    private String description;
    private Instant date;


	 @CreationTimestamp
	 private Instant createdOn;

	 @UpdateTimestamp
	 private Instant lastUpdatedOn;

}
