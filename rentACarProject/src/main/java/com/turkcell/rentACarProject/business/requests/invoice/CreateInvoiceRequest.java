package com.turkcell.rentACarProject.business.requests.invoice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateInvoiceRequest {

	private String invoiceNumber;
	private int rentalId;
	private int customerId;
}
