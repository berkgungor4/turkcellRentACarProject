package com.turkcell.rentACarProject.api.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.turkcell.rentACarProject.business.abstracts.OrderedAdditionalServiceService;
import com.turkcell.rentACarProject.business.dtos.orderedAdditionalService.ListOrderedAdditionalServiceDto;
import com.turkcell.rentACarProject.business.requests.orderedAdditionalService.CreateOrderedAdditionalServiceRequest;
import com.turkcell.rentACarProject.business.requests.orderedAdditionalService.DeleteOrderedAdditionalServiceRequest;
import com.turkcell.rentACarProject.business.requests.orderedAdditionalService.UpdateOrderedAdditionalServiceRequest;
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
	
	@GetMapping("/getAll")
	DataResult<List<ListOrderedAdditionalServiceDto>> getAll(){
		return this.orderedAdditionalServiceService.getAll();
	}
	
	@GetMapping("/getById")
	DataResult<ListOrderedAdditionalServiceDto> getById(@RequestParam int id){
		return this.orderedAdditionalServiceService.getById(id);
	}
	
	@GetMapping("/findAllByRentalId")
	DataResult<List<ListOrderedAdditionalServiceDto>> findAllByRentalId(@RequestParam int rentalId){
		return this.orderedAdditionalServiceService.findAllByRentalId(rentalId);
	}
	
	@PostMapping("/create")
	public Result add(@RequestBody CreateOrderedAdditionalServiceRequest createOrderedAdditionalServiceRequest) {
		return this.orderedAdditionalServiceService.create(createOrderedAdditionalServiceRequest);
	}
	
	@PutMapping("/update")
	Result update(@RequestBody @Valid UpdateOrderedAdditionalServiceRequest updateOrderedAdditionalServiceRequest){
		return this.orderedAdditionalServiceService.update(updateOrderedAdditionalServiceRequest);
	}
	
	@DeleteMapping("/delete")
	Result delete(@RequestBody DeleteOrderedAdditionalServiceRequest deleteOrderedAdditionalServiceRequest){
		return this.orderedAdditionalServiceService.delete(deleteOrderedAdditionalServiceRequest);
	}
	
}
