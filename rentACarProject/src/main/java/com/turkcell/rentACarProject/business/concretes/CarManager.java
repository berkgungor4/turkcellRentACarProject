package com.turkcell.rentACarProject.business.concretes;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.turkcell.rentACarProject.business.abstracts.CarService;
import com.turkcell.rentACarProject.business.constants.Messages;
import com.turkcell.rentACarProject.business.dtos.car.ListCarDto;
import com.turkcell.rentACarProject.business.requests.car.CreateCarRequest;
import com.turkcell.rentACarProject.business.requests.car.DeleteCarRequest;
import com.turkcell.rentACarProject.business.requests.car.UpdateCarRequest;
import com.turkcell.rentACarProject.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACarProject.core.utilities.results.DataResult;
import com.turkcell.rentACarProject.core.utilities.results.ErrorDataResult;
import com.turkcell.rentACarProject.core.utilities.results.Result;
import com.turkcell.rentACarProject.core.utilities.results.SuccessDataResult;
import com.turkcell.rentACarProject.core.utilities.results.SuccessResult;
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
		
		List<Car> result = this.carDao.findAll();
		
		List<ListCarDto> response = result.stream()
				.map(car -> this.modelMapperService.forDto().map(car, ListCarDto.class)).collect(Collectors.toList());

		return new SuccessDataResult<List<ListCarDto>>(response, Messages.SUCCESS);
	}

	@Override
	public Result create(CreateCarRequest createCarRequest) {
		
		Car car = this.modelMapperService.forRequest().map(createCarRequest, Car.class);
		this.carDao.save(car);
		
		return new SuccessResult(Messages.CAR_ADD);
	}

	@Override
	public Result delete(DeleteCarRequest deleteCarRequest) {
		
		Car car = this.modelMapperService.forRequest().map(deleteCarRequest, Car.class);
		this.carDao.delete(car);
		
		return new SuccessResult(Messages.CAR_DELETE);
	}

	@Override
	public Result update(UpdateCarRequest updateCarRequest) {
		
		Car car = this.modelMapperService.forRequest().map(updateCarRequest, Car.class);
		this.carDao.save(car);
		
		return new SuccessResult(Messages.CAR_UPDATE);
	}

	@Override
	public DataResult<ListCarDto> getById(int id) {

		Car result = this.carDao.getCarById(id);

		if (result == null) {
			return new ErrorDataResult<ListCarDto>(Messages.CAR_NOT_FOUND);
		}
		ListCarDto response = this.modelMapperService.forDto().map(result, ListCarDto.class);

		return new SuccessDataResult<ListCarDto>(response, Messages.SUCCESS);
	}

	@Override
	public DataResult<List<ListCarDto>> getAllPaged(int pageNo, int pageSize) {

		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

		List<Car> result = this.carDao.findAll(pageable).getContent();
		
		List<ListCarDto> response = result.stream()
				.map(car -> this.modelMapperService.forDto().map(car, ListCarDto.class)).collect(Collectors.toList());

		return new SuccessDataResult<List<ListCarDto>>(response, Messages.SUCCESS);
	}

	@Override
	public DataResult<List<ListCarDto>> getAllByDailyPriceLessThanEqual(double dailyPrice) {

		List<Car> result = this.carDao.getByDailyPriceLessThanEqual(dailyPrice);

		List<ListCarDto> response = result.stream()
				.map(car -> this.modelMapperService.forDto().map(car, ListCarDto.class)).collect(Collectors.toList());

		return new SuccessDataResult<List<ListCarDto>>(response, Messages.SUCCESS);
	}

	@Override
	public DataResult<List<ListCarDto>> getAllSorted(Sort.Direction direction) {

		Sort sort = Sort.by(direction, "dailyPrice");

		List<Car> result = this.carDao.findAll(sort);

		List<ListCarDto> response = result.stream()
				.map(car -> this.modelMapperService.forDto().map(car, ListCarDto.class)).collect(Collectors.toList());

		return new SuccessDataResult<>(response, Messages.SUCCESS);
	}

}
