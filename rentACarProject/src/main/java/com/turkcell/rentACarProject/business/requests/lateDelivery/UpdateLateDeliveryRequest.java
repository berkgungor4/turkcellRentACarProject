package com.turkcell.rentACarProject.business.requests.lateDelivery;

import com.turkcell.rentACarProject.business.requests.creditCard.CreateCreditCardRequest;
import com.turkcell.rentACarProject.business.requests.rental.UpdateRentalRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateLateDeliveryRequest {

	private UpdateRentalRequest updateRentalRequest;
	
	private CreateCreditCardRequest createCreditCardRequest;
}
