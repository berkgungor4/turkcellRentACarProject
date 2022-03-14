package com.turkcell.rentACarProject.business.concretes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.turkcell.rentACarProject.business.abstracts.AdditionalServiceService;
import com.turkcell.rentACarProject.business.dtos.additionalService.ListAdditionalServiceDto;
import com.turkcell.rentACarProject.business.requests.additionalService.CreateAdditionalServiceRequest;
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
	public AdditionalServiceManager(ModelMapperService modelMapperService,
			AdditionalServiceDao additionalServiceDao) {

		this.modelMapperService = modelMapperService;
		this.additionalServiceDao = additionalServiceDao;
	}

	@Override
	public Result add(CreateAdditionalServiceRequest createAdditionalServiceRequest) {
		AdditionalService additionalService = this.modelMapperService.forRequest().map(createAdditionalServiceRequest, AdditionalService.class);
		this.additionalServiceDao.save(additionalService);
		return new SuccessResult("");
	}

	@Override
	public DataResult<ListAdditionalServiceDto> findById(int id) {
		if(additionalServiceDao.existsById(id)) {
			AdditionalService additionalService = additionalServiceDao.findById(id).get();
			ListAdditionalServiceDto response = modelMapperService.forDto().map(additionalService, ListAdditionalServiceDto.class);
			return new SuccessDataResult<ListAdditionalServiceDto>(response);
		}else return new ErrorDataResult<ListAdditionalServiceDto>();
	}

	
	
}
