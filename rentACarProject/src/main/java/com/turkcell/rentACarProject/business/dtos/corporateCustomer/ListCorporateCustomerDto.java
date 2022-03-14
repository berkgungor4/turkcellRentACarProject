package com.turkcell.rentACarProject.business.dtos.corporateCustomer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListCorporateCustomerDto {
	
	private int id;
	private String email;
	private String password;
	private String name;
	private int taxNumber;
}
