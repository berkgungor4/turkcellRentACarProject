package com.turkcell.rentACarProject.business.abstracts;

import java.util.List;

import org.springframework.stereotype.Service;

import com.turkcell.rentACarProject.business.dtos.rental.GetRentalDto;
import com.turkcell.rentACarProject.business.dtos.rental.ListRentalDto;
import com.turkcell.rentACarProject.business.requests.rental.CreateRentalRequest;
import com.turkcell.rentACarProject.business.requests.rental.DeleteRentalRequest;
import com.turkcell.rentACarProject.business.requests.rental.UpdateRentalRequest;
import com.turkcell.rentACarProject.core.utilities.result.DataResult;
import com.turkcell.rentACarProject.core.utilities.result.Result;

@Service
public interface RentalService {
	
	DataResult<List<ListRentalDto>> getAll();
	DataResult<GetRentalDto> getById(int id);
	DataResult<List<GetRentalDto>> getByCarId(int id);
	
    Result add(CreateRentalRequest createRentalRequest);
    Result update(UpdateRentalRequest updateRentalRequest);
    Result delete(DeleteRentalRequest deleteRentalRequest);
}
