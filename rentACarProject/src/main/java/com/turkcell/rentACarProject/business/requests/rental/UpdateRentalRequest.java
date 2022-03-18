package com.turkcell.rentACarProject.business.requests.rental;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class UpdateRentalRequest {

	private int id;
    private int carId;
    private double additionalPrice;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate rentDate;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate returnDate;
    
    private int initialCityId;
    private int returnCityId;
    private int customerId;
    private int initialMileage;
    private int returnMileage;

}

