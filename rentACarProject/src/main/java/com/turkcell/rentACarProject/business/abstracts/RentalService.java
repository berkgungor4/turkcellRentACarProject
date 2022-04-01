package com.turkcell.rentACarProject.business.abstracts;

import java.util.List;

import com.turkcell.rentACarProject.business.dtos.rental.ListRentalDto;
import com.turkcell.rentACarProject.business.requests.rental.CreateRentalRequest;
import com.turkcell.rentACarProject.business.requests.rental.DeleteRentalRequest;
import com.turkcell.rentACarProject.business.requests.rental.UpdateRentalRequest;
import com.turkcell.rentACarProject.core.utilities.results.DataResult;
import com.turkcell.rentACarProject.core.utilities.results.Result;

public interface RentalService {
	
	DataResult<List<ListRentalDto>> getAll();
	DataResult<ListRentalDto> getById(int id);
	DataResult<List<ListRentalDto>> getByCarId(int carId);
	
	Result create(CreateRentalRequest createRentalRequest);  
	Result update(UpdateRentalRequest updateRentalRequest);
	Result delete(DeleteRentalRequest deleteRentalRequest);
	
	Result isCarRented(int carId);
}
