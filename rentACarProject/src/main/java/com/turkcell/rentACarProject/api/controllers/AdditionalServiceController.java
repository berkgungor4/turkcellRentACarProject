package com.turkcell.rentACarProject.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.turkcell.rentACarProject.business.abstracts.AdditionalServiceService;
import com.turkcell.rentACarProject.business.dtos.additionalService.ListAdditionalServiceDto;
import com.turkcell.rentACarProject.business.requests.additionalService.CreateAdditionalServiceRequest;
import com.turkcell.rentACarProject.core.utilities.results.DataResult;
import com.turkcell.rentACarProject.core.utilities.results.Result;

@RestController
@RequestMapping("/api/additionalServices")
public class AdditionalServiceController {
	
	private AdditionalServiceService additionalServiceService;

	@Autowired
	public AdditionalServiceController(AdditionalServiceService additionalServiceService) {
		this.additionalServiceService = additionalServiceService;
	}
	
	@PostMapping("/create")
	public Result add(@RequestBody CreateAdditionalServiceRequest createAdditionalServiceRequest) {
		return this.additionalServiceService.add(createAdditionalServiceRequest);
	}
	
	@GetMapping("/findById")
	public DataResult<ListAdditionalServiceDto> findById(@RequestParam int id){
		return this.additionalServiceService.findById(id);
	}
	
}
