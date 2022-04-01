package com.turkcell.rentACarProject.dataAccess.abstracts;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.turkcell.rentACarProject.entities.concretes.Invoice;

@Repository
public interface InvoiceDao extends JpaRepository<Invoice, Integer>{

	Invoice getByInvoiceId(int id);
	
	List<Invoice> getByCustomer_customerId(int customerId);
	
	List<Invoice> findByCreateDateBetween(LocalDate creationDate, LocalDate returnDate);
	
	boolean existsByInvoiceNo(String invoiceNumber);
	
	Invoice getByRentalCar_rentalCarId(int carId);
}
