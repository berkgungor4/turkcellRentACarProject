package com.turkcell.rentACarProject.entities.concretes;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "customers")
@PrimaryKeyJoinColumn(name = "id", referencedColumnName = "id")
public class Customer extends User {

	@Column(name = "id", insertable = false, updatable = false)
	private int id;

	@OneToMany(mappedBy = "customer")
	private List<Rental> rentals;

	@OneToMany(mappedBy = "customer")
	private List<Invoice> invoices;
}
