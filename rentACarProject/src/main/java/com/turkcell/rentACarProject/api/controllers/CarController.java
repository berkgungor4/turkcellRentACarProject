package com.turkcell.rentACarProject.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.turkcell.rentACarProject.business.abstracts.CarService;
import com.turkcell.rentACarProject.business.dtos.GetCarDto;
import com.turkcell.rentACarProject.business.dtos.ListCarDto;
import com.turkcell.rentACarProject.business.requests.car.CreateCarRequest;
import com.turkcell.rentACarProject.business.requests.car.DeleteCarRequest;
import com.turkcell.rentACarProject.business.requests.car.UpdateCarRequest;
import com.turkcell.rentACarProject.core.utilities.result.DataResult;
import com.turkcell.rentACarProject.core.utilities.result.Result;

@RestController
@RequestMapping("/api/cars")
public class CarController {

	private CarService carService;

	@Autowired
	public CarController(CarService carService) {
		this.carService = carService;
	}

	@GetMapping("/getAll")
	public DataResult<List<ListCarDto>> getAll() {
		return this.carService.getAll();
	}

	@PostMapping("/create")
	public Result add(@RequestBody CreateCarRequest createCarRequest) {
		return this.carService.create(createCarRequest);
	}

	@DeleteMapping("/delete")
	public Result delete(@RequestBody DeleteCarRequest deleteCarRequest) {
		return this.carService.delete(deleteCarRequest);
	}

	@PutMapping("/update")
	public Result update(@RequestBody UpdateCarRequest updateCarRequest) {
		return this.carService.update(updateCarRequest);
	}

	@GetMapping("/get")
	public DataResult<GetCarDto> get(@RequestParam int id) {
		return this.carService.getById(id);
	}
	
	@GetMapping("/getAllSorted")
	public DataResult<List<ListCarDto>> getAllSorted(Sort.Direction direction) {
		return this.carService.getAllSorted(direction);
	}
	
	@GetMapping("/getAllPaged")
	public DataResult<List<ListCarDto>> getAllPaged(int pageNo, int pageSize) {
		return this.carService.getAllPaged(pageNo, pageSize);
	}
	
	@GetMapping("/getByDailyPriceLessThanEqual")
	public DataResult<List<ListCarDto>> getByDailyPriceLessThanEqual(double dailyPrice) {
		return this.carService.getByDailyPriceLessThanEqual(dailyPrice);
	}
	
}
