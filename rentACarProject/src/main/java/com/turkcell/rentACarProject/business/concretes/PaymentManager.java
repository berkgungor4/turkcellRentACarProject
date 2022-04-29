package com.turkcell.rentACarProject.business.concretes;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.turkcell.rentACarProject.business.abstracts.CreditCardService;
import com.turkcell.rentACarProject.business.abstracts.CustomerService;
import com.turkcell.rentACarProject.business.abstracts.InvoiceService;
import com.turkcell.rentACarProject.business.abstracts.OrderedAdditionalServiceService;
import com.turkcell.rentACarProject.business.abstracts.PaymentService;
import com.turkcell.rentACarProject.business.abstracts.PosService;
import com.turkcell.rentACarProject.business.abstracts.RentalService;
import com.turkcell.rentACarProject.business.constants.Messages;
import com.turkcell.rentACarProject.business.dtos.payment.ListPaymentDto;
import com.turkcell.rentACarProject.business.dtos.rental.ListRentalDto;
import com.turkcell.rentACarProject.business.requests.creditCard.CreateCreditCardRequest;
import com.turkcell.rentACarProject.business.requests.invoice.CreateInvoiceRequest;
import com.turkcell.rentACarProject.business.requests.lateDelivery.CreateLateDeliveryRequest;
import com.turkcell.rentACarProject.business.requests.payment.CreatePaymentRequest;
import com.turkcell.rentACarProject.business.requests.payment.DeletePaymentRequest;
import com.turkcell.rentACarProject.core.exceptions.BusinessException;
import com.turkcell.rentACarProject.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACarProject.core.utilities.results.DataResult;
import com.turkcell.rentACarProject.core.utilities.results.Result;
import com.turkcell.rentACarProject.core.utilities.results.SuccessDataResult;
import com.turkcell.rentACarProject.core.utilities.results.SuccessResult;
import com.turkcell.rentACarProject.dataAccess.abstracts.PaymentDao;
import com.turkcell.rentACarProject.entities.concretes.Invoice;
import com.turkcell.rentACarProject.entities.concretes.Payment;
import com.turkcell.rentACarProject.entities.concretes.Rental;

import net.bytebuddy.utility.RandomString;

@Service
public class PaymentManager implements PaymentService {

	private PaymentDao paymentDao;
	private ModelMapperService modelMapperService;
	private InvoiceService invoiceService;
	private PosService posService;
	private CustomerService customerService;
	private RentalService rentalService;
	private CreditCardService creditCardService;
	private OrderedAdditionalServiceService orderedAdditionalServiceService;

	@Autowired
	public PaymentManager(PaymentDao paymentDao, ModelMapperService modelMapperService, InvoiceService invoiceService,
			PosService posService, CustomerService customerService, RentalService rentalService,
			CreditCardService creditCardService, OrderedAdditionalServiceService orderedAdditionalServiceService) {
		this.paymentDao = paymentDao;
		this.modelMapperService = modelMapperService;
		this.invoiceService = invoiceService;
		this.posService = posService;
		this.customerService = customerService;
		this.rentalService = rentalService;
		this.creditCardService = creditCardService;
		this.orderedAdditionalServiceService = orderedAdditionalServiceService;
	}

	@Override
	public DataResult<List<ListPaymentDto>> getAll() {

		var result = this.paymentDao.findAll();

		List<ListPaymentDto> response = result.stream()
				.map(payment -> this.modelMapperService.forDto().map(payment, ListPaymentDto.class))
				.collect(Collectors.toList());

		return new SuccessDataResult<List<ListPaymentDto>>(response, Messages.SUCCESS);
	}

	@Transactional
	@Override
	public Result create(boolean registered, CreatePaymentRequest createPaymentRequest) {

		this.posService.payments(createPaymentRequest.getCreateCreditCardRequest().getCardOwnerName(),
				createPaymentRequest.getCreateCreditCardRequest().getCardNumber(),
				createPaymentRequest.getCreateCreditCardRequest().getCardCvvNumber());

		runPaymentProcess(registered, createPaymentRequest);

		return new SuccessResult(Messages.PAYMENT_ADD);
	}

	@Transactional
	@Override
	public void createForLateDelivery(CreateLateDeliveryRequest createLateDeliveryRequest) {

		this.posService.payments(createLateDeliveryRequest.getCreateCreditCardRequest().getCardOwnerName(),
				createLateDeliveryRequest.getCreateCreditCardRequest().getCardNumber(),
				createLateDeliveryRequest.getCreateCreditCardRequest().getCardCvvNumber());
		runLateDeliveriesPaymentProcess(createLateDeliveryRequest);
	}

	@Override
	public Result delete(DeletePaymentRequest deletePaymentRequest) {

		checkIfPaymentExists(deletePaymentRequest.getId());

		this.paymentDao.deleteById(deletePaymentRequest.getId());

		return new SuccessResult(Messages.PAYMENT_DELETE);
	}

	@Override
	public DataResult<ListPaymentDto> getById(int paymentId) {

		Payment result = checkIfPaymentExists(paymentId);

		ListPaymentDto response = this.modelMapperService.forDto().map(result, ListPaymentDto.class);

		return new SuccessDataResult<ListPaymentDto>(response, Messages.SUCCESS);
	}

	@Transactional
	private void runPaymentProcess(boolean registered, CreatePaymentRequest createPaymentRequest) {

		Rental rental = saveRental(createPaymentRequest);

		saveOrderedAdditionalService(createPaymentRequest.getAdditionalServiceId(), rental.getId());

		saveCreditCard(registered, createPaymentRequest.getCreateCreditCardRequest());

		Invoice invoice = saveInvoiceByCustomerType(rental);

		Payment payment = this.modelMapperService.forRequest().map(createPaymentRequest, Payment.class);
		savePayment(payment, rental, invoice);
	}

	@Transactional
	private void runLateDeliveriesPaymentProcess(CreateLateDeliveryRequest createLateDeliveryRequest) {

		Rental rental = getByRental(createLateDeliveryRequest);

		Invoice invoice = saveInvoiceByCustomerType(rental);

		Payment payment = this.modelMapperService.forRequest().map(createLateDeliveryRequest, Payment.class);
		savePayment(payment, rental, invoice);
	}

	private Rental getByRental(CreateLateDeliveryRequest createLateDeliveryRequest) {

		ListRentalDto listRentalDto = this.rentalService.getById(createLateDeliveryRequest.getRentalId()).getData();

		return this.modelMapperService.forRequest().map(listRentalDto, Rental.class);
	}

	private Rental saveRental(CreatePaymentRequest createPaymentRequest) {

		if (this.customerService
				.checkCustomerIfForRental(createPaymentRequest.getCreateRentalRequest().getCustomerId())) {
			return this.rentalService.createForCustomer(createPaymentRequest.getCreateRentalRequest());
		}
		
		throw new BusinessException(Messages.CUSTOMER_NOT_FOUND);
	}

	private void saveOrderedAdditionalService(List<Integer> additionalServiceId, int rentalId) {

		this.orderedAdditionalServiceService.create(additionalServiceId, rentalId);
	}

	private void saveCreditCard(boolean registered, CreateCreditCardRequest createCreditCardRequest) {

		if (registered) {
			creditCardService.create(createCreditCardRequest);
		}
	}

	private Invoice saveInvoiceByCustomerType(Rental rental) {

		CreateInvoiceRequest createInvoiceRequest = new CreateInvoiceRequest();
		createInvoiceRequest.setInvoiceNumber(RandomString.make(10));
		createInvoiceRequest.setCustomerId(rental.getCustomer().getId());
		createInvoiceRequest.setRentalId(rental.getId());

		if (this.customerService.checkCustomerIfForRental(rental.getCustomer().getId())) {
			return this.invoiceService.createForCustomer(createInvoiceRequest);
		}
		throw new BusinessException(Messages.CUSTOMER_NOT_FOUND);
	}

	private void savePayment(Payment payment, Rental rental, Invoice invoice) {

		payment.setCustomerId(rental.getCustomer().getId());
		payment.setInvoiceId(invoice.getId());
		payment.setPaymentTotal(invoice.getRentTotalPrice());
		payment.setPaymentDate(LocalDate.now());
		payment.setRentalId(rental.getId());
		payment.setId(0);

		this.paymentDao.save(payment);
	}

	private Payment checkIfPaymentExists(int paymentId) {

		Payment payment = this.paymentDao.getById(paymentId);

		if (payment == null) {
			throw new BusinessException(Messages.PAYMENT_NOT_FOUND);
		}
		
		return payment;
	}

}
