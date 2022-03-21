package com.turkcell.rentACarProject.business.abstracts;

import java.util.List;

import com.turkcell.rentACarProject.business.dtos.brand.ListBrandDto;
import com.turkcell.rentACarProject.business.requests.brand.CreateBrandRequest;
import com.turkcell.rentACarProject.business.requests.brand.DeleteBrandRequest;
import com.turkcell.rentACarProject.business.requests.brand.UpdateBrandRequest;
import com.turkcell.rentACarProject.core.exceptions.BusinessException;
import com.turkcell.rentACarProject.core.utilities.results.DataResult;
import com.turkcell.rentACarProject.core.utilities.results.Result;

public interface BrandService {

	DataResult<List<ListBrandDto>> getAll();
	DataResult<ListBrandDto> getById(int id);
	
	Result create(CreateBrandRequest createBrandRequest) throws BusinessException;
	Result delete(DeleteBrandRequest deleteBrandRequest);
	Result update(UpdateBrandRequest updateBrandRequest);
}
