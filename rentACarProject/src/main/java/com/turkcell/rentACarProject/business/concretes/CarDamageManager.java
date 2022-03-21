package com.turkcell.rentACarProject.business.concretes;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.turkcell.rentACarProject.business.abstracts.CarDamageService;
import com.turkcell.rentACarProject.business.dtos.carDamage.ListCarDamageDto;
import com.turkcell.rentACarProject.business.requests.carDamage.CreateCarDamageRequest;
import com.turkcell.rentACarProject.business.requests.carDamage.UpdateCarDamageRequest;
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
	
	@Autowired
	public CarDamageManager(CarDamageDao carDamageDao, ModelMapperService modelMapperService) {
		this.carDamageDao = carDamageDao;
		this.modelMapperService = modelMapperService;
	}
	
	@Override
	public DataResult<List<ListCarDamageDto>> getAll() {
		
		var result = this.carDamageDao.findAll();
		List<ListCarDamageDto> response = result.stream()
				.map(carDamage -> this.modelMapperService.forDto().map(carDamage, ListCarDamageDto.class))
				.collect(Collectors.toList());
		
		return new SuccessDataResult<List<ListCarDamageDto>>(response);
	}

	@Override
	public DataResult<List<ListCarDamageDto>> getAllByCarId(int id) {
		
		List<CarDamage> carDamageList = this.carDamageDao.getAllByCarId(id);
		
        List<ListCarDamageDto> response = carDamageList.stream()
                .map(carDamage -> modelMapperService.forDto().map(carDamage, ListCarDamageDto.class))
                .collect(Collectors.toList());

        return new SuccessDataResult<List<ListCarDamageDto>>(response);
	}

	@Override
	public Result create(CreateCarDamageRequest createCarDamageRequest) {		
	
	CarDamage carDamage = this.modelMapperService.forRequest().map(createCarDamageRequest, CarDamage.class);
	this.carDamageDao.save(carDamage);
	return new SuccessResult("Created damage information of " + createCarDamageRequest.getDescription() + " car.");
	}

	@Override
	public Result update(UpdateCarDamageRequest updateCarDamageRequest) {
		
		CarDamage carDamage = this.modelMapperService.forRequest().map(updateCarDamageRequest, CarDamage.class);
		this.carDamageDao.save(carDamage);
		return new SuccessResult("Updated maintenance information of " + updateCarDamageRequest.getDescription() + " car.");
	}

	@Override
	public Result delete(int id) {
        	carDamageDao.deleteById(id);
            return new SuccessResult("CarMaintenance.Deleted");
	}

}
