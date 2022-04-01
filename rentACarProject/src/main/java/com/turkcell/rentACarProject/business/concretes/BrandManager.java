package com.turkcell.rentACarProject.business.concretes;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.turkcell.rentACarProject.business.abstracts.BrandService;
import com.turkcell.rentACarProject.business.dtos.brand.ListBrandDto;
import com.turkcell.rentACarProject.business.requests.brand.CreateBrandRequest;
import com.turkcell.rentACarProject.business.requests.brand.DeleteBrandRequest;
import com.turkcell.rentACarProject.business.requests.brand.UpdateBrandRequest;
import com.turkcell.rentACarProject.core.exceptions.BusinessException;
import com.turkcell.rentACarProject.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACarProject.core.utilities.results.DataResult;
import com.turkcell.rentACarProject.core.utilities.results.ErrorDataResult;
import com.turkcell.rentACarProject.core.utilities.results.Result;
import com.turkcell.rentACarProject.core.utilities.results.SuccessDataResult;
import com.turkcell.rentACarProject.core.utilities.results.SuccessResult;
import com.turkcell.rentACarProject.dataAccess.abstracts.BrandDao;
import com.turkcell.rentACarProject.entities.concretes.Brand;

@Service
public class BrandManager implements BrandService {

	private BrandDao brandDao;
	private ModelMapperService modelMapperService;

	@Autowired
	public BrandManager(BrandDao brandDao, ModelMapperService modelMapperService) {
		this.brandDao = brandDao;
		this.modelMapperService = modelMapperService;
	}

	@Override
	public DataResult<List<ListBrandDto>> getAll() {
		var result = this.brandDao.findAll();
		List<ListBrandDto> response = result.stream()
				.map(brand -> this.modelMapperService.forDto().map(brand, ListBrandDto.class))
				.collect(Collectors.toList());
		return new SuccessDataResult<List<ListBrandDto>>(response);
	}

	@Override
	public DataResult<ListBrandDto> getById(int id) {
		Brand result = this.brandDao.getByBrandId(id);
		if (result == null) {
			return new ErrorDataResult<ListBrandDto>();
		}
		ListBrandDto response = this.modelMapperService.forDto().map(result, ListBrandDto.class);
		return new SuccessDataResult<ListBrandDto>(response);
	}

	@Override
	public Result create(CreateBrandRequest createBrandRequest) {

		Brand brand = this.modelMapperService.forRequest().map(createBrandRequest, Brand.class);
		this.brandDao.save(brand);
		return new SuccessResult("Brand.Created");
	}

	@Override
	public Result delete(DeleteBrandRequest deleteBrandRequest) {
		Brand brand = this.modelMapperService.forRequest().map(deleteBrandRequest, Brand.class);
		this.brandDao.delete(brand);
		return new SuccessResult("Brand.Deleted");
	}

	@Override
	public Result update(UpdateBrandRequest updateBrandRequest) {
		
		checkIfBrandExists(updateBrandRequest.getId());
		
		Brand brand = this.modelMapperService.forRequest().map(updateBrandRequest, Brand.class);
		this.brandDao.save(brand);
		return new SuccessResult("Brand.Updated");
	}

	private Brand checkIfBrandExists(int id){
		
		Brand brand = this.brandDao.getByBrandId(id) ;
		
		if (brand== null) {
			throw new BusinessException("BusinessMessages.BRANDNOTFOUND");
		}
		return brand;
	}

}
