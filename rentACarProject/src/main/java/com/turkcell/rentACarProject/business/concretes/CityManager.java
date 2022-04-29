package com.turkcell.rentACarProject.business.concretes;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.turkcell.rentACarProject.business.abstracts.CityService;
import com.turkcell.rentACarProject.business.constants.Messages;
import com.turkcell.rentACarProject.business.dtos.city.ListCityDto;
import com.turkcell.rentACarProject.business.requests.city.CreateCityRequest;
import com.turkcell.rentACarProject.business.requests.city.DeleteCityRequest;
import com.turkcell.rentACarProject.business.requests.city.UpdateCityRequest;
import com.turkcell.rentACarProject.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACarProject.core.utilities.results.DataResult;
import com.turkcell.rentACarProject.core.utilities.results.ErrorDataResult;
import com.turkcell.rentACarProject.core.utilities.results.Result;
import com.turkcell.rentACarProject.core.utilities.results.SuccessDataResult;
import com.turkcell.rentACarProject.core.utilities.results.SuccessResult;
import com.turkcell.rentACarProject.dataAccess.abstracts.CityDao;
import com.turkcell.rentACarProject.entities.concretes.City;

@Service
public class CityManager implements CityService {

	private CityDao cityDao;
	private ModelMapperService modelMapperService;

	public CityManager(CityDao cityDao, ModelMapperService modelMapperService) {
		this.cityDao = cityDao;
		this.modelMapperService = modelMapperService;
	}

	@Override
	public DataResult<List<ListCityDto>> getAll() {

		List<City> result = this.cityDao.findAll();

		List<ListCityDto> response = result.stream()
				.map(city -> this.modelMapperService.forDto().map(city, ListCityDto.class))
				.collect(Collectors.toList());

		return new SuccessDataResult<List<ListCityDto>>(response, Messages.SUCCESS);
	}

	@Override
	public DataResult<ListCityDto> getById(int id) {

		City result = this.cityDao.getById(id);

		if (result == null) {
			return new ErrorDataResult<ListCityDto>(Messages.CITY_NOT_FOUND);
		}
		ListCityDto response = this.modelMapperService.forDto().map(result, ListCityDto.class);

		return new SuccessDataResult<ListCityDto>(response, Messages.SUCCESS);
	}

	@Override
	public Result create(CreateCityRequest createCityRequest) {

		City city = this.modelMapperService.forRequest().map(createCityRequest, City.class);
		this.cityDao.save(city);
		
		return new SuccessResult(Messages.CITY_ADD);
	}

	@Override
	public Result update(UpdateCityRequest updateCityRequest) {
		City city = this.modelMapperService.forRequest().map(updateCityRequest, City.class);
		this.cityDao.save(city);
		
		return new SuccessResult(Messages.CITY_UPDATE);
	}

	@Override
	public Result delete(DeleteCityRequest deleteCityRequest) {
		City city = this.modelMapperService.forRequest().map(deleteCityRequest, City.class);
		this.cityDao.delete(city);
		
		return new SuccessResult(Messages.CITY_DELETE);
	}

}
