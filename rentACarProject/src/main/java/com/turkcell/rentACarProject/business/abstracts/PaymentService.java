package com.turkcell.rentACarProject.business.abstracts;

import java.util.List;

import com.turkcell.rentACarProject.business.dtos.payment.ListPaymentDto;
import com.turkcell.rentACarProject.business.requests.payment.CreatePaymentRequest;
import com.turkcell.rentACarProject.core.utilities.results.DataResult;
import com.turkcell.rentACarProject.core.utilities.results.Result;

public interface PaymentService {
	
	DataResult<List<ListPaymentDto>> getAll();

	Result fakeIsBankAdd(CreatePaymentRequest createPaymentRequest);

}
