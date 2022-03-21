package com.turkcell.rentACarProject.business.dtos.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListPaymentDto {
	
	private int id;
	private int invoiceId;
	private int orderedAdditionalServiceId;
}
