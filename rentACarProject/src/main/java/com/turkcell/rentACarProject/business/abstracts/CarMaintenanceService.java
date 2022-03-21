package com.turkcell.rentACarProject.business.abstracts;

import java.util.List;

import com.turkcell.rentACarProject.business.dtos.carMaintenance.ListCarMaintenanceDto;
import com.turkcell.rentACarProject.business.requests.carMaintenance.CreateCarMaintenanceRequest;
import com.turkcell.rentACarProject.business.requests.carMaintenance.UpdateCarMaintenanceRequest;
import com.turkcell.rentACarProject.core.exceptions.BusinessException;
import com.turkcell.rentACarProject.core.utilities.results.DataResult;
import com.turkcell.rentACarProject.core.utilities.results.Result;

public interface CarMaintenanceService {
	
	DataResult<List<ListCarMaintenanceDto>> getAll();
	DataResult<List<ListCarMaintenanceDto>> getAllByCarId(int id);
	Result create(CreateCarMaintenanceRequest createCarMaintenanceRequest);
	Result update(UpdateCarMaintenanceRequest updateCarMaintenanceRequest);
	Result delete(int id);
	Result isCarInMaintenance(int carId) throws BusinessException;
}
