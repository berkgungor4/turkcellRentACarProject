package com.turkcell.rentACarProject.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkcell.rentACarProject.business.abstracts.InvoiceService;
import com.turkcell.rentACarProject.business.dtos.invoice.ListInvoiceDto;
import com.turkcell.rentACarProject.business.requests.invoice.CreateInvoiceRequest;
import com.turkcell.rentACarProject.business.requests.invoice.DeleteInvoiceRequest;
import com.turkcell.rentACarProject.business.requests.invoice.UpdateInvoiceRequest;
import com.turkcell.rentACarProject.core.utilities.results.DataResult;
import com.turkcell.rentACarProject.core.utilities.results.Result;

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
	
	@PostMapping("/create")
	Result create(@RequestBody CreateInvoiceRequest createInvoiceRequest) {
		return this.invoiceService.create(createInvoiceRequest);
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
