package com.turkcell.rentACarProject.business.abstracts;

import java.util.List;

import com.turkcell.rentACarProject.business.dtos.orderedAdditionalService.ListOrderedAdditionalServiceDto;
import com.turkcell.rentACarProject.business.requests.orderedAdditionalService.CreateOrderedAdditionalServiceRequest;
import com.turkcell.rentACarProject.business.requests.orderedAdditionalService.DeleteOrderedAdditionalServiceRequest;
import com.turkcell.rentACarProject.business.requests.orderedAdditionalService.UpdateOrderedAdditionalServiceRequest;
import com.turkcell.rentACarProject.core.utilities.results.DataResult;
import com.turkcell.rentACarProject.core.utilities.results.Result;

public interface OrderedAdditionalServiceService {

	DataResult<List<ListOrderedAdditionalServiceDto>> getAll();
	DataResult<ListOrderedAdditionalServiceDto> getById(int id);
	DataResult<List<ListOrderedAdditionalServiceDto>> findAllByRentalId(int rentalId);
	
	Result create(CreateOrderedAdditionalServiceRequest createOrderedAdditionalServiceRequest);
	Result update(UpdateOrderedAdditionalServiceRequest updateOrderedAdditionalServiceRequest);
	Result delete(DeleteOrderedAdditionalServiceRequest deleteOrderedAdditionalServiceRequest);
}
