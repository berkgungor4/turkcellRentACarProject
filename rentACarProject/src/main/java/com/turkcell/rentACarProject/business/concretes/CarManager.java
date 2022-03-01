package com.turkcell.rentACarProject.business.concretes;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.turkcell.rentACarProject.business.abstracts.CarService;
import com.turkcell.rentACarProject.business.dtos.GetCarDto;
import com.turkcell.rentACarProject.business.dtos.ListCarDto;
import com.turkcell.rentACarProject.business.requests.car.CreateCarRequest;
import com.turkcell.rentACarProject.business.requests.car.DeleteCarRequest;
import com.turkcell.rentACarProject.business.requests.car.UpdateCarRequest;
import com.turkcell.rentACarProject.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACarProject.core.utilities.result.DataResult;
import com.turkcell.rentACarProject.core.utilities.result.Result;
import com.turkcell.rentACarProject.core.utilities.result.SuccessDataResult;
import com.turkcell.rentACarProject.core.utilities.result.SuccessResult;
import com.turkcell.rentACarProject.dataAccess.abstracts.CarDao;
import com.turkcell.rentACarProject.entities.concretes.Car;

@Service
public class CarManager implements CarService {
	
	private CarDao carDao;
	private ModelMapperService modelMapperService;

	@Autowired
	public CarManager(CarDao carDao, ModelMapperService modelMapperService) {
		this.carDao = carDao;
		this.modelMapperService = modelMapperService;
	}

	@Override
	public DataResult<List<ListCarDto>> getAll() {
		var result = this.carDao.findAll();
		List<ListCarDto> response = result.stream()
				.map(car -> this.modelMapperService.forDto().map(car, ListCarDto.class))
				.collect(Collectors.toList());
		return new SuccessDataResult<List<ListCarDto>>(response);
	}
	
	@Override
	public DataResult<GetCarDto> getById(int id) {
		Car result = this.carDao.getCarById(id);
		GetCarDto response = this.modelMapperService.forDto().map(result, GetCarDto.class);
		return new SuccessDataResult<GetCarDto>(response);
	}
	
	@Override
	public Result create(CreateCarRequest createCarRequest) {
		Car car = this.modelMapperService.forRequest().map(createCarRequest, Car.class);
		this.carDao.save(car);
		return new SuccessResult("Car.Created");
	}

	@Override
	public Result delete(DeleteCarRequest deleteCarRequest) {
		Car car = this.modelMapperService.forRequest().map(deleteCarRequest, Car.class);
		this.carDao.delete(car);
		return new SuccessResult("Car.Deleted");
	}

	@Override
	public Result update(UpdateCarRequest updateCarRequest) {
		Car car = this.modelMapperService.forRequest().map(updateCarRequest, Car.class);
		this.carDao.save(car);
		return new SuccessResult("Car.Updated");
	}

	@Override
	public DataResult<List<ListCarDto>> getAllSorted(Direction direction) {
		Sort sort = Sort.by(direction, "dailyPrice");
		
		List<Car> result = this.carDao.findAll(sort);
		List<ListCarDto> response = result.stream()
				.map(car->this.modelMapperService.forDto().map(car, ListCarDto.class))
				.collect(Collectors.toList());
		
		return new SuccessDataResult<List<ListCarDto>>(response);
	}

	@Override
	public DataResult<List<ListCarDto>> getAllPaged(int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of(pageNo-1, pageSize);
		
		List<Car> result = this.carDao.findAll(pageable).getContent();
		
		List<ListCarDto> response = result.stream()
				.map(car->this.modelMapperService.forDto().map(car, ListCarDto.class))
				.collect(Collectors.toList());
		
		return new SuccessDataResult<List<ListCarDto>>(response);
	}

	@Override
	public DataResult<List<ListCarDto>> getByDailyPriceLessThanEqual(double dailyPrice) {
		var result = this.carDao.getByDailyPriceLessThanEqual(dailyPrice);
		
		List<ListCarDto> response = result.stream()
				.map(car->this.modelMapperService.forDto().map(car, ListCarDto.class))
				.collect(Collectors.toList());
		
		return new SuccessDataResult<List<ListCarDto>>(response, "Data.Listed");
	}

}
