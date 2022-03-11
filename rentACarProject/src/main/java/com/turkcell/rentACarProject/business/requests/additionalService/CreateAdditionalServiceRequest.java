package com.turkcell.rentACarProject.business.requests.additonalService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAdditionalServiceRequest {
	
	private String name;
	private double dailyPrice;
}
