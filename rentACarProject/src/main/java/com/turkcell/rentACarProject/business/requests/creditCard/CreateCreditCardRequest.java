package com.turkcell.rentACarProject.business.requests.creditCard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCreditCardRequest {

	private String cardOwnerName;
	private String cardNumber;
	private String cardCvvNumber;
}
