package com.turkcell.rentACarProject.entities.concretes;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="payments")
public class Payment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "payment_date")
	private LocalDate paymentDate;
	
	@Column(name = "payment_total")
	private double paymentTotal;

	@Column(name="invoice_id")
	private int invoiceId;

	@Column(name = "customer_id")
	private int customerId;

	@Column(name = "rental_id")
	private int rentalId;

}
