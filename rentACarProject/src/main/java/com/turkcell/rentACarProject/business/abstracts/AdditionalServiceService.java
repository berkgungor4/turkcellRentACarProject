package com.turkcell.rentACarProject.business.abstracts;

import com.turkcell.rentACarProject.business.dtos.additionalService.ListAdditionalServiceDto;
import com.turkcell.rentACarProject.business.requests.additionalService.CreateAdditionalServiceRequest;
import com.turkcell.rentACarProject.core.utilities.results.DataResult;
import com.turkcell.rentACarProject.core.utilities.results.Result;

public interface AdditionalServiceService {

	Result add(CreateAdditionalServiceRequest createAdditionalServiceRequest);
	DataResult<ListAdditionalServiceDto> findById(int id);
}
