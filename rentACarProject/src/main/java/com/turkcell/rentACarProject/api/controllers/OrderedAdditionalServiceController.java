package com.turkcell.rentACarProject.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.turkcell.rentACarProject.business.abstracts.OrderedAdditionalServiceService;
import com.turkcell.rentACarProject.business.dtos.orderedAdditionalService.ListOrderedAdditionalServiceDto;
import com.turkcell.rentACarProject.business.requests.orderedAdditionalService.CreateOrderedAdditionalServiceRequest;
import com.turkcell.rentACarProject.core.utilities.results.DataResult;
import com.turkcell.rentACarProject.core.utilities.results.Result;

@RestController
@RequestMapping("/api/orderedAdditionalServices")
public class OrderedAdditionalServiceController {
	
	private OrderedAdditionalServiceService orderedAdditionalServiceService;

	@Autowired
	public OrderedAdditionalServiceController(OrderedAdditionalServiceService orderedAdditionalServiceService) {
		this.orderedAdditionalServiceService = orderedAdditionalServiceService;
	}
	
	@PostMapping("/create")
	public Result add(@RequestBody CreateOrderedAdditionalServiceRequest createOrderedAdditionalServiceRequest) {
		return this.orderedAdditionalServiceService.add(createOrderedAdditionalServiceRequest);
	}
	
	@GetMapping("/findById")
	public DataResult<List<ListOrderedAdditionalServiceDto>> findAllByRentalId(@RequestParam int rentalId){
		return this.orderedAdditionalServiceService.findAllByRentalId(rentalId);
	}
	
}
