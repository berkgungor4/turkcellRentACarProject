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

import com.turkcell.rentACarProject.business.abstracts.CarDamageService;
import com.turkcell.rentACarProject.business.dtos.carDamage.ListCarDamageDto;
import com.turkcell.rentACarProject.business.requests.carDamage.CreateCarDamageRequest;
import com.turkcell.rentACarProject.business.requests.carDamage.DeleteCarDamageRequest;
import com.turkcell.rentACarProject.business.requests.carDamage.UpdateCarDamageRequest;
import com.turkcell.rentACarProject.core.utilities.results.DataResult;
import com.turkcell.rentACarProject.core.utilities.results.Result;

@RestController
@RequestMapping("/api/carDamages")
public class CarDamageController {

	private CarDamageService carDamageService;
	
	@Autowired
	public CarDamageController(CarDamageService carDamageService) {
		this.carDamageService = carDamageService;
	}
	
	@GetMapping("/getAll")
	public DataResult<List<ListCarDamageDto>> getAll() {
		return this.carDamageService.getAll();
	}
	
	@GetMapping("/getById")
	DataResult<ListCarDamageDto> getById(@RequestParam int id) {
		return this.carDamageService.getById(id);
	}
	
	@GetMapping("/getAllByCarId")
	public DataResult<List<ListCarDamageDto>> getAllByCarId(@RequestParam int id) {
		return this.carDamageService.getCarDamageByCar(id);
	}
	
	@PostMapping("/create")
	public Result add(@RequestBody CreateCarDamageRequest createCarDamageRequest){
		return this.carDamageService.create(createCarDamageRequest);
	}
	
	@PutMapping("/update")
	public Result update(@RequestBody UpdateCarDamageRequest updateCarDamageRequest) {
		return this.carDamageService.update(updateCarDamageRequest);
	}
	
	@DeleteMapping("/delete")
	public Result delete(@RequestBody DeleteCarDamageRequest deleteCarDamageRequest) {
		return this.carDamageService.delete(deleteCarDamageRequest);
	}
	
}
