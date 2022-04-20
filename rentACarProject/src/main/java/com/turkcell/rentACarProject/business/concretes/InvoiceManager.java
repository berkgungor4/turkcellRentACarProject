package com.turkcell.rentACarProject.business.concretes;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.turkcell.rentACarProject.business.abstracts.AdditionalServiceService;
import com.turkcell.rentACarProject.business.abstracts.CustomerService;
import com.turkcell.rentACarProject.business.abstracts.InvoiceService;
import com.turkcell.rentACarProject.business.abstracts.OrderedAdditionalServiceService;
import com.turkcell.rentACarProject.business.abstracts.RentalService;
import com.turkcell.rentACarProject.business.constants.Messages;
import com.turkcell.rentACarProject.business.dtos.invoice.ListInvoiceDto;
import com.turkcell.rentACarProject.business.dtos.rental.ListRentalDto;
import com.turkcell.rentACarProject.business.requests.invoice.CreateInvoiceRequest;
import com.turkcell.rentACarProject.business.requests.invoice.DeleteInvoiceRequest;
import com.turkcell.rentACarProject.business.requests.invoice.UpdateInvoiceRequest;
import com.turkcell.rentACarProject.core.exceptions.BusinessException;
import com.turkcell.rentACarProject.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACarProject.core.utilities.results.DataResult;
import com.turkcell.rentACarProject.core.utilities.results.Result;
import com.turkcell.rentACarProject.core.utilities.results.SuccessDataResult;
import com.turkcell.rentACarProject.core.utilities.results.SuccessResult;
import com.turkcell.rentACarProject.dataAccess.abstracts.InvoiceDao;
import com.turkcell.rentACarProject.entities.concretes.Invoice;
import com.turkcell.rentACarProject.entities.concretes.Rental;

@Service
public class InvoiceManager implements InvoiceService{
	
	private InvoiceDao invoiceDao;
	private ModelMapperService modelMapperService;
	private OrderedAdditionalServiceService orderedAdditionalServiceService;
	private AdditionalServiceService additionalServiceService;
	private RentalService rentalService;

	@Autowired
	public InvoiceManager(InvoiceDao invoiceDao, ModelMapperService modelMapperService, CustomerService customerService, OrderedAdditionalServiceService orderedAdditionalServiceService, AdditionalServiceService additionalServiceService, RentalService rentalService) {
		this.invoiceDao = invoiceDao;
		this.modelMapperService = modelMapperService;
		this.orderedAdditionalServiceService = orderedAdditionalServiceService;
		this.additionalServiceService = additionalServiceService;
		this.rentalService = rentalService;
	}

	@Override
	public DataResult<List<ListInvoiceDto>> getAll() {
		
		var result = this.invoiceDao.findAll();
		List<ListInvoiceDto> response = result.stream()
				.map(invoice -> this.modelMapperService.forDto().map(invoice, ListInvoiceDto.class))
				.collect(Collectors.toList());
		return new SuccessDataResult<List<ListInvoiceDto>>(response, Messages.SUCCESS);
	}
	
	@Override
	public DataResult<ListInvoiceDto> getById(int id) {
		
		Invoice result = checkIfInvoiceExists(id);
		
		ListInvoiceDto response = this.modelMapperService.forDto().map(result, ListInvoiceDto.class);
		
		return new SuccessDataResult<ListInvoiceDto>(response, Messages.SUCCESS);
	}
	
	@Override
	public DataResult<List<ListInvoiceDto>> getByDateOfBetween(LocalDate creationDate, LocalDate returnDate) {
		
		List<Invoice> result = this.invoiceDao.findByCreationDateBetween(creationDate, returnDate);
		List<ListInvoiceDto> response = result.stream()
				.map(invoice -> this.modelMapperService.forDto().map(invoice, ListInvoiceDto.class))
				.collect(Collectors.toList());
		
		return new SuccessDataResult<List<ListInvoiceDto>>(response, Messages.SUCCESS);
	}

	@Override
	public DataResult<List<ListInvoiceDto>> getInvoiceByCustomer(int customerId) {
		
		List<Invoice> result = this.invoiceDao.getByCustomer_id(customerId);
		List<ListInvoiceDto> response = result.stream()
				.map(invoice -> this.modelMapperService.forDto().map(invoice, ListInvoiceDto.class))
				.collect(Collectors.toList());
		
		return new SuccessDataResult<List<ListInvoiceDto>>(response, Messages.SUCCESS);
	}
	
	@Override
	public DataResult<List<ListInvoiceDto>> getInvoiceByRental(int rentalId) {
		
		List<Invoice> result = this.invoiceDao.getByRental_id(rentalId);
		List<ListInvoiceDto> response = result.stream()
				.map(invoice -> this.modelMapperService.forDto().map(invoice, ListInvoiceDto.class))
				.collect(Collectors.toList());
		
		return new SuccessDataResult<List<ListInvoiceDto>>(response, Messages.SUCCESS);
	}

	@Override
	public Result createForCustomer(CreateInvoiceRequest createInvoiceRequest) {
		
		ListRentalDto listRentalDto = this.rentalService.getById(createInvoiceRequest.getRentalId()).getData();
		
		Invoice invoice = this.modelMapperService.forRequest().map(createInvoiceRequest, Invoice.class);
		
		invoice.setId(0);

		invoice = invoiceTableSetColumns(invoice, createInvoiceRequest, listRentalDto);
		
		this.invoiceDao.save(invoice);
		
		return new SuccessDataResult<CreateInvoiceRequest>(createInvoiceRequest, Messages.INVOICE_ADD);
	}

	@Override
	public Result delete(DeleteInvoiceRequest deleteInvoiceRequest) {
		
		checkIfInvoiceExists(deleteInvoiceRequest.getId());
		Invoice invoice = this.modelMapperService.forRequest().map(deleteInvoiceRequest, Invoice.class);
		this.invoiceDao.delete(invoice);
		return new SuccessResult(Messages.INVOICE_DELETE);
	}

	@Override
	public Result update(UpdateInvoiceRequest updateInvoiceRequest) {
		
		checkIfInvoiceExists(updateInvoiceRequest.getId());

		Invoice invoice = this.invoiceDao.getInvoiceById(updateInvoiceRequest.getId());
		invoice.setCreationDate(updateInvoiceRequest.getCreationDate());
		this.invoiceDao.save(invoice);

		return new SuccessResult(Messages.INVOICE_UPDATE);
	}
	
	private Invoice checkIfInvoiceExists(int id) {
		
		Invoice invoice =this.invoiceDao.getInvoiceById(id);
		
		if(invoice==null) {
			throw new BusinessException(Messages.INVOICE_NOT_FOUND);
		}
		
		return invoice;
	}
	
	private Invoice invoiceTableSetColumns(Invoice invoice, CreateInvoiceRequest createInvoiceRequest, ListRentalDto listRentalDto) {
		
		Rental rental = this.modelMapperService.forDto().map(listRentalDto, Rental.class);
		
		invoice.setCreationDate(LocalDate.now());
		invoice.setRentDate(invoice.getRental().getRentDate());
		invoice.setReturnDate(invoice.getRental().getReturnDate());
		invoice.setNumberOfDaysRented(dateBetweenCalculator(rental));
		invoice.setRentTotalPrice(totalPriceCalculator(rental, invoice.getNumberOfDaysRented()));
		
		return invoice;
	}

	private int dateBetweenCalculator(Rental rental) {
		
		int passedDays = 1;
		
		if(ChronoUnit.DAYS.between(rental.getRentDate(), rental.getReturnDate()) == 0) {
			
			return passedDays;
		}
		
		passedDays = Integer.valueOf((int) ChronoUnit.DAYS.between(rental.getRentDate(), rental.getReturnDate()));
		
		return passedDays;
	}

	private double totalPriceCalculator(Rental rental, int passedDays) {
		
		double totalPrice = rental.getTotalPrice() * passedDays;
		
		if(rental.getInitialCity() != rental.getReturnCity()) {
			
			totalPrice += 750;
		}
		
		return totalPrice;
	}

}
