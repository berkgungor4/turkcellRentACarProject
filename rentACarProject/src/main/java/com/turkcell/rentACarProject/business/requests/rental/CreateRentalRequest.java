package com.turkcell.rentACarProject.business.requests.rental;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateRentalRequest {
	
	private int carId;
	private int customerId;
	private LocalDate rentDate;
}
