package com.turkcell.rentACarProject.business.concretes;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sun.net.httpserver.Authenticator.Result;
import com.turkcell.rentACarProject.business.abstracts.RentalService;
import com.turkcell.rentACarProject.business.dtos.carMaintenance.ListCarMaintenanceDto;
import com.turkcell.rentACarProject.business.dtos.rental.GetRentalDto;
import com.turkcell.rentACarProject.business.dtos.rental.ListRentalDto;
import com.turkcell.rentACarProject.business.requests.rental.CreateRentalRequest;
import com.turkcell.rentACarProject.business.requests.rental.DeleteRentalRequest;
import com.turkcell.rentACarProject.business.requests.rental.UpdateRentalRequest;
import com.turkcell.rentACarProject.core.exceptions.BusinessException;
import com.turkcell.rentACarProject.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACarProject.core.utilities.result.DataResult;
import com.turkcell.rentACarProject.core.utilities.result.ErrorDataResult;
import com.turkcell.rentACarProject.core.utilities.result.ErrorResult;
import com.turkcell.rentACarProject.core.utilities.result.SuccessDataResult;
import com.turkcell.rentACarProject.core.utilities.result.SuccessResult;
import com.turkcell.rentACarProject.dataAccess.abstracts.CarDao;
import com.turkcell.rentACarProject.dataAccess.abstracts.CarMaintenanceDao;
import com.turkcell.rentACarProject.dataAccess.abstracts.RentalDao;
import com.turkcell.rentACarProject.entities.concretes.Car;
import com.turkcell.rentACarProject.entities.concretes.CarMaintenance;
import com.turkcell.rentACarProject.entities.concretes.Rental;

@Service
public class RentalManager implements RentalService {
	
	private RentalDao rentalDao;
	private CarMaintenanceDao carMaintenanceDao;
	private CarDao carDao;
	private ModelMapperService modelMapperService;
	
	@Autowired
	public RentalManager(RentalDao rentalDao, ModelMapperService modelMapperService) {
		this.rentalDao=rentalDao;
		this.carMaintenanceDao=carMaintenanceDao;
		this.carDao = carDao;
		this.modelMapperService=modelMapperService;
	}
	
	@Override
	public DataResult<List<ListRentalDto>> getAll() {
		
		var result = this.rentalDao.findAll();
		
		List<ListRentalDto> response = result.stream()
				.map(carRental -> modelMapperService.forDto().map(carRental, ListRentalDto.class))
				.collect(Collectors.toList());
		
		return new SuccessDataResult<List<ListRentalDto>>(response);
	}

	@Override
	public DataResult<GetRentalDto> getById(int id) {
		
		Rental rental = rentalDao.getById(id);
		if (rental != null) {
			GetRentalDto response = modelMapperService.forDto().map(rental, GetRentalDto.class);
			return new SuccessDataResult<GetRentalDto>(response);
		}
		return new ErrorDataResult<GetRentalDto>("CarRental.NotFound");
	}

	@Override
	public DataResult<List<GetRentalDto>> getByCarId(int carId) {
		
		var result = this.rentalDao.getRentalsByCarId(carId);
        
        List<GetRentalDto> response = result.stream()
        		.map(rental -> this.modelMapperService.forDto().map(rental, GetRentalDto.class))
        		.collect(Collectors.toList());
        return new SuccessDataResult<List<GetRentalDto>>(response);
	}

	@Override
	public Result add(CreateRentalRequest createRentalRequest) {
		
		Rental rental = this.modelMapperService.forRequest().map(createRentalRequest, Rental.class);
		
		if (!checkIsUnderMaintenance(rental)) {
			return new ErrorResult("CarRental.NotAdded , Car is under maintenance!");
		}
		this.rentalDao.save(rental);
		return new SuccessResult("The rental information of the vehicle with id "+createRentalRequest.getCarId()+" has been updated from the database.");
	}

	@Override
	public Result update(UpdateRentalRequest updateRentalRequest) {
		
		Rental rental = this.modelMapperService.forRequest().map(updateRentalRequest, Rental.class);

		if (!checkIsUnderMaintenance(rental)) {
			return new ErrorResult("CarRental.NotUpdated , Car is under maintenance at requested times");
		}
		this.rentalDao.save(rental);
		return new SuccessResult("The rental information of the vehicle with id "+updateRentalRequest.getCarId()+" has been updated from the database.");
	}

	@Override
	public Result delete(DeleteRentalRequest deleteRentalRequest) {
		Rental rental = this.modelMapperService.forRequest().map(deleteRentalRequest, Rental.class);
		this.rentalDao.delete(rental);
		return new SuccessResult("The rental information of the vehicle with id "+deleteRentalRequest.getId()+" has been deleted from the database.");
	}
	
	private boolean checkIsUnderMaintenance(Rental rental) {
		List<CarMaintenance> result = this.carMaintenanceDao.getByCarId(rental.getCar().getId());
		if (result != null) {
			for (CarMaintenance carMaintenance : result) {
				if (rental.getRentDate().isBefore(carMaintenance.getReturnDate()) || rental.getReturnDate().isBefore(carMaintenance.getReturnDate())) {
					return false;
				}
			}
		}
		return true;
	}
}
