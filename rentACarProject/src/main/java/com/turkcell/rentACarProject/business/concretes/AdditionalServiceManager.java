package com.turkcell.rentACarProject.business.concretes;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.turkcell.rentACarProject.business.abstracts.AdditionalServiceService;
import com.turkcell.rentACarProject.business.constants.Messages;
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

		List<AdditionalService> result = this.additionalServiceDao.findAll();
		
		List<ListAdditionalServiceDto> response = result.stream().map(additionalService -> this.modelMapperService
				.forDto().map(additionalService, ListAdditionalServiceDto.class)).collect(Collectors.toList());

		return new SuccessDataResult<List<ListAdditionalServiceDto>>(response, Messages.SUCCESS);
	}

	@Override
	public DataResult<ListAdditionalServiceDto> getById(int id) {

		var result = this.additionalServiceDao.getAdditionalServiceById(id);

		if (result != null) {
			ListAdditionalServiceDto response = this.modelMapperService.forDto().map(result,
					ListAdditionalServiceDto.class);

			return new SuccessDataResult<ListAdditionalServiceDto>(response, Messages.SUCCESS);
		}
		return new ErrorDataResult<ListAdditionalServiceDto>(Messages.ADDITIONAL_SERVICE_NOT_FOUND);
	}

	@Override
	public Result create(CreateAdditionalServiceRequest createAdditionalServiceRequest) {

		AdditionalService additionalService = this.modelMapperService.forRequest().map(createAdditionalServiceRequest,
				AdditionalService.class);
		this.additionalServiceDao.save(additionalService);

		return new SuccessResult(Messages.ADDITIONAL_SERVICE_ADD);
	}

	@Override
	public Result update(UpdateAdditionalServiceRequest updateAdditionalServiceRequest) {

		checkIfAdditionalServiceExists(updateAdditionalServiceRequest.getId());

		AdditionalService additionalService = this.additionalServiceDao
				.getAdditionalServiceById(updateAdditionalServiceRequest.getId());

		this.additionalServiceDao.save(additionalService);

		return new SuccessResult(Messages.ADDITIONAL_SERVICE_UPDATE);
	}

	@Override
	public Result delete(DeleteAdditionalServiceRequest deleteAdditionalServiceRequest) {

		checkIfAdditionalServiceExists(deleteAdditionalServiceRequest.getId());

		this.additionalServiceDao.deleteById(deleteAdditionalServiceRequest.getId());

		return new SuccessResult(Messages.ADDITIONAL_SERVICE_DELETE);
	}

	private AdditionalService checkIfAdditionalServiceExists(int id) {

		AdditionalService additionalService = this.additionalServiceDao.getAdditionalServiceById(id);

		if (additionalService == null) {
			throw new BusinessException(Messages.ADDITIONAL_SERVICE_NOT_FOUND);
		}
		return additionalService;
	}

}
