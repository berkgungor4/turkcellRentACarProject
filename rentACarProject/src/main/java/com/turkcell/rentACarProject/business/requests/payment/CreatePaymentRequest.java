package com.turkcell.rentACarProject.business.requests.payment;

import java.util.List;

import com.turkcell.rentACarProject.business.requests.creditCard.CreateCreditCardRequest;
import com.turkcell.rentACarProject.business.requests.rental.CreateRentalRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePaymentRequest {
	
	private List<Integer> additionalServiceId;
	private CreateRentalRequest rental;
	private CreateCreditCardRequest createCreditCard;
}
