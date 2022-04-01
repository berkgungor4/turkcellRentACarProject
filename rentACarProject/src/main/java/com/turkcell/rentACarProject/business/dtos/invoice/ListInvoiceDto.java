package com.turkcell.rentACarProject.business.dtos.invoice;

import java.time.LocalDate;

import com.turkcell.rentACarProject.entities.concretes.Customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListInvoiceDto {

	private int id;
	private String invoiceNumber;
	private LocalDate creationDate;
	private LocalDate rentDate;
	private LocalDate returnDate;
	private int numberOfDaysRented;
	private double rentTotalPrice;
	private int rental;
	private Customer customer;
}
