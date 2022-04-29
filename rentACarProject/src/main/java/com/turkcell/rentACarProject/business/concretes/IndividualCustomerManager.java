package com.turkcell.rentACarProject.business.concretes;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.turkcell.rentACarProject.business.abstracts.IndividualCustomerService;
import com.turkcell.rentACarProject.business.constants.Messages;
import com.turkcell.rentACarProject.business.dtos.individualCustomer.ListIndividualCustomerDto;
import com.turkcell.rentACarProject.business.requests.individualCustomer.CreateIndividualCustomerRequest;
import com.turkcell.rentACarProject.business.requests.individualCustomer.DeleteIndividualCustomerRequest;
import com.turkcell.rentACarProject.business.requests.individualCustomer.UpdateIndividualCustomerRequest;
import com.turkcell.rentACarProject.core.exceptions.BusinessException;
import com.turkcell.rentACarProject.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACarProject.core.utilities.results.DataResult;
import com.turkcell.rentACarProject.core.utilities.results.ErrorDataResult;
import com.turkcell.rentACarProject.core.utilities.results.Result;
import com.turkcell.rentACarProject.core.utilities.results.SuccessDataResult;
import com.turkcell.rentACarProject.core.utilities.results.SuccessResult;
import com.turkcell.rentACarProject.dataAccess.abstracts.IndividualCustomerDao;
import com.turkcell.rentACarProject.entities.concretes.IndividualCustomer;

@Service
public class IndividualCustomerManager implements IndividualCustomerService {

	private IndividualCustomerDao individualCustomerDao;
	private ModelMapperService modelMapperService;

	@Autowired
	public IndividualCustomerManager(IndividualCustomerDao customerDao, ModelMapperService modelMapperService) {
		this.individualCustomerDao = customerDao;
		this.modelMapperService = modelMapperService;
	}

	@Override
	public DataResult<List<ListIndividualCustomerDto>> getAll() {

		List<IndividualCustomer> result = this.individualCustomerDao.findAll();

		List<ListIndividualCustomerDto> response = result.stream()
				.map(customer -> this.modelMapperService.forDto().map(customer, ListIndividualCustomerDto.class))
				.collect(Collectors.toList());

		return new SuccessDataResult<List<ListIndividualCustomerDto>>(response, Messages.SUCCESS);
	}

	@Override
	public DataResult<ListIndividualCustomerDto> getById(int id) {

		IndividualCustomer result = this.individualCustomerDao.getById(id);

		if (result == null) {
			return new ErrorDataResult<ListIndividualCustomerDto>(Messages.INDIVIDUAL_CUSTOMER_NOT_FOUND);
		}
		ListIndividualCustomerDto response = this.modelMapperService.forDto().map(result,
				ListIndividualCustomerDto.class);

		return new SuccessDataResult<ListIndividualCustomerDto>(response, Messages.SUCCESS);
	}

	@Override
	public Result create(CreateIndividualCustomerRequest createCustomerRequest) {

		IndividualCustomer customer = this.modelMapperService.forRequest().map(createCustomerRequest,
				IndividualCustomer.class);
		this.individualCustomerDao.save(customer);

		return new SuccessResult(Messages.INDIVIDUAL_CUSTOMER_ADD);
	}

	@Override
	public Result update(UpdateIndividualCustomerRequest updateIndividualCustomerRequest) {

		checkIfIndividualCustomerExists(updateIndividualCustomerRequest.getId());

		IndividualCustomer individualCustomer = this.modelMapperService.forRequest()
				.map(updateIndividualCustomerRequest, IndividualCustomer.class);
		this.individualCustomerDao.save(individualCustomer);

		return new SuccessResult(Messages.INDIVIDUAL_CUSTOMER_UPDATE);
	}

	@Override
	public Result delete(DeleteIndividualCustomerRequest deleteIndividualCustomerRequest) {

		checkIfIndividualCustomerExists(deleteIndividualCustomerRequest.getId());

		this.individualCustomerDao.deleteById(deleteIndividualCustomerRequest.getId());

		return new SuccessResult(Messages.INDIVIDUAL_CUSTOMER_DELETE);
	}

	private void checkIfIndividualCustomerExists(int id) {

		if (!this.individualCustomerDao.existsById(id)) {

			throw new BusinessException(Messages.INDIVIDUAL_CUSTOMER_NOT_FOUND);
		}
	}

}
