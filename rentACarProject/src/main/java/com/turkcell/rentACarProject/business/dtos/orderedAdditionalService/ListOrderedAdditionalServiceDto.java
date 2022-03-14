package com.turkcell.rentACarProject.business.dtos.orderedAdditionalService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListOrderedAdditionalServiceDto {
	private int id;
	private int rentalId;
	private int additionalServiceId;
}
