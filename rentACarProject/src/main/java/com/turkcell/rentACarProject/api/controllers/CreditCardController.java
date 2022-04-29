package com.turkcell.rentACarProject.api.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkcell.rentACarProject.business.abstracts.CreditCardService;
import com.turkcell.rentACarProject.business.requests.creditCard.CreateCreditCardRequest;
import com.turkcell.rentACarProject.business.requests.creditCard.DeleteCreditCardRequest;
import com.turkcell.rentACarProject.business.requests.creditCard.UpdateCreditCardRequest;
import com.turkcell.rentACarProject.core.utilities.results.Result;

@RestController
@RequestMapping("/api/creditCards")
public class CreditCardController {

	private CreditCardService creditCardService;

	@Autowired
	public CreditCardController(CreditCardService creditCardService) {

		this.creditCardService = creditCardService;
	}
	
	@PostMapping("/create")
	public Result create(@RequestBody @Valid CreateCreditCardRequest createCreditCardRequest) {
		
		return this.creditCardService.create(createCreditCardRequest);
	}
	
	@PutMapping("/update")
	public Result update(@RequestBody @Valid UpdateCreditCardRequest updateCreditCardRequest) {
		
		return this.creditCardService.update(updateCreditCardRequest);
	}
	
	@DeleteMapping("/delete")
	public Result delete(@RequestBody @Valid DeleteCreditCardRequest deleteCreditCardRequest) {
		
		return this.creditCardService.delete(deleteCreditCardRequest);
	}
	
}
