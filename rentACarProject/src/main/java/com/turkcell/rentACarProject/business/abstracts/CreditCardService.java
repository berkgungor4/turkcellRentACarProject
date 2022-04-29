package com.turkcell.rentACarProject.business.abstracts;

import com.turkcell.rentACarProject.business.requests.creditCard.CreateCreditCardRequest;
import com.turkcell.rentACarProject.business.requests.creditCard.DeleteCreditCardRequest;
import com.turkcell.rentACarProject.business.requests.creditCard.UpdateCreditCardRequest;
import com.turkcell.rentACarProject.core.utilities.results.Result;

public interface CreditCardService {

	Result create(CreateCreditCardRequest createCreditCardRequest);

	Result update(UpdateCreditCardRequest updateCreditCardRequest);

	Result delete(DeleteCreditCardRequest deleteCreditCardRequest);
}
