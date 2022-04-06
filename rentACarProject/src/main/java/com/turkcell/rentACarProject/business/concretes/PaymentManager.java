package com.turkcell.rentACarProject.business.concretes;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.turkcell.rentACarProject.business.abstracts.InvoiceService;
import com.turkcell.rentACarProject.business.abstracts.OrderedAdditionalServiceService;
import com.turkcell.rentACarProject.business.abstracts.PaymentService;
import com.turkcell.rentACarProject.business.abstracts.PosService;
import com.turkcell.rentACarProject.business.constants.Messages;
import com.turkcell.rentACarProject.business.dtos.payment.ListPaymentDto;
import com.turkcell.rentACarProject.business.requests.payment.CreatePaymentRequest;
import com.turkcell.rentACarProject.business.requests.payment.DeletePaymentRequest;
import com.turkcell.rentACarProject.core.exceptions.BusinessException;
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
	private InvoiceService invoiceService;
	private OrderedAdditionalServiceService orderedAdditionalServiceService;
	private PosService posService;

	@Autowired
	public PaymentManager(PaymentDao paymentDao, ModelMapperService modelMapperService,InvoiceService invoiceService, OrderedAdditionalServiceService orderedAdditionalServiceService, PosService posService) {
		this.paymentDao = paymentDao;
		this.modelMapperService = modelMapperService;
		this.invoiceService = invoiceService;
		this.orderedAdditionalServiceService = orderedAdditionalServiceService;
		this.posService = posService;
	}

	@Override
	public DataResult<List<ListPaymentDto>> getAll() {
		
		var result = this.paymentDao.findAll();

		List<ListPaymentDto> response = result.stream()
				.map(payment -> this.modelMapperService.forDto().map(payment, ListPaymentDto.class))
				.collect(Collectors.toList());

		return new SuccessDataResult<List<ListPaymentDto>>(response, Messages.SUCCESS);
	}
	
	@Override
	public DataResult<ListPaymentDto> getById(int id) {
		
		Payment result = checkIfPaymentExists(id);

		ListPaymentDto response = this.modelMapperService.forDto().map(result, ListPaymentDto.class);

		return new SuccessDataResult<ListPaymentDto>(response, Messages.SUCCESS);
		
	}
	
	@Override
	public Result create(CreatePaymentRequest createPaymentRequest) {
		
		checkIfInvoiceExists(createPaymentRequest.getInvoiceId());
		
		checkIfOrderedAdditionalServiceExists(createPaymentRequest.getOrderedAdditionalServiceId());
		
		this.posService.payments(createPaymentRequest.getCreateCreditCard().getCardOwnerName(), createPaymentRequest.getCreateCreditCard().getCardNumber(), createPaymentRequest.getCreateCreditCard().getCardCvvNumber());
		
		Payment payment = this.modelMapperService.forRequest().map(createPaymentRequest, Payment.class);
		
		payment.setId(0);

		this.paymentDao.save(payment);

		return new SuccessResult(Messages.PAYMENT_ADD);
	}

	@Override
	public Result delete(DeletePaymentRequest deletePaymentRequest) {
		
		checkIfPaymentExists(deletePaymentRequest.getId());

		this.paymentDao.deleteById(deletePaymentRequest.getId());

		return new SuccessResult(Messages.PAYMENT_DELETE);
	}
	
	private Payment checkIfPaymentExists(int id) {

		Payment payment = this.paymentDao.getPaymentById(id);

		if (payment == null) {
			throw new BusinessException(Messages.PAYMENT_ADD);
		}
		return payment;
	}
	
	private void checkIfInvoiceExists(int invoiceId) {
		
	    if(this.invoiceService.getById(invoiceId)==null) {
	    	throw new BusinessException(Messages.INVOICE_NOT_FOUND);
	    }
	}
	
	private void checkIfOrderedAdditionalServiceExists(int orderedAdditionalServiceId) {
		
	    if(this.orderedAdditionalServiceService.getById(orderedAdditionalServiceId)==null) {
	    	throw new BusinessException(Messages.ORDERED_ADDITIONAL_SERVICE_NOT_FOUND);
	    }
	}

	@Override
	public void addForLateDelivery(CreatePaymentRequest createPaymentRequest) {
		
		this.posService.payments(createPaymentRequest.getCreateCreditCard().getCardOwnerName(), createPaymentRequest.getCreateCreditCard().getCardNumber(), createPaymentRequest.getCreateCreditCard().getCardCvvNumber());
		runLateDeliveriesPaymentProcess(createPaymentRequest);
		
	}
	
}
