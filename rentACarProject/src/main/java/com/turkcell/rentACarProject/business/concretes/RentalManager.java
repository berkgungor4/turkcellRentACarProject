package com.turkcell.rentACarProject.business.concretes;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.turkcell.rentACarProject.business.abstracts.CarMaintenanceService;
import com.turkcell.rentACarProject.business.abstracts.CarService;
import com.turkcell.rentACarProject.business.abstracts.RentalService;
import com.turkcell.rentACarProject.business.dtos.car.ListCarDto;
import com.turkcell.rentACarProject.business.dtos.rental.ListRentalDto;
import com.turkcell.rentACarProject.business.requests.rental.CreateRentalRequest;
import com.turkcell.rentACarProject.business.requests.rental.DeleteRentalRequest;
import com.turkcell.rentACarProject.business.requests.rental.UpdateRentalRequest;
import com.turkcell.rentACarProject.core.exceptions.BusinessException;
import com.turkcell.rentACarProject.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACarProject.core.utilities.results.DataResult;
import com.turkcell.rentACarProject.core.utilities.results.Result;
import com.turkcell.rentACarProject.core.utilities.results.SuccessDataResult;
import com.turkcell.rentACarProject.core.utilities.results.SuccessResult;
import com.turkcell.rentACarProject.dataAccess.abstracts.RentalDao;
import com.turkcell.rentACarProject.entities.concretes.Rental;

@Service
public class RentalManager implements RentalService {

	private RentalDao rentalDao;
	private ModelMapperService modelMapperService;
	private CarMaintenanceService carMaintenanceService;
	private CarService carService;
	
	@Autowired
	public RentalManager(RentalDao rentalDao, ModelMapperService modelMapperService, 
			@Lazy CarMaintenanceService carMaintenanceService, CarService carService) {
		
		this.rentalDao = rentalDao;
		this.modelMapperService = modelMapperService;
		this.carMaintenanceService = carMaintenanceService;
		this.carService = carService;
	}
	
	@Override
	public DataResult<List<ListRentalDto>> getAll() {
		
		var result = this.rentalDao.findAll();
		List<ListRentalDto> response = result.stream()
				.map(rental -> this.modelMapperService.forDto().map(rental, ListRentalDto.class))
				.collect(Collectors.toList());
		
		return new SuccessDataResult<List<ListRentalDto>>(response, "BusinessMessages.SUCCESS");
	}

	@Override
	public DataResult<ListRentalDto> getById(int id) {

		Rental result = checkIfRentalExists(id);
		
		ListRentalDto response = this.modelMapperService.forRequest().map(result, ListRentalDto.class);
		
		response.setTotalPrice(result.getTotalPrice());

		return new SuccessDataResult<ListRentalDto>(response, "BusinessMessages.SUCCESS");
	}
	
	@Override
	public DataResult<List<ListRentalDto>> getByCarId(int carId) {
		
		List<Rental> result = this.rentalDao.getByCar_CarId(carId);
		List<ListRentalDto> response = result.stream()
			.map(rental -> this.modelMapperService.forDto().map(rental, ListRentalDto.class))
			.collect(Collectors.toList());
			
		return new SuccessDataResult<List<ListRentalDto>>(response, "BusinessMessages.SUCCESS");
	}
	
	@Override
	public Result create(CreateRentalRequest createRentalRequest) {
		
		carMaintenanceService.isCarInMaintenance(createRentalRequest.getCarId());
		   
	    Rental rental = this.modelMapperService.forRequest().map(createRentalRequest, Rental.class);
	    
	    rental.setId(0);
		
		rental.setTotalPrice(rentalCalculation(rental));
		
		rental.setInitialMileage(this.carService.getById(createRentalRequest.getCarId()).getData().getMileage());
		
	    this.rentalDao.save(rental);
	    
	    return new SuccessResult("The rental information of the vehicle with id" +createRentalRequest.getCarId()+ "has been added from the database.");
	}

	@Override
	public Result delete(DeleteRentalRequest deleteRentalRequest) {
		
		checkIfRentalExists(deleteRentalRequest.getId());
		
		Rental rental = this.modelMapperService.forRequest().map(deleteRentalRequest, Rental.class);
		this.rentalDao.delete(rental);
		return new SuccessResult("The rental information of the vehicle with id "+deleteRentalRequest.getId()+" has been deleted from the database.");
	}

	@Override
	public Result update(UpdateRentalRequest updateRentalRequest) {
		
		checkIfRentalExists(updateRentalRequest.getId());
		
		Rental rental = this.rentalDao.getByRentalId(updateRentalRequest.getId());
		rental.setTotalPrice(updateTotalPrice(rental, updateRentalRequest)); 
		rental.getCar().setMileage(updateRentalRequest.getReturnMileage());
		
		rental.setReturnDate(updateRentalRequest.getReturnDate());
		rental.setReturnMileage(updateRentalRequest.getReturnMileage());
		
		this.rentalDao.save(rental);
		return new SuccessResult("The rental updated from the database.");
	}
	
	@Override
	public Result isCarRented(int carId) throws BusinessException {
		
		if(this.rentalDao.getByRentalId(carId) != null) {
			throw new BusinessException("Maintenance  can't be added (Car is Rented at requested times)");
		}
		else
			return new SuccessResult();
	}
	
	private Rental checkIfRentalExists(int id){
		
		Rental rental = this.rentalDao.getById(id);
		if (rental == null) {
			throw new BusinessException("BusinessMessages.RENTALCARNOTFOUND");
		}
		return rental;
	}

	private double rentalCalculation(Rental rental) {
		
		ListCarDto car = this.carService.getById(rental.getCar().getId()).getData();
		
		long dateBetween = ChronoUnit.DAYS.between(rental.getRentDate(), rental.getReturnDate());
		if(dateBetween==0) {
			dateBetween=1;
		}
		
		double rentPrice=car.getDailyPrice();
		double totalPrice=rentPrice*dateBetween;
 
        if(rental.getInitialCity().getId()!=rental.getReturnCity().getId()) {
        	totalPrice=totalPrice+750;
        }
        
    		return totalPrice;
	}
	
	private double updateTotalPrice(Rental rental, UpdateRentalRequest updateRentalRequest) {

		long dateBetween = ChronoUnit.DAYS.between(rental.getReturnDate(), updateRentalRequest.getReturnDate());
		
		ListCarDto car = this.carService.getById(rental.getCar().getId()).getData();
		
		double totalPrice=0;
		double rentPrice=car.getDailyPrice();
		
	    if(dateBetween!=0) {
	    	
	    if(rental.getInitialCity().getId()!=updateRentalRequest.getReturnCityId()) {
	        	totalPrice=totalPrice+750;	
		}
	    
		totalPrice=rentPrice*dateBetween;
	}	
	    System.out.println(totalPrice);
	    return totalPrice;
	}

}
