package com.turkcell.rentACarProject.business.requests.invoice;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateInvoiceRequest {
	
	private int id;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate creationDate;
}
