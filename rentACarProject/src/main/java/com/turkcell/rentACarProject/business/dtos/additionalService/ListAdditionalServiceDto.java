package com.turkcell.rentACarProject.business.dtos.additionalService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListAdditionalServiceDto {
	
	private int id;
	private String name;
	private double dailyPrice;
}
