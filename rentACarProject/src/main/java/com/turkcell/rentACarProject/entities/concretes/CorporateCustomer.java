package com.turkcell.rentACarProject.entities.concretes;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "corporate_customers")
@PrimaryKeyJoinColumn(name = "id")
public class CorporateCustomer extends Customer {
	
	@Column(name= "id", insertable = false, updatable = false)
	private int id;
	
	@Column(name = "name")
	private String name;
	 
	@Column(name = "tax_number", unique = true)
	private String taxNumber;
}
