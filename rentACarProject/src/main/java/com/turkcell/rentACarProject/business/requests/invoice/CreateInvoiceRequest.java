package com.turkcell.rentACarProject.business.requests.invoice;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateInvoiceRequest {
	
	private String invoiceNumber;
	private LocalDate creationDate;
	private int numberOfDaysRented;
	private int rentalId;
	private int customerId;
}
