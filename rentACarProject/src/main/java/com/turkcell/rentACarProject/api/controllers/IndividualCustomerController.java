package com.turkcell.rentACarProject.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.turkcell.rentACarProject.business.abstracts.IndividualCustomerService;
import com.turkcell.rentACarProject.business.dtos.individualCustomer.ListIndividualCustomerDto;
import com.turkcell.rentACarProject.business.requests.individualCustomer.CreateIndividualCustomerRequest;
import com.turkcell.rentACarProject.core.utilities.results.DataResult;
import com.turkcell.rentACarProject.core.utilities.results.Result;

@RestController
@RequestMapping("/api/individualCustomers")
public class IndividualCustomerController {
	
	private IndividualCustomerService individualCustomerService;
	
	@Autowired
	public IndividualCustomerController(IndividualCustomerService customerService) {
		this.individualCustomerService = customerService;
	}
	
	@GetMapping("/getAll")
	DataResult<List<ListIndividualCustomerDto>> getAll() {
		return this.individualCustomerService.getAll();
		
	}
	@GetMapping("/getById")
	DataResult<ListIndividualCustomerDto> getById(@RequestParam int id) {
		return this.individualCustomerService.getById(id);
		
	}
	@PostMapping("/create")
	Result create(@RequestBody CreateIndividualCustomerRequest createIndividualCustomerRequest) {
		return this.individualCustomerService.create(createIndividualCustomerRequest);
		
	}

}
