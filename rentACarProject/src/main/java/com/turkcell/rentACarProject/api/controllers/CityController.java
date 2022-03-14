package com.turkcell.rentACarProject.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.turkcell.rentACarProject.business.abstracts.CityService;
import com.turkcell.rentACarProject.business.dtos.city.ListCityDto;
import com.turkcell.rentACarProject.business.requests.city.CreateCityRequest;
import com.turkcell.rentACarProject.core.exceptions.BusinessException;
import com.turkcell.rentACarProject.core.utilities.results.DataResult;
import com.turkcell.rentACarProject.core.utilities.results.Result;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/cities")
public class CityController {
	
	private CityService cityService;

	@Autowired
	public CityController(CityService cityService) {
		this.cityService = cityService;
	}
	

	DataResult<List<ListCityDto>> getAll() {
		return this.cityService.getAll();
		
	}
	DataResult<ListCityDto> getById(@RequestParam int id){
		return this.cityService.getById(id); 
		
	}
	
	Result create(@RequestBody CreateCityRequest createCityRequest) throws BusinessException {
		return this.cityService.create(createCityRequest);		
	}
	
}
