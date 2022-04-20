package com.turkcell.rentACarProject.business.requests.lateDelivery;

import com.turkcell.rentACarProject.business.requests.creditCard.CreateCreditCardRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateLateDeliveryRequest {
	
	private int rentalId;
	private CreateCreditCardRequest createCreditCardRequest;
}
