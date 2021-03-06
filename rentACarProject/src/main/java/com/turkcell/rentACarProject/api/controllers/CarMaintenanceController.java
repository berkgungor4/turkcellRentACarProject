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

import com.turkcell.rentACarProject.business.abstracts.CarMaintenanceService;
import com.turkcell.rentACarProject.business.dtos.carMaintenance.ListCarMaintenanceDto;
import com.turkcell.rentACarProject.business.requests.carMaintenance.CreateCarMaintenanceRequest;
import com.turkcell.rentACarProject.business.requests.carMaintenance.DeleteCarMaintenanceRequest;
import com.turkcell.rentACarProject.business.requests.carMaintenance.UpdateCarMaintenanceRequest;
import com.turkcell.rentACarProject.core.utilities.results.DataResult;
import com.turkcell.rentACarProject.core.utilities.results.Result;

@RestController
@RequestMapping("/api/carMaintenances")
public class CarMaintenanceController {

	private CarMaintenanceService carMaintenanceService;

	@Autowired
	public CarMaintenanceController(CarMaintenanceService carMaintenanceService) {
		this.carMaintenanceService = carMaintenanceService;
	}

	@GetMapping("/getAll")
	public DataResult<List<ListCarMaintenanceDto>> getAll() {
		return this.carMaintenanceService.getAll();
	}

	@GetMapping("/getById")
	DataResult<ListCarMaintenanceDto> getById(@RequestParam int id) {
		return this.carMaintenanceService.getById(id);
	}

	@GetMapping("/getAllByCarId")
	public DataResult<List<ListCarMaintenanceDto>> getByCarId(@RequestParam int carId) {
		return this.carMaintenanceService.getCarMaintenanceByCar(carId);
	}

	@PostMapping("/create")
	public Result add(@RequestBody CreateCarMaintenanceRequest createCarMaintenanceRequest) {
		return this.carMaintenanceService.create(createCarMaintenanceRequest);
	}

	@PutMapping("/update")
	public Result update(@RequestBody UpdateCarMaintenanceRequest updateCarMaintenanceRequest) {
		return this.carMaintenanceService.update(updateCarMaintenanceRequest);
	}

	@DeleteMapping("/delete")
	public Result delete(@RequestBody DeleteCarMaintenanceRequest deleteCarMaintenanceRequest) {
		return this.carMaintenanceService.delete(deleteCarMaintenanceRequest);
	}
	
}
