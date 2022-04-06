package com.turkcell.rentACarProject.entities.concretes;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "customers")
@PrimaryKeyJoinColumn(name = "customer_id",referencedColumnName = "id")
public class Customer extends User {

	@Column(name="customer_id", insertable= false, updatable = false)
	private int customerId;
	
	private LocalDate registeredAt;
	
	@OneToMany(mappedBy = "customer")
    @JsonIgnore
    private List<Rental> rentals;
	
	@OneToMany(mappedBy = "customer")
	private List<Invoice> invoices;
	
	@OneToOne(mappedBy = "customer")
	private Payment payment;
}
