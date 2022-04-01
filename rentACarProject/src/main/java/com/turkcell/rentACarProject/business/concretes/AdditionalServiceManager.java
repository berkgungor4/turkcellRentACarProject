package com.turkcell.rentACarProject.business.concretes;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.turkcell.rentACarProject.business.abstracts.AdditionalServiceService;
import com.turkcell.rentACarProject.business.dtos.additionalService.ListAdditionalServiceDto;
import com.turkcell.rentACarProject.business.requests.additionalService.CreateAdditionalServiceRequest;
import com.turkcell.rentACarProject.business.requests.additionalService.DeleteAdditionalServiceRequest;
import com.turkcell.rentACarProject.business.requests.additionalService.UpdateAdditionalServiceRequest;
import com.turkcell.rentACarProject.core.exceptions.BusinessException;
import com.turkcell.rentACarProject.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACarProject.core.utilities.results.DataResult;
import com.turkcell.rentACarProject.core.utilities.results.ErrorDataResult;
import com.turkcell.rentACarProject.core.utilities.results.Result;
import com.turkcell.rentACarProject.core.utilities.results.SuccessDataResult;
import com.turkcell.rentACarProject.core.utilities.results.SuccessResult;
import com.turkcell.rentACarProject.dataAccess.abstracts.AdditionalServiceDao;
import com.turkcell.rentACarProject.entities.concretes.AdditionalService;

@Service
public class AdditionalServiceManager implements AdditionalServiceService {

	private ModelMapperService modelMapperService;
	private AdditionalServiceDao additionalServiceDao;

	@Autowired
	public AdditionalServiceManager(ModelMapperService modelMapperService, AdditionalServiceDao additionalServiceDao) {

		this.modelMapperService = modelMapperService;
		this.additionalServiceDao = additionalServiceDao;
	}

	@Override
	public DataResult<List<ListAdditionalServiceDto>> getAll() {

		var result = this.additionalServiceDao.findAll();
		List<ListAdditionalServiceDto> response = result.stream().map(additionalService -> this.modelMapperService
				.forDto().map(additionalService, ListAdditionalServiceDto.class)).collect(Collectors.toList());

		return new SuccessDataResult<List<ListAdditionalServiceDto>>(response, "Success");
	}
	
	@Override
	public DataResult<ListAdditionalServiceDto> getById(int id) {

		var result = this.additionalServiceDao.getByAdditionalServiceId(id);

		if (result != null) {
			ListAdditionalServiceDto response = this.modelMapperService.forDto().map(result,
					ListAdditionalServiceDto.class);

			return new SuccessDataResult<ListAdditionalServiceDto>(response, "");
		}
		return new ErrorDataResult<ListAdditionalServiceDto>("");
	}

	@Override
	public Result create(CreateAdditionalServiceRequest createAdditionalServiceRequest) {

		AdditionalService additionalService = this.modelMapperService.forRequest().map(createAdditionalServiceRequest,
				AdditionalService.class);
		this.additionalServiceDao.save(additionalService);

		return new SuccessResult("");
	}

	@Override
	public Result update(UpdateAdditionalServiceRequest updateAdditionalServiceRequest) {

		checkIfAdditionalServiceExists(updateAdditionalServiceRequest.getId());

		AdditionalService additionalService = this.additionalServiceDao
				.getByAdditionalServiceId(updateAdditionalServiceRequest.getId());

		this.additionalServiceDao.save(additionalService);

		return new SuccessResult("");
	}

	@Override
	public Result delete(DeleteAdditionalServiceRequest deleteAdditionalServiceRequest) {

		checkIfAdditionalServiceExists(deleteAdditionalServiceRequest.getId());

		this.additionalServiceDao.deleteById(deleteAdditionalServiceRequest.getId());

		return new SuccessResult("");
	}

	private void checkIfAdditionalServiceExists(int id) {

		if (!this.additionalServiceDao.existsById(id)) {
			
			throw new BusinessException("Messages.ADDITIONALSERVICENOTFOUND");
		}
	}

}
