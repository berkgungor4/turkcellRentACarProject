package com.turkcell.rentACarProject.business.concretes;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.turkcell.rentACarProject.business.abstracts.InvoiceService;
import com.turkcell.rentACarProject.business.abstracts.OrderedAdditionalServiceService;
import com.turkcell.rentACarProject.business.abstracts.PaymentService;
import com.turkcell.rentACarProject.business.dtos.payment.ListPaymentDto;
import com.turkcell.rentACarProject.business.requests.payment.CreatePaymentRequest;
import com.turkcell.rentACarProject.core.exceptions.BusinessException;
import com.turkcell.rentACarProject.core.posServices.FakeIsBankService;
import com.turkcell.rentACarProject.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACarProject.core.utilities.results.DataResult;
import com.turkcell.rentACarProject.core.utilities.results.Result;
import com.turkcell.rentACarProject.core.utilities.results.SuccessDataResult;
import com.turkcell.rentACarProject.core.utilities.results.SuccessResult;
import com.turkcell.rentACarProject.dataAccess.abstracts.PaymentDao;
import com.turkcell.rentACarProject.entities.concretes.Payment;

@Service
public class PaymentManager implements PaymentService {
	
	private PaymentDao paymentDao;
	private ModelMapperService modelMapperService;
	private FakeIsBankService fakeIsBankService;

	@Autowired
	public PaymentManager(PaymentDao paymentDao, ModelMapperService modelMapperService, FakeIsBankService fakeIsBankService) {
		this.paymentDao = paymentDao;
		this.modelMapperService = modelMapperService;
		this.fakeIsBankService = fakeIsBankService;
	}

	@Override
	public DataResult<List<ListPaymentDto>> getAll() {
		
		var result = this.paymentDao.findAll();

		List<ListPaymentDto> response = result.stream()
				.map(payment -> this.modelMapperService.forDto().map(payment, ListPaymentDto.class))
				.collect(Collectors.toList());

		return new SuccessDataResult<List<ListPaymentDto>>(response);
	}

	@Override
	public Result fakeIsBankAdd(CreatePaymentRequest createPaymentRequest) {
		
		checkPaymentInvoiceExists(createPaymentRequest.getInvoiceId());
		
		checkPaymentOrderedAdditionalServiceExists(createPaymentRequest.getOrderedAdditionalServiceId());
		
		this.fakeIsBankService.payments(createPaymentRequest.getCardOwnerName(), createPaymentRequest.getCardNumber(), createPaymentRequest.getCardCvvNumber());

		Payment payment = this.modelMapperService.forRequest().map(createPaymentRequest, Payment.class);
		
		this.paymentDao.save(payment);

		return new SuccessResult("Payment.Added ");
	}
	
	private void checkPaymentInvoiceExists(int id) {
		
		if(this.paymentDao.getByInvoice_invoiceId(id)!=null) {
			throw new BusinessException("Can find an invoice with this payment id");
		}
	}
	
	private void checkPaymentOrderedAdditionalServiceExists(int id) {
		
		if(this.paymentDao.getByOrderedAdditionalService_orderedAdditionalServiceId(id)!=null) {
			throw new BusinessException("Can find an ordered additional service with this payment id");
		}
	}
	

}
