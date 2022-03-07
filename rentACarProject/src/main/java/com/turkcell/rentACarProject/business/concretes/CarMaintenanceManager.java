package com.turkcell.rentACarProject.business.concretes;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.turkcell.rentACarProject.business.abstracts.CarMaintenanceService;
import com.turkcell.rentACarProject.business.dtos.carMaintenance.ListCarMaintenanceDto;
import com.turkcell.rentACarProject.business.requests.carMaintenance.CreateCarMaintenanceRequest;
import com.turkcell.rentACarProject.business.requests.carMaintenance.DeleteCarMaintenanceRequest;
import com.turkcell.rentACarProject.business.requests.carMaintenance.UpdateCarMaintenanceRequest;
import com.turkcell.rentACarProject.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACarProject.core.utilities.result.DataResult;
import com.turkcell.rentACarProject.core.utilities.result.ErrorResult;
import com.turkcell.rentACarProject.core.utilities.result.Result;
import com.turkcell.rentACarProject.core.utilities.result.SuccessDataResult;
import com.turkcell.rentACarProject.core.utilities.result.SuccessResult;
import com.turkcell.rentACarProject.dataAccess.abstracts.CarDao;
import com.turkcell.rentACarProject.dataAccess.abstracts.CarMaintenanceDao;
import com.turkcell.rentACarProject.dataAccess.abstracts.RentalDao;
import com.turkcell.rentACarProject.entities.concretes.CarMaintenance;
import com.turkcell.rentACarProject.entities.concretes.Rental;

@Service
public class CarMaintenanceManager implements CarMaintenanceService{

	private CarMaintenanceDao carMaintenanceDao;
	private RentalDao rentalDao;
	private CarDao carDao;
	private ModelMapperService modelMapperService;
	
	@Autowired
	public CarMaintenanceManager(CarMaintenanceDao carMaintenanceDao, ModelMapperService modelMapperService) {
		this.carMaintenanceDao=carMaintenanceDao;
		this.rentalDao=rentalDao;
		this.carDao = carDao;
		this.modelMapperService=modelMapperService;
	}

	@Override
	public DataResult<List<ListCarMaintenanceDto>> getAll() {
		
		var result = this.carMaintenanceDao.findAll();
		
		List<ListCarMaintenanceDto> response = result.stream()
				.map(carMaintenance->this.modelMapperService.forDto().map(carMaintenance, ListCarMaintenanceDto.class))
				.collect(Collectors.toList());
		
		return new SuccessDataResult<List<ListCarMaintenanceDto>>(response);	
	}

	@Override
	public DataResult<List<ListCarMaintenanceDto>> getByCarId(int carId) {

		var result = this.carMaintenanceDao.getByCarId(carId);
		
		List<ListCarMaintenanceDto> response = result.stream()
				.map(carMaintenance->this.modelMapperService.forDto().map(carMaintenance, ListCarMaintenanceDto.class))
				.collect(Collectors.toList());
		
		return new SuccessDataResult<List<ListCarMaintenanceDto>>(response);
	}

	@Override
	public Result add(CreateCarMaintenanceRequest createCarMaintenanceRequest) {
		
		CarMaintenance carMaintenance = this.modelMapperService.forRequest().map(createCarMaintenanceRequest, CarMaintenance.class);
		carMaintenance.setId(0);

		if (!checkIsRented(carMaintenance)) {
			return new ErrorResult("CarMaintenance.NotAdded , Car is already rented!");
		}

		this.carMaintenanceDao.save(carMaintenance);
		return new SuccessResult("Created maintenance information of "+createCarMaintenanceRequest.getDescription()+" car.");
	}

	@Override
	public Result delete(DeleteCarMaintenanceRequest deleteCarMaintenanceRequest) {
		
		CarMaintenance carMaintenance = this.modelMapperService.forRequest().map(deleteCarMaintenanceRequest, CarMaintenance.class);
		this.carMaintenanceDao.delete(carMaintenance);
		return new SuccessResult("The maintenance information of the vehicle with id "+deleteCarMaintenanceRequest.getId()+" has been deleted from the database.");
	}

	@Override
	public Result update(UpdateCarMaintenanceRequest updateCarMaintenanceRequest) {
		
		CarMaintenance carMaintenance = this.modelMapperService.forRequest().map(updateCarMaintenanceRequest, CarMaintenance.class);
		this.carMaintenanceDao.save(carMaintenance);
		return new SuccessResult("Updated maintenance information of "+updateCarMaintenanceRequest.getDescription()+" car.");
	}
	
	private boolean checkIsRented(CarMaintenance carMaintenance) {
		List<Rental> result = this.rentalDao.getRentalsByCarId(carMaintenance.getCar().getId());
		if (result != null) {
			for (Rental rental : result) {
				if (rental.getReturnDate() != null && carMaintenance.getReturnDate().isAfter(rental.getRentDate()) && carMaintenance.getReturnDate().isBefore((rental.getReturnDate()))) {
					return false;
				}
				if (rental.getReturnDate() == null && carMaintenance.getReturnDate().isAfter(rental.getRentDate()) || carMaintenance.getReturnDate().equals(rental.getRentDate())) {
					return false;
				}
			}
		}
		return true;
	}
	
}
