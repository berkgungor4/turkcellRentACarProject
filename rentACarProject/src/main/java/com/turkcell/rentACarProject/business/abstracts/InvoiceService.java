package com.turkcell.rentACarProject.business.abstracts;

import java.time.LocalDate;
import java.util.List;

import com.turkcell.rentACarProject.business.dtos.invoice.ListInvoiceDto;
import com.turkcell.rentACarProject.business.requests.invoice.CreateInvoiceRequest;
import com.turkcell.rentACarProject.business.requests.invoice.DeleteInvoiceRequest;
import com.turkcell.rentACarProject.business.requests.invoice.UpdateInvoiceRequest;
import com.turkcell.rentACarProject.core.utilities.results.DataResult;
import com.turkcell.rentACarProject.core.utilities.results.Result;
import com.turkcell.rentACarProject.entities.concretes.Invoice;

public interface InvoiceService {

	DataResult<List<ListInvoiceDto>> getAll();

	DataResult<ListInvoiceDto> getById(int id);

	DataResult<List<ListInvoiceDto>> getByDateOfBetween(LocalDate creationDate, LocalDate returnDate);

	DataResult<List<ListInvoiceDto>> getInvoiceByCustomer(int customerId);

	DataResult<List<ListInvoiceDto>> getInvoiceByRental(int rentalId);

	Invoice createForCustomer(CreateInvoiceRequest createInvoiceRequest);

	Result update(UpdateInvoiceRequest updateInvoiceRequest);

	Result delete(DeleteInvoiceRequest deleteInvoiceRequest);
}
