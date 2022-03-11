package com.turkcell.rentACarProject.business.abstracts;

import java.util.List;

import org.springframework.stereotype.Service;

import com.turkcell.rentACarProject.business.dtos.additionalService.ListAdditionalServiceDto;
import com.turkcell.rentACarProject.business.requests.additonalService.CreateAdditionalServiceRequest;
import com.turkcell.rentACarProject.business.requests.additonalService.DeleteAdditionalServiceRequest;
import com.turkcell.rentACarProject.business.requests.additonalService.UpdateAdditionalServiceRequest;
import com.turkcell.rentACarProject.core.utilities.result.DataResult;
import com.turkcell.rentACarProject.core.utilities.result.Result;

@Service
public interface AdditionalServiceService {
	
	DataResult<List<ListAdditionalServiceDto>> getAll();
	
	Result create(CreateAdditionalServiceRequest createAdditionalServiceRequest);
	Result delete(DeleteAdditionalServiceRequest deleteAdditionalServiceRequest);
	Result update(UpdateAdditionalServiceRequest updateAdditionalServiceRequest);
}
