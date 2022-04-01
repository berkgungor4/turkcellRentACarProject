package com.turkcell.rentACarProject.business.adapters;

import com.turkcell.rentACarProject.business.abstracts.PosService;
import com.turkcell.rentACarProject.business.outServices.FakeIsBank;

public class FakeIsBankAdapter implements PosService {
	
	@Override
	public boolean payments(String cardOwnerName, String cardNumber, int cardCvvNumber) {
		
		FakeIsBank fakeIsBank = new FakeIsBank();
		
		fakeIsBank.makePayment(cardOwnerName, cardNumber, cardCvvNumber);
		
		return true;
	}
}
