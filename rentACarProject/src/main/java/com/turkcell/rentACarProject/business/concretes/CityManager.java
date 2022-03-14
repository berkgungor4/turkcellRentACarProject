package com.turkcell.rentACarProject.business.concretes;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.turkcell.rentACarProject.business.abstracts.CityService;
import com.turkcell.rentACarProject.business.dtos.city.ListCityDto;
import com.turkcell.rentACarProject.business.requests.city.CreateCityRequest;
import com.turkcell.rentACarProject.core.exceptions.BusinessException;
import com.turkcell.rentACarProject.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACarProject.core.utilities.results.DataResult;
import com.turkcell.rentACarProject.core.utilities.results.ErrorDataResult;
import com.turkcell.rentACarProject.core.utilities.results.Result;
import com.turkcell.rentACarProject.core.utilities.results.SuccessDataResult;
import com.turkcell.rentACarProject.core.utilities.results.SuccessResult;
import com.turkcell.rentACarProject.dataAccess.abstracts.CityDao;
import com.turkcell.rentACarProject.entities.concretes.City;

@Service
public class CityManager implements CityService{

	private CityDao cityDao;
	private ModelMapperService modelMapperService;
	
	
	public CityManager(CityDao cityDao, ModelMapperService modelMapperService) {
		this.cityDao = cityDao;
		this.modelMapperService = modelMapperService;
	}

	@Override
	public DataResult<List<ListCityDto>> getAll() {
		
		var result = this.cityDao.findAll();
		
		List<ListCityDto> response = result.stream()
				.map(city -> this.modelMapperService.forDto().map(city, ListCityDto.class))
				.collect(Collectors.toList());
		
		return new SuccessDataResult<List<ListCityDto>>(response);
	}

	@Override
	public DataResult<ListCityDto> getById(int id) {
		
		City result = this.cityDao.getById(id);
		
		if(result == null) {
			return new ErrorDataResult<ListCityDto>("City.NotFound");
		}
		ListCityDto response = this.modelMapperService.forDto().map(result, ListCityDto.class);		
		
		return new SuccessDataResult<ListCityDto>(response);
	}

	@Override
	public Result create(CreateCityRequest createCityRequest) throws BusinessException {
		
	City city = this.modelMapperService.forRequest().map(createCityRequest, City.class);
		
		this.cityDao.save(city);
		
		return new SuccessResult("City.Added");
	}

}
