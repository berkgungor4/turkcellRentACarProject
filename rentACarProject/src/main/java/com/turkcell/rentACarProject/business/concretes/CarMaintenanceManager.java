package com.turkcell.rentACarProject.business.concretes;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.turkcell.rentACarProject.business.abstracts.CarMaintenanceService;
import com.turkcell.rentACarProject.business.abstracts.RentalService;
import com.turkcell.rentACarProject.business.dtos.carMaintenance.ListCarMaintenanceDto;
import com.turkcell.rentACarProject.business.requests.carMaintenance.CreateCarMaintenanceRequest;
import com.turkcell.rentACarProject.business.requests.carMaintenance.UpdateCarMaintenanceRequest;
import com.turkcell.rentACarProject.core.exceptions.BusinessException;
import com.turkcell.rentACarProject.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACarProject.core.utilities.results.DataResult;
import com.turkcell.rentACarProject.core.utilities.results.ErrorResult;
import com.turkcell.rentACarProject.core.utilities.results.Result;
import com.turkcell.rentACarProject.core.utilities.results.SuccessDataResult;
import com.turkcell.rentACarProject.core.utilities.results.SuccessResult;
import com.turkcell.rentACarProject.dataAccess.abstracts.CarMaintenanceDao;
import com.turkcell.rentACarProject.entities.concretes.CarMaintenance;

@Service
public class CarMaintenanceManager implements CarMaintenanceService{
	
	private CarMaintenanceDao carMaintenanceDao;
	private ModelMapperService modelMapperService;
	private RentalService rentalService;
	
	@Autowired
	public CarMaintenanceManager(CarMaintenanceDao carMaintenanceDao, ModelMapperService modelMapperService,
			@Lazy RentalService rentalService) {
		this.carMaintenanceDao = carMaintenanceDao;
		this.modelMapperService = modelMapperService;
		this.rentalService = rentalService;
	}

	@Override
	public DataResult<List<ListCarMaintenanceDto>> getAll() {
		var result = this.carMaintenanceDao.findAll();
		List<ListCarMaintenanceDto> response = result.stream()
				.map(carMaintenance -> this.modelMapperService.forDto().map(carMaintenanceDao, ListCarMaintenanceDto.class))
				.collect(Collectors.toList());
		
		return new SuccessDataResult<List<ListCarMaintenanceDto>>(response);
	}

	@Override
    public DataResult<List<ListCarMaintenanceDto>> getAllByCarId(int id) {
		
        List<CarMaintenance> carMaintenanceList = this.carMaintenanceDao.getAllByCarId(id);
        List<ListCarMaintenanceDto> response = carMaintenanceList.stream()
                .map(carMaintenance -> modelMapperService.forDto().map(carMaintenance, ListCarMaintenanceDto.class))
                .collect(Collectors.toList());

        return new SuccessDataResult<List<ListCarMaintenanceDto>>(response);
    }
	@Override
	public Result create(CreateCarMaintenanceRequest createCarMaintenanceRequest) throws BusinessException{
		
		rentalService.isCarRented(createCarMaintenanceRequest.getCarId());
		
		CarMaintenance carMaintenance = this.modelMapperService.forRequest().map(createCarMaintenanceRequest, CarMaintenance.class);
		this.carMaintenanceDao.save(carMaintenance);
		
		return new SuccessResult("Created maintenance information of " + createCarMaintenanceRequest.getDescription() + " car.");
	}

	@Override
	public Result update(UpdateCarMaintenanceRequest updateCarMaintenanceRequest) {
		CarMaintenance carMaintenance = this.modelMapperService.forRequest().map(updateCarMaintenanceRequest, CarMaintenance.class);
		this.carMaintenanceDao.save(carMaintenance);
		return new SuccessResult("Updated maintenance information of " + updateCarMaintenanceRequest.getDescription() + " car.");
	}

	@Override
    public Result delete(int id) {
		
        if (this.carMaintenanceDao.existsById(id)) {
        	carMaintenanceDao.deleteById(id);
            return new SuccessResult("CarMaintenance.Deleted");
        } else
            return new ErrorResult("ThisIdNotFound");
    }

	@Override
	public Result isCarInMaintenance(int carId) throws BusinessException {
		
		if(this.carMaintenanceDao.findByCarIdAndReturnDateIsNull(carId) != null)
			throw new BusinessException("CarIsInMaintenance");
		
		else
			return new SuccessResult();
	}
	
}
