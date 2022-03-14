package com.turkcell.rentACarProject.business.concretes;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.turkcell.rentACarProject.business.abstracts.CustomerService;
import com.turkcell.rentACarProject.business.dtos.customer.ListCustomerDto;
import com.turkcell.rentACarProject.business.requests.customer.CreateCustomerRequest;
import com.turkcell.rentACarProject.core.exceptions.BusinessException;
import com.turkcell.rentACarProject.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACarProject.core.utilities.results.DataResult;
import com.turkcell.rentACarProject.core.utilities.results.ErrorDataResult;
import com.turkcell.rentACarProject.core.utilities.results.Result;
import com.turkcell.rentACarProject.core.utilities.results.SuccessDataResult;
import com.turkcell.rentACarProject.core.utilities.results.SuccessResult;
import com.turkcell.rentACarProject.dataAccess.abstracts.CustomerDao;
import com.turkcell.rentACarProject.entities.concretes.Customer;

@Service
public class CustomerManager implements CustomerService {
	
	private CustomerDao customerDao;
	private ModelMapperService modelMapperService;
	
	@Autowired
	public CustomerManager(CustomerDao customerDao, ModelMapperService modelMapperService) {
		this.customerDao = customerDao;
		this.modelMapperService = modelMapperService;
	}

	@Override
	public DataResult<List<ListCustomerDto>> getAll() {
		
		var result = this.customerDao.findAll();
		List<ListCustomerDto> response = result.stream()
				.map(customer -> this.modelMapperService.forDto().map(customer, ListCustomerDto.class))
				.collect(Collectors.toList());
		return new SuccessDataResult<List<ListCustomerDto>>(response);
	}

	@Override
	public DataResult<ListCustomerDto> getById(int id) {
		
		Customer result = this.customerDao.getById(id);
		if(result == null) {
			return new ErrorDataResult<ListCustomerDto>("Car.NotFound");
		}
		ListCustomerDto response = this.modelMapperService.forDto().map(result, ListCustomerDto.class);		
		return new SuccessDataResult<ListCustomerDto>(response);
	}

	@Override
	public Result create(CreateCustomerRequest createCustomerRequest) throws BusinessException {
		
		Customer customer = this.modelMapperService.forRequest().map(createCustomerRequest, Customer.class);
		this.customerDao.save(customer);
		return new SuccessResult("CustomerAdded");
	}

}
