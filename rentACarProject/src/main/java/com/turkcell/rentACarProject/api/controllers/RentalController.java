package com.turkcell.rentACarProject.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.turkcell.rentACarProject.business.abstracts.RentalService;
import com.turkcell.rentACarProject.business.dtos.rental.GetRentalDto;
import com.turkcell.rentACarProject.business.dtos.rental.ListRentalDto;
import com.turkcell.rentACarProject.business.requests.rental.CreateRentalRequest;
import com.turkcell.rentACarProject.business.requests.rental.DeleteRentalRequest;
import com.turkcell.rentACarProject.business.requests.rental.UpdateRentalRequest;
import com.turkcell.rentACarProject.core.utilities.result.DataResult;
import com.turkcell.rentACarProject.core.utilities.result.Result;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

	private RentalService rentalService;
	
	@Autowired
	public RentalController(RentalService rentalService) {
		this.rentalService = rentalService;
	}
	
	@GetMapping("/getAll")
	public DataResult<List<ListRentalDto>> getAll() {
		return rentalService.getAll();
	}

	@GetMapping("/getById")
	public DataResult<GetRentalDto> getById(@RequestParam int id) {
		return rentalService.getById(id);
	}

	@PostMapping("/add")
	public Result add(@RequestBody CreateRentalRequest createRentalRequest) {
		return this.rentalService.add(createRentalRequest);
	}

	@DeleteMapping("/delete")
	public Result delete(@RequestBody DeleteRentalRequest deleteCarRentalRequest) {
		return this.rentalService.delete(deleteCarRentalRequest);
	}

	@PutMapping("/update")
	public Result update(@RequestBody UpdateRentalRequest updateCarRentalRequest) {
		return this.rentalService.update(updateCarRentalRequest);
	}
}
