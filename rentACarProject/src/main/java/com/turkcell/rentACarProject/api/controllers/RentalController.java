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

import com.turkcell.rentACarProject.business.abstracts.RentalService;
import com.turkcell.rentACarProject.business.dtos.rental.ListRentalDto;
import com.turkcell.rentACarProject.business.requests.rental.CreateRentalRequest;
import com.turkcell.rentACarProject.business.requests.rental.DeleteRentalRequest;
import com.turkcell.rentACarProject.business.requests.rental.UpdateRentalRequest;
import com.turkcell.rentACarProject.core.utilities.results.DataResult;
import com.turkcell.rentACarProject.core.utilities.results.Result;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {
	
	private RentalService rentalService;
	
	@Autowired
	public RentalController(RentalService rentalService) {
		this.rentalService = rentalService;
	}
	
	@GetMapping("/getAll")
	DataResult<List<ListRentalDto>> getAll() {
		return this.rentalService.getAll();
	}

	@GetMapping("/getById")
	DataResult<ListRentalDto> getById(@RequestParam int id) {
		return this.rentalService.getById(id);
	}
	
	@GetMapping("/getByCarId")
	DataResult<List<ListRentalDto>> getByCarId(int carId) {
		return this.rentalService.getByCarId(carId);
	}
	
	@PostMapping("/create")
	Result create(@RequestBody @Valid CreateRentalRequest createRentalRequest) {
		return this.rentalService.create(createRentalRequest);
	}
	
	@DeleteMapping("/delete")
	Result delete(@RequestBody DeleteRentalRequest deleteCarRequest) {
		return this.rentalService.delete(deleteCarRequest);		
	}

	@PutMapping("/update")
	Result update(@RequestBody UpdateRentalRequest updateCarRequest) {
		return this.rentalService.update(updateCarRequest);
	}
	
}
