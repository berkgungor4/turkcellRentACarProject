package com.turkcell.rentACarProject.business.abstracts;

import java.util.List;

import com.turkcell.rentACarProject.business.dtos.customer.ListCustomerDto;
import com.turkcell.rentACarProject.business.requests.customer.CreateCustomerRequest;
import com.turkcell.rentACarProject.core.exceptions.BusinessException;
import com.turkcell.rentACarProject.core.utilities.results.DataResult;
import com.turkcell.rentACarProject.core.utilities.results.Result;

public interface CustomerService {
	
	DataResult<List<ListCustomerDto>> getAll();
	DataResult<ListCustomerDto> getById(int id);
	Result create(CreateCustomerRequest createCustomerRequest) throws BusinessException;
}
