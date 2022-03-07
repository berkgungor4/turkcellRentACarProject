package com.turkcell.rentACarProject.business.dtos.rental;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetRentalDto {
	
	private int id;
	private int carId;
	private int customerId;
	private LocalDate rentDate;
	private LocalDate returnDate;
}
