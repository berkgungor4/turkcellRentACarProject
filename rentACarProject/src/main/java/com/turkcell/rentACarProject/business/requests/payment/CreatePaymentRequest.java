package com.turkcell.rentACarProject.business.requests.payment;

import com.turkcell.rentACarProject.business.requests.creditCard.CreateCreditCardRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePaymentRequest {
	
	private int invoiceId;
	private int orderedAdditionalServiceId;
	private CreateCreditCardRequest createCreditCard;
}
