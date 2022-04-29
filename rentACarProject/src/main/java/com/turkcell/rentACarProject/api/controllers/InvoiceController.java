package com.turkcell.rentACarProject.api.controllers;

import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.turkcell.rentACarProject.business.abstracts.InvoiceService;
import com.turkcell.rentACarProject.business.dtos.invoice.ListInvoiceDto;
import com.turkcell.rentACarProject.business.requests.invoice.CreateInvoiceRequest;
import com.turkcell.rentACarProject.business.requests.invoice.DeleteInvoiceRequest;
import com.turkcell.rentACarProject.business.requests.invoice.UpdateInvoiceRequest;
import com.turkcell.rentACarProject.core.utilities.results.DataResult;
import com.turkcell.rentACarProject.core.utilities.results.Result;
import com.turkcell.rentACarProject.entities.concretes.Invoice;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {
	
	private InvoiceService invoiceService;

	@Autowired
	public InvoiceController(InvoiceService invoiceService) {
		this.invoiceService = invoiceService;
	}
	
	@GetMapping("/getAll")
	DataResult<List<ListInvoiceDto>> getAll(){
	return this.invoiceService.getAll();
	}
	
	@GetMapping("/getById")
	DataResult<ListInvoiceDto> getById(@RequestParam int id){
		return this.invoiceService.getById(id);
	}
	
	@GetMapping("/getByDateOfBetween")
	DataResult<List<ListInvoiceDto>> getByDateOfBetween (@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate creationDate, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate returnDate){
		return this.invoiceService.getByDateOfBetween(creationDate, returnDate);
	}
	
	@GetMapping("/getInvoiceByCustomer")
	DataResult<List<ListInvoiceDto>> getInvoiceByCustomer(@RequestParam int customerId) {
		return this.invoiceService.getInvoiceByCustomer(customerId);
	}
	
	@GetMapping("/getInvoiceByRental")
	DataResult<List<ListInvoiceDto>> getInvoiceByRental(@RequestParam int rentalId){
		return this.invoiceService.getInvoiceByRental(rentalId);
	}
	
	@PostMapping("/create")
	public Invoice createForCustomer(@RequestBody @Valid CreateInvoiceRequest createInvoiceRequest) {
		return this.invoiceService.createForCustomer(createInvoiceRequest);
	}
	
	@DeleteMapping("/delete")
	public Result delete(@RequestBody DeleteInvoiceRequest deleteInvoiceRequest) {
		return this.invoiceService.delete(deleteInvoiceRequest);
	}
	
	@PutMapping("/update")
	public Result update(@RequestBody UpdateInvoiceRequest updateInvoiceRequest) {
		return this.invoiceService.update(updateInvoiceRequest);
	}
}
