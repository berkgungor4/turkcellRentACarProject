package com.turkcell.rentACarProject.business.requests.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePaymentRequest {
	
	private String cardOwnerName;
	private String cardNumber;
	private int cardCvvNumber;
	private int invoiceId;
	private int orderedAdditionalServiceId;
}
