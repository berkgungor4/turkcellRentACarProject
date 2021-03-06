package com.turkcell.rentACarProject.business.requests.car;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCarRequest {

	private int id;
	private double dailyPrice;
	private String description;
	private int mileage;
}
