package com.turkcell.rentACarProject.business.requests.rental;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateRentalRequest {

	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate rentDate;
	
    private int customerId;
    private int carId;
    private int initialCityId;
}