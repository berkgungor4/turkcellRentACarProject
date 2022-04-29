package com.turkcell.rentACarProject.business.abstracts;

public interface PosService {

	public boolean payments(String cardOwnerName, String cardNumber, String cardCvvNumber);
}
