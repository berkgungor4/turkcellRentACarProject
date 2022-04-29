package com.turkcell.rentACarProject.business.concretes;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.turkcell.rentACarProject.business.abstracts.CarMaintenanceService;
import com.turkcell.rentACarProject.business.abstracts.CarService;
import com.turkcell.rentACarProject.business.abstracts.CustomerService;
import com.turkcell.rentACarProject.business.abstracts.PaymentService;
import com.turkcell.rentACarProject.business.abstracts.RentalService;
import com.turkcell.rentACarProject.business.constants.Messages;
import com.turkcell.rentACarProject.business.dtos.car.ListCarDto;
import com.turkcell.rentACarProject.business.dtos.customer.ListCustomerDto;
import com.turkcell.rentACarProject.business.dtos.rental.ListRentalDto;
import com.turkcell.rentACarProject.business.requests.creditCard.CreateCreditCardRequest;
import com.turkcell.rentACarProject.business.requests.lateDelivery.CreateLateDeliveryRequest;
import com.turkcell.rentACarProject.business.requests.lateDelivery.UpdateLateDeliveryRequest;
import com.turkcell.rentACarProject.business.requests.rental.CreateRentalRequest;
import com.turkcell.rentACarProject.business.requests.rental.DeleteRentalRequest;
import com.turkcell.rentACarProject.business.requests.rental.UpdateRentalRequest;
import com.turkcell.rentACarProject.core.exceptions.BusinessException;
import com.turkcell.rentACarProject.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACarProject.core.utilities.results.DataResult;
import com.turkcell.rentACarProject.core.utilities.results.Result;
import com.turkcell.rentACarProject.core.utilities.results.SuccessDataResult;
import com.turkcell.rentACarProject.core.utilities.results.SuccessResult;
import com.turkcell.rentACarProject.dataAccess.abstracts.RentalDao;
import com.turkcell.rentACarProject.entities.concretes.Customer;
import com.turkcell.rentACarProject.entities.concretes.Rental;

@Service
public class RentalManager implements RentalService {

	private RentalDao rentalDao;
	private ModelMapperService modelMapperService;
	private CarService carService;
	private PaymentService paymentService;
	private CustomerService customerService;
	private CarMaintenanceService carMaintenanceService;

	@Autowired
	@Lazy
	public RentalManager(RentalDao rentalDao, ModelMapperService modelMapperService, CarService carService,
			PaymentService paymentService, CustomerService customerService,
			CarMaintenanceService carMaintenanceService) {

		this.rentalDao = rentalDao;
		this.modelMapperService = modelMapperService;
		this.carService = carService;
		this.paymentService = paymentService;
		this.customerService = customerService;
		this.carMaintenanceService = carMaintenanceService;
	}

	@Override
	public DataResult<List<ListRentalDto>> getAll() {

		List<Rental> result = this.rentalDao.findAll();
		
		List<ListRentalDto> response = result.stream()
				.map(rental -> this.modelMapperService.forDto().map(rental, ListRentalDto.class))
				.collect(Collectors.toList());

		return new SuccessDataResult<List<ListRentalDto>>(response, Messages.SUCCESS);
	}

	@Override
	public DataResult<ListRentalDto> getById(int id) {

		Rental result = checkIfRentalExists(id);

		ListRentalDto response = this.modelMapperService.forRequest().map(result, ListRentalDto.class);

		response.setTotalPrice(result.getTotalPrice());

		return new SuccessDataResult<ListRentalDto>(response, Messages.SUCCESS);
	}

	@Override
	public DataResult<List<ListRentalDto>> getRentalByCar(int carId) {

		List<Rental> result = this.rentalDao.getByCar_id(carId);
		
		List<ListRentalDto> response = result.stream()
				.map(rental -> this.modelMapperService.forDto().map(rental, ListRentalDto.class))
				.collect(Collectors.toList());

		return new SuccessDataResult<List<ListRentalDto>>(response, Messages.SUCCESS);
	}

	@Transactional
	@Override
	public Result lateDelivery(int id, UpdateLateDeliveryRequest updateLateDeliveryRequest,
			UpdateRentalRequest updateRentalRequest) {

		checkIfRentalExists(id);

		Rental rental = this.rentalDao.getById(id);

		checkIfDates(rental, updateLateDeliveryRequest.getUpdateRentalRequest());

		CreateLateDeliveryRequest createLateDeliveryRequest = createPayment(id);

		createLateDeliveryRequest.setCreateCreditCardRequest(createCreditCard(updateLateDeliveryRequest));

		updateTotalPrice(rental, updateLateDeliveryRequest.getUpdateRentalRequest());

		rental.getCar().setMileage(updateLateDeliveryRequest.getUpdateRentalRequest().getReturnMileage());

		rental.setReturnDate(updateRentalRequest.getReturnDate());
		rental.setReturnMileage(updateRentalRequest.getReturnMileage());

		rentalDao.save(rental);

		this.paymentService.createForLateDelivery(createLateDeliveryRequest);

		return new SuccessResult(Messages.RENTAL_UPDATE);
	}

	@Override
	public Rental createForCustomer(CreateRentalRequest createRentalRequest) {

		carMaintenanceService.isCarInMaintenance(createRentalRequest.getCarId());

		Rental rental = this.modelMapperService.forRequest().map(createRentalRequest, Rental.class);

		rental.setId(0);

		rental.setTotalPrice(rentalCalculation(rental));

		rental.setCustomer(customerCorrectionAndCheckIfCustomerExists(createRentalRequest.getCustomerId()));

		rental.setInitialMileage(this.carService.getById(createRentalRequest.getCarId()).getData().getMileage());

		this.rentalDao.save(rental);

		return rental;
	}

	@Override
	public Result delete(DeleteRentalRequest deleteRentalRequest) {

		checkIfRentalExists(deleteRentalRequest.getId());

		Rental rental = this.modelMapperService.forRequest().map(deleteRentalRequest, Rental.class);
		this.rentalDao.delete(rental);
		
		return new SuccessResult(Messages.RENTAL_DELETE);
	}

	@Override
	public Result update(UpdateRentalRequest updateRentalRequest) {

		checkIfRentalExists(updateRentalRequest.getId());

		Rental rental = this.rentalDao.getById(updateRentalRequest.getId());

		checkIfDates(rental, updateRentalRequest);

		updateTotalPrice(rental, updateRentalRequest);

		rental.getCar().setMileage(updateRentalRequest.getReturnMileage());

		rental.setReturnDate(updateRentalRequest.getReturnDate());
		rental.setReturnMileage(updateRentalRequest.getReturnMileage());

		rentalDao.save(rental);

		return new SuccessResult(Messages.RENTAL_UPDATE);
	}

	@Override
	public Result isCarRented(int carId) {

		if (this.rentalDao.getRentalById(carId) != null) {
			
			throw new BusinessException(Messages.RENTAL_IN_MAINTENANCE);
		} else
			
			return new SuccessResult(Messages.SUCCESS);
	}

	private Rental checkIfRentalExists(int id) {

		Rental rental = this.rentalDao.getById(id);
		if (rental == null) {
			
			throw new BusinessException(Messages.RENTAL_NOT_FOUND);
		}
		
		return rental;
	}

	private double rentalCalculation(Rental rental) {

		ListCarDto car = this.carService.getById(rental.getCar().getId()).getData();

		long dateBetween = ChronoUnit.DAYS.between(rental.getRentDate(), rental.getReturnDate());
		if (dateBetween == 0) {
			dateBetween = 1;
		}

		double rentPrice = car.getDailyPrice();
		double totalPrice = rentPrice * dateBetween;

		if (rental.getInitialCity().getId() != rental.getReturnCity().getId()) {
			totalPrice = totalPrice + 750;
		}

		return totalPrice;
	}

	private void updateTotalPrice(Rental rental, UpdateRentalRequest updateRentalRequest) {

		double totalPrice = 0;

		long dateBetween = ChronoUnit.DAYS.between(rental.getReturnDate(), updateRentalRequest.getReturnDate());

		ListCarDto car = this.carService.getById(rental.getCar().getId()).getData();

		double rentPrice = car.getDailyPrice();

		if (rental.getInitialCity().getId() != updateRentalRequest.getReturnCityId()) {
			totalPrice = totalPrice + 750;
		}

		totalPrice = rentPrice * dateBetween;

		rental.setTotalPrice(totalPrice);
	}

	private CreateLateDeliveryRequest createPayment(int id) {

		CreateLateDeliveryRequest createLateDeliveryRequest = new CreateLateDeliveryRequest();

		createLateDeliveryRequest.setRentalId(id);

		return createLateDeliveryRequest;
	}

	private void checkIfDates(Rental rental, UpdateRentalRequest updateRentalRequest) {

		if (rental.getReturnDate().isAfter(updateRentalRequest.getReturnDate())) {
			
			throw new BusinessException(Messages.RENTAL_RETURN_DATE_ERROR);
		}
	}

	private CreateCreditCardRequest createCreditCard(UpdateLateDeliveryRequest updateLateDeliveryRequest) {

		CreateCreditCardRequest createCreditCardRequest = new CreateCreditCardRequest();
		createCreditCardRequest
				.setCardOwnerName(updateLateDeliveryRequest.getCreateCreditCardRequest().getCardOwnerName());
		createCreditCardRequest
				.setCardCvvNumber(updateLateDeliveryRequest.getCreateCreditCardRequest().getCardCvvNumber());
		createCreditCardRequest.setCardNumber(updateLateDeliveryRequest.getCreateCreditCardRequest().getCardNumber());

		return createCreditCardRequest;
	}

	private Customer customerCorrectionAndCheckIfCustomerExists(int customerId) {

		ListCustomerDto listCustomerDto = this.customerService.getById(customerId).getData();
		
		Customer customer = this.modelMapperService.forDto().map(listCustomerDto, Customer.class);

		return customer;
	}

}
