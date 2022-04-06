package com.turkcell.rentACarProject.business.concretes;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.turkcell.rentACarProject.business.abstracts.CarMaintenanceService;
import com.turkcell.rentACarProject.business.abstracts.RentalService;
import com.turkcell.rentACarProject.business.constants.Messages;
import com.turkcell.rentACarProject.business.dtos.carMaintenance.ListCarMaintenanceDto;
import com.turkcell.rentACarProject.business.requests.carMaintenance.CreateCarMaintenanceRequest;
import com.turkcell.rentACarProject.business.requests.carMaintenance.DeleteCarMaintenanceRequest;
import com.turkcell.rentACarProject.business.requests.carMaintenance.UpdateCarMaintenanceRequest;
import com.turkcell.rentACarProject.core.exceptions.BusinessException;
import com.turkcell.rentACarProject.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACarProject.core.utilities.results.DataResult;
import com.turkcell.rentACarProject.core.utilities.results.ErrorDataResult;
import com.turkcell.rentACarProject.core.utilities.results.Result;
import com.turkcell.rentACarProject.core.utilities.results.SuccessDataResult;
import com.turkcell.rentACarProject.core.utilities.results.SuccessResult;
import com.turkcell.rentACarProject.dataAccess.abstracts.CarMaintenanceDao;
import com.turkcell.rentACarProject.entities.concretes.CarMaintenance;

@Service
public class CarMaintenanceManager implements CarMaintenanceService {

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
		List<ListCarMaintenanceDto> response = result.stream().map(
				carMaintenance -> this.modelMapperService.forDto().map(carMaintenance, ListCarMaintenanceDto.class))
				.collect(Collectors.toList());

		return new SuccessDataResult<List<ListCarMaintenanceDto>>(response, Messages.SUCCESS);
	}

	@Override
	public DataResult<ListCarMaintenanceDto> getById(int id) {

		CarMaintenance result = this.carMaintenanceDao.getById(id);
		if (result == null) {
			return new ErrorDataResult<ListCarMaintenanceDto>(Messages.CAR_MAINTENANCE_NOT_FOUND);
		}
		ListCarMaintenanceDto response = this.modelMapperService.forDto().map(result, ListCarMaintenanceDto.class);
		return new SuccessDataResult<ListCarMaintenanceDto>(response, Messages.SUCCESS);
	}

	@Override
	public DataResult<List<ListCarMaintenanceDto>> getByCarId(int carId) {

		List<CarMaintenance> carMaintenanceList = this.carMaintenanceDao.getByCar_id(carId);
		List<ListCarMaintenanceDto> response = carMaintenanceList.stream()
				.map(carMaintenance -> modelMapperService.forDto().map(carMaintenance, ListCarMaintenanceDto.class))
				.collect(Collectors.toList());

		return new SuccessDataResult<List<ListCarMaintenanceDto>>(response, Messages.SUCCESS);
	}

	@Override
	public Result create(CreateCarMaintenanceRequest createCarMaintenanceRequest) {

		rentalService.isCarRented(createCarMaintenanceRequest.getCarId());

		CarMaintenance carMaintenance = this.modelMapperService.forRequest().map(createCarMaintenanceRequest,
				CarMaintenance.class);
		this.carMaintenanceDao.save(carMaintenance);

		return new SuccessResult(Messages.CAR_MAINTENANCE_ADD);
	}

	@Override
	public Result update(UpdateCarMaintenanceRequest updateCarMaintenanceRequest) {

		CarMaintenance carMaintenance = this.modelMapperService.forRequest().map(updateCarMaintenanceRequest,
				CarMaintenance.class);
		this.carMaintenanceDao.save(carMaintenance);
		return new SuccessResult(Messages.CAR_MAINTENANCE_UPDATE);
	}

	@Override
	public Result delete(DeleteCarMaintenanceRequest deleteCarMaintenanceRequest) {

		CarMaintenance carMaintenance = this.modelMapperService.forRequest().map(deleteCarMaintenanceRequest,
				CarMaintenance.class);
		this.carMaintenanceDao.delete(carMaintenance);
		return new SuccessResult(Messages.CAR_MAINTENANCE_DELETE);
	}

	@Override
	public Result isCarInMaintenance(int carId) {

		if (this.carMaintenanceDao.getCarMaintenanceById(carId) != null)
			throw new BusinessException(Messages.CAR_MAINTENANCE_CAR_IN_RENT );

		else
			return new SuccessResult(Messages.SUCCESS);
	}

}
