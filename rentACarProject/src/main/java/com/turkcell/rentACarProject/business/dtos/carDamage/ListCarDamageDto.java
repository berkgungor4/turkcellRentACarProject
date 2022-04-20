package com.turkcell.rentACarProject.business.dtos.carDamage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListCarDamageDto {

	private int id;
	private String description;
	private int carId;
}
