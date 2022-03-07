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
import org.springframework.web.bind.annotation.RestController;

import com.turkcell.rentACarProject.business.abstracts.CarMaintenanceService;
import com.turkcell.rentACarProject.business.dtos.carMaintenance.GetCarMaintenanceDto;
import com.turkcell.rentACarProject.business.dtos.carMaintenance.ListCarMaintenanceDto;
import com.turkcell.rentACarProject.business.requests.carMaintenance.CreateCarMaintenanceRequest;
import com.turkcell.rentACarProject.business.requests.carMaintenance.DeleteCarMaintenanceRequest;
import com.turkcell.rentACarProject.business.requests.carMaintenance.UpdateCarMaintenanceRequest;
import com.turkcell.rentACarProject.core.utilities.result.DataResult;
import com.turkcell.rentACarProject.core.utilities.result.Result;

@RestController
@RequestMapping("/api/carMaintenances")
public class CarMaintenanceController {

	private CarMaintenanceService carMaintenanceService;
	
	@Autowired
	public CarMaintenanceController(CarMaintenanceService carMaintenanceService){
		this.carMaintenanceService=carMaintenanceService;
	}
	
	@GetMapping("/getall")
	public DataResult<List<ListCarMaintenanceDto>> getAll(){
			return	this.carMaintenanceService.getAll();
	}
	
	@PostMapping("/create")
	public Result add(@RequestBody @Valid CreateCarMaintenanceRequest createCarMaintenanceRequest) {
		return this.carMaintenanceService.add(createCarMaintenanceRequest);
	}
	
	@DeleteMapping("/delete")
	public Result delete(DeleteCarMaintenanceRequest deleteCarMaintenanceRequest) {
		return this.carMaintenanceService.delete(deleteCarMaintenanceRequest);
	}
	
	@PutMapping("/update")
	public Result update(UpdateCarMaintenanceRequest updateCarMaintenanceRequest) {
		return this.carMaintenanceService.update(updateCarMaintenanceRequest);
	}
	
	@GetMapping("/getByCarId")
	public DataResult<List<GetCarMaintenanceDto>> getByCarId(int carId){
		return this.carMaintenanceService.getByCarId(carId);
	}
	
}
