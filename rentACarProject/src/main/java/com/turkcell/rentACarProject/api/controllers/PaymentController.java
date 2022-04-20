package com.turkcell.rentACarProject.api.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.turkcell.rentACarProject.business.abstracts.PaymentService;
import com.turkcell.rentACarProject.business.dtos.payment.ListPaymentDto;
import com.turkcell.rentACarProject.business.requests.payment.CreatePaymentRequest;
import com.turkcell.rentACarProject.business.requests.payment.DeletePaymentRequest;
import com.turkcell.rentACarProject.core.utilities.results.DataResult;
import com.turkcell.rentACarProject.core.utilities.results.Result;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

	private PaymentService paymentService;

	@Autowired
	public PaymentController(PaymentService paymentService) {
		this.paymentService = paymentService;
	}
	
	@GetMapping("/getAll")
	DataResult<List<ListPaymentDto>> getAll(){
		return this.paymentService.getAll();
	}
	
	@GetMapping("/getById")
	DataResult<ListPaymentDto> getById(@RequestParam int id){
		return this.paymentService.getById(id);
	}
	
	@PostMapping("/create")
	Result create(boolean registered, @RequestBody @Valid CreatePaymentRequest createPaymentRequest) {
		return this.paymentService.create(registered, createPaymentRequest);
	}
	
	@DeleteMapping("/delete")
	Result delete(@RequestBody DeletePaymentRequest deletePaymentRequest){
		return this.paymentService.delete(deletePaymentRequest);
	}
	
}
