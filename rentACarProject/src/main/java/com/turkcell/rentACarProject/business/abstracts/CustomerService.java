package com.turkcell.rentACarProject.business.abstracts;

import java.util.List;

import com.turkcell.rentACarProject.business.dtos.customer.ListCustomerDto;
import com.turkcell.rentACarProject.core.utilities.results.DataResult;

public interface CustomerService {

	DataResult<List<ListCustomerDto>> getAll();

	DataResult<ListCustomerDto> getById(int id);

	boolean checkCustomerIfForRental(int id);
}
