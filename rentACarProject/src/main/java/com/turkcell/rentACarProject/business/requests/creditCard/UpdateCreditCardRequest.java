package com.turkcell.rentACarProject.business.requests.creditCard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCreditCardRequest {

	private int id;
	private String cardOwnerName;
	private String cardNumber;
	private String cardCvvNumber;
}
