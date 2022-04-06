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
	private OrderedAdditionalServiceService orderedAdditionalServiceService;
	private AdditionalServiceService additionalServiceService;

	@Autowired
	public InvoiceManager(InvoiceDao invoiceDao, ModelMapperService modelMapperService, CustomerService customerService,RentalService rentalService, OrderedAdditionalServiceService orderedAdditionalServiceService, AdditionalServiceService additionalServiceService) {
		this.invoiceDao = invoiceDao;
		this.modelMapperService = modelMapperService;
		this.orderedAdditionalServiceService = orderedAdditionalServiceService;
		this.additionalServiceService = additionalServiceService;
	}

	@Override
	public DataResult<List<ListInvoiceDto>> getAll() {
		
		var result = this.invoiceDao.findAll();
		List<ListInvoiceDto> response = result.stream()
				.map(brand -> this.modelMapperService.forDto().map(brand, ListInvoiceDto.class))
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
	public DataResult<List<ListInvoiceDto>> getInvoiceByCustomer(int id) {
		
		List<Invoice> result = this.invoiceDao.getByCustomer_id(id);
		List<ListInvoiceDto> response = result.stream()
				.map(invoice -> this.modelMapperService.forDto().map(invoice, ListInvoiceDto.class))
				.collect(Collectors.toList());
		
		return new SuccessDataResult<List<ListInvoiceDto>>(response, Messages.SUCCESS);
	}

	@Override
	public Result create(CreateInvoiceRequest createInvoiceRequest) {
		
		Invoice invoice = this.modelMapperService.forRequest().map(createInvoiceRequest, Invoice.class);
		
		invoice.setId(0);
		
		invoiceTableSetColumns(invoice, createInvoiceRequest);
		
		this.invoiceDao.save(invoice);

		return new SuccessResult(Messages.INVOICE_ADD);
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
		int numberDays=dateBetween.intValue();
		if(numberDays==0) {
			numberDays=1;
		}
		return numberDays;
	}

	private double totalPriceCalculator(Invoice invoice) {
		
		double rentDailyPrice = invoice.getRental().getTotalPrice();
		int numberDays = invoice.getNumberOfDaysRented();
		double rentPrice = rentDailyPrice*numberDays;
		
		double additionalServiceDailyPrice=0;
		List<ListOrderedAdditionalServiceDto> listOrderedAdditionalServiceDto = this.orderedAdditionalServiceService.findAllByRentalId(invoice.getRental().getId()).getData();
		
		for (int i = 0; i < listOrderedAdditionalServiceDto.size(); i++) {	

			ListAdditionalServiceDto additionalService = this.additionalServiceService.getById(listOrderedAdditionalServiceDto.get(i).getAdditionalServiceId()).getData();
			additionalServiceDailyPrice+=additionalService.getPrice();	
		}
		double additionalServicePrice = additionalServiceDailyPrice*numberDays;
		
		return additionalServicePrice+rentPrice;
	}

}
