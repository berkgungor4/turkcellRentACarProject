package com.turkcell.rentACarProject.business.concretes;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.turkcell.rentACarProject.business.abstracts.CorporateCustomerService;
import com.turkcell.rentACarProject.business.dtos.corporateCustomer.ListCorporateCustomerDto;
import com.turkcell.rentACarProject.business.requests.corporateCustomer.CreateCorporateCustomerRequest;
import com.turkcell.rentACarProject.core.exceptions.BusinessException;
import com.turkcell.rentACarProject.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACarProject.core.utilities.results.DataResult;
import com.turkcell.rentACarProject.core.utilities.results.ErrorDataResult;
import com.turkcell.rentACarProject.core.utilities.results.Result;
import com.turkcell.rentACarProject.core.utilities.results.SuccessDataResult;
import com.turkcell.rentACarProject.core.utilities.results.SuccessResult;
import com.turkcell.rentACarProject.dataAccess.abstracts.CorporateCustomerDao;
import com.turkcell.rentACarProject.entities.concretes.CorporateCustomer;

@Service
public class CorporateCustomerManager implements CorporateCustomerService {

	private CorporateCustomerDao corporateCustomerDao;
	private ModelMapperService modelMapperService;
	
	@Autowired
	public CorporateCustomerManager(CorporateCustomerDao corporateCustomerDao, ModelMapperService modelMapperService) {
		this.corporateCustomerDao = corporateCustomerDao;
		this.modelMapperService = modelMapperService;
	}

	@Override
	public DataResult<List<ListCorporateCustomerDto>> getAll() {

		var result = this.corporateCustomerDao.findAll();
		
		List<ListCorporateCustomerDto> response = result.stream()
				.map(corporateCustomer -> this.modelMapperService.forDto().map(corporateCustomer, ListCorporateCustomerDto.class))
				.collect(Collectors.toList());
		
		return new SuccessDataResult<List<ListCorporateCustomerDto>>(response);
	}

	@Override
	public DataResult<ListCorporateCustomerDto> getById(int id) {

		CorporateCustomer result = this.corporateCustomerDao.getById(id);
		
		if(result == null) {
			return new ErrorDataResult<ListCorporateCustomerDto>("Car.NotFound");
		}
		ListCorporateCustomerDto response = this.modelMapperService.forDto().map(result, ListCorporateCustomerDto.class);		
		
		return new SuccessDataResult<ListCorporateCustomerDto>(response);
	}

	@Override
	public Result create(CreateCorporateCustomerRequest createCorporateCustomerRequest) throws BusinessException {
		
		CorporateCustomer customer = this.modelMapperService.forRequest().map(createCorporateCustomerRequest, CorporateCustomer.class);
		
		this.corporateCustomerDao.save(customer);
		
		return new SuccessResult("CorporateCustomer.Added");
	}

}
