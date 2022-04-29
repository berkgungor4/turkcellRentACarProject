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
import com.turkcell.rentACarProject.business.constants.Messages;
import com.turkcell.rentACarProject.business.dtos.additionalService.ListAdditionalServiceDto;
import com.turkcell.rentACarProject.business.dtos.invoice.ListInvoiceDto;
import com.turkcell.rentACarProject.business.dtos.orderedAdditionalService.ListOrderedAdditionalServiceDto;
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

@Service
public class InvoiceManager implements InvoiceService{
	
	private InvoiceDao invoiceDao;
	private ModelMapperService modelMapperService;
	private AdditionalServiceService additionalServiceService;
	private OrderedAdditionalServiceService orderedAdditionalServiceService;

	@Autowired
	public InvoiceManager(InvoiceDao invoiceDao, ModelMapperService modelMapperService, CustomerService customerService, AdditionalServiceService additionalServiceService, OrderedAdditionalServiceService orderedAdditionalServiceService) {
		this.invoiceDao = invoiceDao;
		this.modelMapperService = modelMapperService;
		this.additionalServiceService = additionalServiceService;
		this.orderedAdditionalServiceService = orderedAdditionalServiceService;
	}

	@Override
	public DataResult<List<ListInvoiceDto>> getAll() {
		
		List<Invoice> result = this.invoiceDao.findAll();
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
	public Invoice createForCustomer(CreateInvoiceRequest createInvoiceRequest) {
		
		Invoice invoice = this.modelMapperService.forRequest().map(createInvoiceRequest, Invoice.class);
		
		invoice.setId(0);

		invoiceTableSetColumns(invoice, createInvoiceRequest);
		
		this.invoiceDao.save(invoice);
		
		return invoice;
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
	
	private void invoiceTableSetColumns(Invoice invoice, CreateInvoiceRequest createInvoiceRequest) {
		
		invoice.setCreationDate(LocalDate.now());
		invoice.setRentDate(invoice.getRental().getRentDate());
		invoice.setReturnDate(invoice.getRental().getReturnDate());
		invoice.setNumberOfDaysRented(dateBetweenCalculator(invoice));
		invoice.setRentTotalPrice(totalPriceCalculator(invoice));
	}

	private int dateBetweenCalculator(Invoice invoice) {
		
		Long dateBetween = ChronoUnit.DAYS.between(invoice.getRentDate(), invoice.getReturnDate());
		int numberDays=dateBetween.intValue()+1;
		
		return numberDays;
	}

	private double totalPriceCalculator(Invoice invoice) {
		
		double rentPrice = invoice.getRental().getTotalPrice();
		int numberDays = invoice.getNumberOfDaysRented();
		
		double additionalServiceDailyPrice=0;
		List<ListOrderedAdditionalServiceDto> listOrderedAdditionalServiceDto = this.orderedAdditionalServiceService.getOrderedAdditionalServiceByRental(invoice.getRental().getId()).getData();
		
		for (int i = 0; i < listOrderedAdditionalServiceDto.size(); i++) {	

			ListAdditionalServiceDto additionalService = this.additionalServiceService.getById(listOrderedAdditionalServiceDto.get(i).getAdditionalServiceId()).getData();
			additionalServiceDailyPrice+=additionalService.getDailyPrice();	
		}
		double additionalServicePrice = additionalServiceDailyPrice*numberDays;
		
		return additionalServicePrice+rentPrice;
	}

}
