package com.turkcell.rentACarProject.business.adapters;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.turkcell.rentACarProject.business.abstracts.PosService;
import com.turkcell.rentACarProject.business.outServices.FakeIsBank;

@Service
@Primary
public class FakeIsBankAdapter implements PosService {

	@Override
	public boolean payments(String cardOwnerName, String cardNumber, String cardCvvNumber) {

		FakeIsBank fakeIsBank = new FakeIsBank();

		fakeIsBank.makePayment(cardOwnerName, cardNumber, cardCvvNumber);

		return true;
	}
}
