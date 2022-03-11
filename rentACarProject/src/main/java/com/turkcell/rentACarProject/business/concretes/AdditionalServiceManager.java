package com.turkcell.rentACarProject.business.concretes;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.turkcell.rentACarProject.business.abstracts.AdditionalServiceService;
import com.turkcell.rentACarProject.business.dtos.additionalService.ListAdditionalServiceDto;
import com.turkcell.rentACarProject.business.requests.additonalService.CreateAdditionalServiceRequest;
import com.turkcell.rentACarProject.business.requests.additonalService.DeleteAdditionalServiceRequest;
import com.turkcell.rentACarProject.business.requests.additonalService.UpdateAdditionalServiceRequest;
import com.turkcell.rentACarProject.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACarProject.core.utilities.result.DataResult;
import com.turkcell.rentACarProject.core.utilities.result.Result;
import com.turkcell.rentACarProject.core.utilities.result.SuccessDataResult;
import com.turkcell.rentACarProject.core.utilities.result.SuccessResult;
import com.turkcell.rentACarProject.dataAccess.abstracts.AdditionalServiceDao;
import com.turkcell.rentACarProject.entities.concretes.AdditionalService;

@Service
public class AdditionalServiceManager implements AdditionalServiceService {
	
	private AdditionalServiceDao additionalServiceDao;
	private ModelMapperService modelMapperService;
	
	@Autowired
	public AdditionalServiceManager(AdditionalServiceDao additionalServiceDao, ModelMapperService modelMapperService) {
		this.additionalServiceDao = additionalServiceDao;
		this.modelMapperService = modelMapperService;
	}

	@Override
	public DataResult<List<ListAdditionalServiceDto>> getAll() {
		var result = this.additionalServiceDao.findAll();
		List<ListAdditionalServiceDto> response = result.stream()
				.map(additonalService -> this.modelMapperService.forDto().map(additonalService, ListAdditionalServiceDto.class))
				.collect(Collectors.toList());
		return new SuccessDataResult<List<ListAdditionalServiceDto>>(response);
	}

	@Override
	public Result create(CreateAdditionalServiceRequest createAdditionalServiceRequest) {
		AdditionalService additionalService = this.modelMapperService.forRequest().map(createAdditionalServiceRequest, AdditionalService.class);
		this.additionalServiceDao.save(additionalService);
		return new SuccessResult("AdditionalService.Created");
	}

	@Override
	public Result delete(DeleteAdditionalServiceRequest deleteAdditionalServiceRequest) {
		AdditionalService additionalService = this.modelMapperService.forRequest().map(deleteAdditionalServiceRequest, AdditionalService.class);
		this.additionalServiceDao.delete(additionalService);
		return new SuccessResult("AdditionalService.Deleted");
	}

	@Override
	public Result update(UpdateAdditionalServiceRequest updateAdditionalServiceRequest) {
		AdditionalService additionalService = this.modelMapperService.forRequest().map(updateAdditionalServiceRequest, AdditionalService.class);
		this.additionalServiceDao.save(additionalService);
		return new SuccessResult("AdditionalService.Updated");
	}

}
