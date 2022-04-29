package com.turkcell.rentACarProject.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.turkcell.rentACarProject.business.abstracts.CorporateCustomerService;
import com.turkcell.rentACarProject.business.dtos.corporateCustomer.ListCorporateCustomerDto;
import com.turkcell.rentACarProject.business.requests.corporateCustomer.CreateCorporateCustomerRequest;
import com.turkcell.rentACarProject.business.requests.corporateCustomer.DeleteCorporateCustomerRequest;
import com.turkcell.rentACarProject.business.requests.corporateCustomer.UpdateCorporateCustomerRequest;
import com.turkcell.rentACarProject.core.utilities.results.DataResult;
import com.turkcell.rentACarProject.core.utilities.results.Result;

@RestController
@RequestMapping("/api/corporateCustomers")
public class CorporateCustomerController {

	private CorporateCustomerService corporateCustomerService;

	@Autowired
	public CorporateCustomerController(CorporateCustomerService corporateCustomerService) {
		this.corporateCustomerService = corporateCustomerService;
	}

	@GetMapping("/getAll")
	DataResult<List<ListCorporateCustomerDto>> getAll() {
		return this.corporateCustomerService.getAll();
	}

	@GetMapping("/getById")
	DataResult<ListCorporateCustomerDto> getById(@RequestParam int id) {
		return this.corporateCustomerService.getById(id);
	}

	@PostMapping("/create")
	Result create(@RequestBody CreateCorporateCustomerRequest createCorporateCustomerRequest) {
		return this.corporateCustomerService.create(createCorporateCustomerRequest);
	}

	@PutMapping("/update")
	Result update(@RequestBody UpdateCorporateCustomerRequest updateCorporateCustomerRequest) {
		return this.corporateCustomerService.update(updateCorporateCustomerRequest);
	}

	@DeleteMapping("/delete")
	Result delete(@RequestBody DeleteCorporateCustomerRequest deleteCorporateCustomerRequest) {
		return this.corporateCustomerService.delete(deleteCorporateCustomerRequest);
	}

}
