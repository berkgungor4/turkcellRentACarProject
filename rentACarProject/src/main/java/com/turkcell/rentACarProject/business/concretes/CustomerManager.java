package com.turkcell.rentACarProject.business.concretes;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.turkcell.rentACarProject.business.abstracts.CustomerService;
import com.turkcell.rentACarProject.business.constants.Messages;
import com.turkcell.rentACarProject.business.dtos.customer.ListCustomerDto;
import com.turkcell.rentACarProject.core.exceptions.BusinessException;
import com.turkcell.rentACarProject.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACarProject.core.utilities.results.DataResult;
import com.turkcell.rentACarProject.core.utilities.results.SuccessDataResult;
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
		return new SuccessDataResult<List<ListCustomerDto>>(response, Messages.SUCCESS);
	}

	@Override
	public DataResult<ListCustomerDto> getById(int id) {
		
		Customer result = this.customerDao.getById(id);
		if(result == null) {
			throw new BusinessException(Messages.CUSTOMER_NOT_FOUND);
		}
		ListCustomerDto response = this.modelMapperService.forDto().map(result, ListCustomerDto.class);		
		return new SuccessDataResult<ListCustomerDto>(response, Messages.SUCCESS);
	}

	@Override
	public boolean checkCustomerIfForRental(int id) {
		if(this.customerDao.getById(id)==null) {
			return false;
		}
		return true;
	}

}
