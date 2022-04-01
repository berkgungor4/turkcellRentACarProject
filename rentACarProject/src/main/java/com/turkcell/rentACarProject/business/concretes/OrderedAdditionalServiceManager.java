package com.turkcell.rentACarProject.business.concretes;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.turkcell.rentACarProject.business.abstracts.AdditionalServiceService;
import com.turkcell.rentACarProject.business.abstracts.OrderedAdditionalServiceService;
import com.turkcell.rentACarProject.business.abstracts.RentalService;
import com.turkcell.rentACarProject.business.dtos.additionalService.ListAdditionalServiceDto;
import com.turkcell.rentACarProject.business.dtos.orderedAdditionalService.ListOrderedAdditionalServiceDto;
import com.turkcell.rentACarProject.business.dtos.rental.ListRentalDto;
import com.turkcell.rentACarProject.business.requests.orderedAdditionalService.CreateOrderedAdditionalServiceRequest;
import com.turkcell.rentACarProject.business.requests.orderedAdditionalService.DeleteOrderedAdditionalServiceRequest;
import com.turkcell.rentACarProject.business.requests.orderedAdditionalService.UpdateOrderedAdditionalServiceRequest;
import com.turkcell.rentACarProject.core.exceptions.BusinessException;
import com.turkcell.rentACarProject.core.utilities.mapping.ModelMapperService;
import com.turkcell.rentACarProject.core.utilities.results.DataResult;
import com.turkcell.rentACarProject.core.utilities.results.Result;
import com.turkcell.rentACarProject.core.utilities.results.SuccessDataResult;
import com.turkcell.rentACarProject.core.utilities.results.SuccessResult;
import com.turkcell.rentACarProject.dataAccess.abstracts.OrderedAdditionalServiceDao;
import com.turkcell.rentACarProject.entities.concretes.AdditionalService;
import com.turkcell.rentACarProject.entities.concretes.OrderedAdditionalService;
import com.turkcell.rentACarProject.entities.concretes.Rental;

@Service
public class OrderedAdditionalServiceManager implements OrderedAdditionalServiceService {

	private OrderedAdditionalServiceDao orderedAdditionalServiceDao;
	private ModelMapperService modelMapperService;
	private RentalService rentalService;
	private AdditionalServiceService additionalServiceService;

	@Autowired
	public OrderedAdditionalServiceManager(OrderedAdditionalServiceDao orderedAdditionalServiceDao,
			ModelMapperService modelMapperService, RentalService rentalService,
			AdditionalServiceService additionalServiceService) {
		super();
		this.orderedAdditionalServiceDao = orderedAdditionalServiceDao;
		this.modelMapperService = modelMapperService;
		this.rentalService = rentalService;
		this.additionalServiceService = additionalServiceService;
	}

	@Override
	public DataResult<List<ListOrderedAdditionalServiceDto>> getAll() {

		List<OrderedAdditionalService> result = this.orderedAdditionalServiceDao.findAll();
		List<ListOrderedAdditionalServiceDto> response = result.stream()
				.map(orderedAdditionalService -> this.modelMapperService.forDto().map(orderedAdditionalService,
						ListOrderedAdditionalServiceDto.class))
				.collect(Collectors.toList());

		return new SuccessDataResult<List<ListOrderedAdditionalServiceDto>>(response, "BusinessMessages.SUCCESS");
	}

	@Override
	public DataResult<ListOrderedAdditionalServiceDto> getById(int id) {

		OrderedAdditionalService result = checkIfOrderedAdditionalServiceExists(id);

		ListOrderedAdditionalServiceDto response = this.modelMapperService.forDto().map(result,
				ListOrderedAdditionalServiceDto.class);

		return new SuccessDataResult<ListOrderedAdditionalServiceDto>(response, "BusinessMessages.SUCCESS");
	}

	@Override
	public DataResult<List<ListOrderedAdditionalServiceDto>> findAllByRentalId(int rentalId) {

		List<OrderedAdditionalService> result = this.orderedAdditionalServiceDao.getByRental_rentalId(rentalId);
		List<ListOrderedAdditionalServiceDto> response = result.stream()
				.map(orderedAdditionalService -> this.modelMapperService.forDto().map(orderedAdditionalService,
						ListOrderedAdditionalServiceDto.class))
				.collect(Collectors.toList());

		return new SuccessDataResult<List<ListOrderedAdditionalServiceDto>>(response, "BusinessMessages.SUCCESS");
	}

	@Override
	public Result create(CreateOrderedAdditionalServiceRequest createOrderedAdditionalServiceRequest) {

		OrderedAdditionalService orderedAdditionalService = this.modelMapperService.forRequest()
				.map(createOrderedAdditionalServiceRequest, OrderedAdditionalService.class);

		orderedAdditionalService.setId(0);

		checkIfAdditionalServiceExists(createOrderedAdditionalServiceRequest.getAdditionalServiceId());

		this.orderedAdditionalServiceDao.save(orderedAdditionalService);

		return new SuccessResult("BusinessMessages.ORDEREDADDITIONALSERVICEADDED");
	}

	@Override
	public Result update(UpdateOrderedAdditionalServiceRequest updateOrderedAdditionalServiceRequest) {

		checkIfOrderedAdditionalServiceExists(updateOrderedAdditionalServiceRequest.getId());

		checkIfRentalExists(updateOrderedAdditionalServiceRequest.getRentalId());

		checkIfAdditionalServiceExists(updateOrderedAdditionalServiceRequest.getAdditionalServiceId());

		OrderedAdditionalService orderedadditionalService = this.orderedAdditionalServiceDao
				.getByOrderedAdditionalServiceId(updateOrderedAdditionalServiceRequest.getId());

		this.orderedAdditionalServiceDao.save(orderedadditionalService);

		return new SuccessResult("BusinessMessages.ORDEREDADDITIONALSERVICEUPDATED");
	}

	@Override
	public Result delete(DeleteOrderedAdditionalServiceRequest deleteOrderedAdditionalService) {

		checkIfOrderedAdditionalServiceExists(deleteOrderedAdditionalService.getId());

		this.orderedAdditionalServiceDao.deleteById(deleteOrderedAdditionalService.getId());

		return new SuccessResult("BusinessMessages.ORDEREDADDITIONALSERVICEDELETED");
	}

	private Rental checkIfRentalExists(int id) {

		ListRentalDto listRentalDto = this.rentalService.getById(id).getData();

		if (listRentalDto == null) {
			throw new BusinessException("BusinessMessages.RENTALCARNOTFOUND");
		}
		Rental rental = this.modelMapperService.forDto().map(listRentalDto, Rental.class);
		return rental;
	}

	private AdditionalService checkIfAdditionalServiceExists(int id) {

		ListAdditionalServiceDto listAdditionalServiceDto = this.additionalServiceService.getById(id).getData();

		if (listAdditionalServiceDto == null) {
			throw new BusinessException("BusinessMessages.ADDITIONALSERVICENOTFOUND");
		}
		AdditionalService additionalService = this.modelMapperService.forDto().map(listAdditionalServiceDto,
				AdditionalService.class);
		return additionalService;
	}

	private OrderedAdditionalService checkIfOrderedAdditionalServiceExists(int id) {

		OrderedAdditionalService orderedAdditionalService = this.orderedAdditionalServiceDao
				.getByOrderedAdditionalServiceId(id);

		if (orderedAdditionalService == null) {
			throw new BusinessException("BusinessMessages.ORDEREDADDITIONALSERVICENOTFOUND");
		}
		return orderedAdditionalService;
	}

}
