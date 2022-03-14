package com.turkcell.rentACarProject.business.concretes;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.turkcell.rentACarProject.business.abstracts.CarMaintenanceService;
import com.turkcell.rentACarProject.business.abstracts.RentalService;
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
	
	@Autowired
	public RentalManager(RentalDao rentalDao, ModelMapperService modelMapperService,
			@Lazy CarMaintenanceService carMaintenanceService) {
		this.rentalDao = rentalDao;
		this.modelMapperService = modelMapperService;
		this.carMaintenanceService = carMaintenanceService;
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
	public Result create(CreateRentalRequest createRentalRequest) throws BusinessException{
	  
		carMaintenanceService.isCarInMaintenance(createRentalRequest.getCarId());
	   
	    Rental rental = this.modelMapperService.forRequest().map(createRentalRequest, Rental.class);
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
		rentalCalculation(updateRentalRequest);
		this.rentalDao.save(rental);
		return new SuccessResult("The rental information of the vehicle with id "+updateRentalRequest.getCarId()+" has been updated from the database.");
	}
	
	@Override
	public Result isCarRented(int id) throws BusinessException {
		
		if(this.rentalDao.findByCarIdAndReturnDateIsNull(id) != null) {
			throw new BusinessException("Rental can't be added (Car is under maintenance at requested times)");
		}
		else
			return new SuccessResult();
	}

	@Override
	public Result rentalCalculation(UpdateRentalRequest updateRentalRequest) {
		
		if(updateRentalRequest.getInitialCityId() != updateRentalRequest.getReturnCityId()) {
			updateRentalRequest.setAdditionalPrice(updateRentalRequest.getAdditionalPrice() + 750);
		}
		
		return new SuccessResult();
	}
}
