package com.turkcell.rentACarProject.core.posServices;

public interface PosService {

	public boolean payments(String cardOwnerName, String cardNumber, int cardCvvNumber);
}
