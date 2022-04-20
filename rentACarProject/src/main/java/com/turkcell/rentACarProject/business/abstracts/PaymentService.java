package com.turkcell.rentACarProject.business.abstracts;

import java.util.List;

import com.turkcell.rentACarProject.business.dtos.payment.ListPaymentDto;
import com.turkcell.rentACarProject.business.requests.lateDelivery.CreateLateDeliveryRequest;
import com.turkcell.rentACarProject.business.requests.payment.CreatePaymentRequest;
import com.turkcell.rentACarProject.business.requests.payment.DeletePaymentRequest;
import com.turkcell.rentACarProject.core.utilities.results.DataResult;
import com.turkcell.rentACarProject.core.utilities.results.Result;

public interface PaymentService {
	
	DataResult<List<ListPaymentDto>> getAll();
	DataResult<ListPaymentDto> getById(int id);
	void createForLateDelivery(CreateLateDeliveryRequest createLateDeliveryRequest);

	Result create(boolean registered, CreatePaymentRequest createPaymentRequest);
	Result delete(DeletePaymentRequest deletePaymentRequest);
}
