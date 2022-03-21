package com.turkcell.rentACarProject.business.concretes;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.turkcell.rentACarProject.business.abstracts.AdditionalServiceService;
import com.turkcell.rentACarProject.business.abstracts.CarMaintenanceService;
import com.turkcell.rentACarProject.business.abstracts.CarService;
import com.turkcell.rentACarProject.business.abstracts.OrderedAdditionalServiceService;
import com.turkcell.rentACarProject.business.abstracts.RentalService;
import com.turkcell.rentACarProject.business.dtos.orderedAdditionalService.ListOrderedAdditionalServiceDto;
import com.turkcell.rentACarProject.business.dtos.rental.ListRentalDto;
import com.turkcell.rentACarProject.business.requests.rental.CreateRentalRequest;
import com.turkcell.rentACarProject.business.requests.rental.DeleteRentalRequest;
import com.turkcell.rentACarProject.business.requests.rental.UpdateRentalRequest;
import com.turkcell.rentACarProject.core.exceptions.BusinessException;
import com.turkcell.rentACarProject.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACarProject.core.utilities.results.DataResult;
import com.turkcell.rentACarProject.core.utilities.results.ErrorDataResult;
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
	private AdditionalServiceService additionalServiceService;
	private OrderedAdditionalServiceService orderedAdditionalServiceService;
	
	@Autowired
	public RentalManager(RentalDao rentalDao, ModelMapperService modelMapperService, 
			@Lazy CarMaintenanceService carMaintenanceService, CarService carService, 
			@Lazy OrderedAdditionalServiceService orderedAdditionalServiceService, 
			AdditionalServiceService additionalServiceService) {
		
		this.rentalDao = rentalDao;
		this.modelMapperService = modelMapperService;
		this.carMaintenanceService = carMaintenanceService;
		this.carService = carService;
		this.additionalServiceService = additionalServiceService;
		this.orderedAdditionalServiceService = orderedAdditionalServiceService;
	}
	
	@Override
	public DataResult<List<ListRentalDto>> getAll() {
		
		var result = this.rentalDao.findAll();
		List<ListRentalDto> response = result.stream()
				.map(rental -> this.modelMapperService.forDto().map(rental, ListRentalDto.class))
				.collect(Collectors.toList());
		
		return new SuccessDataResult<List<ListRentalDto>>(response);
	}

	@Override
	public DataResult<ListRentalDto> getById(int id) {

		Rental result = this.rentalDao.getById(id);
		
		if(result == null) {
			return new ErrorDataResult<ListRentalDto>("CarRental.NotFound");
		}
		
		ListRentalDto response = this.modelMapperService.forDto().map(result, ListRentalDto.class);
		return new SuccessDataResult<ListRentalDto>(response);
	}
	
	@Override
	public Result createForCorporateCustomer(CreateRentalRequest createRentalRequest) throws BusinessException {

		carMaintenanceService.isCarInMaintenance(createRentalRequest.getCarId());
		   
	    Rental rental = this.modelMapperService.forRequest().map(createRentalRequest, Rental.class);
	    this.rentalDao.save(rental);
	    
	    return new SuccessResult("The rental information of the vehicle with id" +createRentalRequest.getCarId()+ "has been added from the database.");
	}

	@Override
	public Result createForIndividualCustomer(CreateRentalRequest createRentalRequest) throws BusinessException {

		carMaintenanceService.isCarInMaintenance(createRentalRequest.getCarId());
		   
	    Rental rental = this.modelMapperService.forRequest().map(createRentalRequest, Rental.class);
	    rental.setInitialMileage(this.carService.getById(createRentalRequest.getCarId()).getData().getMileage());
	    this.rentalDao.save(rental);
	    
	    return new SuccessResult("The rental information of the vehicle with id" +createRentalRequest.getCarId()+ "has been added from the database.");
	}

	@Override
	public Result delete(DeleteRentalRequest deleteRentalRequest) {
		Rental rental = this.modelMapperService.forRequest().map(deleteRentalRequest, Rental.class);
		this.rentalDao.delete(rental);
		return new SuccessResult("The rental information of the vehicle with id "+deleteRentalRequest.getId()+" has been deleted from the database.");
	}

	@Override
	public Result update(UpdateRentalRequest updateRentalRequest) {
		
		Rental rental = this.modelMapperService.forRequest().map(updateRentalRequest, Rental.class);
		rental.setAdditionalPrice(rentalCalculation(rental));
		rental.getCar().setMileage(updateRentalRequest.getReturnMileage());
		updateReturnMileage(rental, updateRentalRequest);
		this.rentalDao.save(rental);
		return new SuccessResult("The rental updated from the database.");
	}
	
	@Override
	public Result isCarRented(int id) throws BusinessException {
		
		if(this.rentalDao.findByCarIdAndReturnDateIsNull(id) != null) {
			throw new BusinessException("Maintenance  can't be added (Car is Rented at requested times)");
		}
		else
			return new SuccessResult();
	}

	private double rentalCalculation(Rental rental) {
		
		double totalPrice = 0;
		
		List<ListOrderedAdditionalServiceDto> orderedAdditionalServiceDtos = orderedAdditionalServiceService.findAllByRentalId(rental.getId()).getData();
		
		if(orderedAdditionalServiceDtos.size() > 0) {
			for(ListOrderedAdditionalServiceDto orderedAdditionalServiceDto : orderedAdditionalServiceDtos) {
				totalPrice += additionalServiceService.findById(orderedAdditionalServiceDto.getAdditionalServiceId()).getData().getPrice(); 
			}	
		}
		
		if(rental.getInitialCity().getId() != rental.getReturnCity().getId())
			totalPrice += 750;

		long days = ChronoUnit.DAYS.between(rental.getRentDate(), rental.getReturnDate());
		
		if(days == 0)
			days = 1;
		
		totalPrice += days * carService.getById(rental.getCar().getId()).getData().getDailyPrice();

		return totalPrice;
	}
	

	private void updateReturnMileage(Rental rental, UpdateRentalRequest updateRentalRequest) {
		
		rental.setReturnMileage(updateRentalRequest.getReturnMileage());
	}
}
