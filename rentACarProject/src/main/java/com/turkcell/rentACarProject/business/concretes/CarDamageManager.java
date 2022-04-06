package com.turkcell.rentACarProject.business.concretes;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.turkcell.rentACarProject.business.abstracts.CarDamageService;
import com.turkcell.rentACarProject.business.abstracts.CarService;
import com.turkcell.rentACarProject.business.constants.Messages;
import com.turkcell.rentACarProject.business.dtos.carDamage.ListCarDamageDto;
import com.turkcell.rentACarProject.business.requests.carDamage.CreateCarDamageRequest;
import com.turkcell.rentACarProject.business.requests.carDamage.DeleteCarDamageRequest;
import com.turkcell.rentACarProject.business.requests.carDamage.UpdateCarDamageRequest;
import com.turkcell.rentACarProject.core.exceptions.BusinessException;
import com.turkcell.rentACarProject.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACarProject.core.utilities.results.DataResult;
import com.turkcell.rentACarProject.core.utilities.results.Result;
import com.turkcell.rentACarProject.core.utilities.results.SuccessDataResult;
import com.turkcell.rentACarProject.core.utilities.results.SuccessResult;
import com.turkcell.rentACarProject.dataAccess.abstracts.CarDamageDao;
import com.turkcell.rentACarProject.entities.concretes.CarDamage;

@Service
public class CarDamageManager implements CarDamageService {

	private CarDamageDao carDamageDao;
	private ModelMapperService modelMapperService;
	private CarService carService;

	@Autowired
	public CarDamageManager(CarDamageDao carDamageDao, ModelMapperService modelMapperService, CarService carService) {
		this.carDamageDao = carDamageDao;
		this.modelMapperService = modelMapperService;
		this.carService = carService;
	}

	@Override
	public DataResult<List<ListCarDamageDto>> getAll() {

		var result = this.carDamageDao.findAll();
		
		List<ListCarDamageDto> response = result.stream().map(carDamage ->this.modelMapperService.forDto()
				.map(carDamage, ListCarDamageDto.class)).collect(Collectors.toList());
		
		return new SuccessDataResult<List<ListCarDamageDto>>(response, Messages.SUCCESS);
	}

	@Override
	public DataResult<ListCarDamageDto> getById(int id) {

		checkIfCarDamageExists(id);
		
		CarDamage result = this.carDamageDao.getCarDamageById(id);
		ListCarDamageDto response = this.modelMapperService.forDto().map(result, ListCarDamageDto.class);
		
		return new SuccessDataResult<ListCarDamageDto>(response, Messages.SUCCESS);
	}

	@Override
	public DataResult<List<ListCarDamageDto>> getByCarId(int carId) {

		checkIfCarExists(carId);
		
		List<CarDamage> result = this.carDamageDao.getByCar_id(carId);
		
		List<ListCarDamageDto> response = result.stream().map(carDamage ->this.modelMapperService.forDto()
				.map(carDamage, ListCarDamageDto.class)).collect(Collectors.toList());

		return new SuccessDataResult<List<ListCarDamageDto>>(response, Messages.SUCCESS);
	}

	@Override
	public Result create(CreateCarDamageRequest createCarDamageRequest) {

		checkIfCarExists(createCarDamageRequest.getCarId());
		
		CarDamage carDamage = this.modelMapperService.forRequest().map(createCarDamageRequest, CarDamage.class);
		
		carDamage.setId(0);
		
		this.carDamageDao.save(carDamage);
		
		return new SuccessResult(Messages.CAR_DAMAGE_ADD);
	}

	@Override
	public Result update(UpdateCarDamageRequest updateCarDamageRequest) {

		checkIfCarDamageExists(updateCarDamageRequest.getId());
		
		CarDamage carDamage = this.modelMapperService.forRequest().map(updateCarDamageRequest, CarDamage.class);
		this.carDamageDao.save(carDamage);
		
		return new SuccessResult(Messages.CAR_DAMAGE_UPDATE);
	}

	@Override
	public Result delete(DeleteCarDamageRequest deleteCarDamageRequest) {
		
		checkIfCarDamageExists(deleteCarDamageRequest.getId());

		this.carDamageDao.deleteById(deleteCarDamageRequest.getId());
		
		return new SuccessResult(Messages.CAR_DAMAGE_DELETE);
	}
	
	private void checkIfCarDamageExists(int id) {
		
		if(!this.carDamageDao.existsById(id)) {
			
			throw new BusinessException(Messages.CAR_DAMAGE_NOT_FOUND);
		}
	}
	
	private void checkIfCarExists(int carId) {
		
		if(!this.carService.getById(carId).isSuccess()) {
			
			throw new BusinessException(Messages.CAR_NOT_FOUND);
		}		
	}
	
}
