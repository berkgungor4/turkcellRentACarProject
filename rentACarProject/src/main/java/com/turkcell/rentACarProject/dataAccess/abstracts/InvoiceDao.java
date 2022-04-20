package com.turkcell.rentACarProject.dataAccess.abstracts;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.turkcell.rentACarProject.entities.concretes.Invoice;

@Repository
public interface InvoiceDao extends JpaRepository<Invoice, Integer>{

	Invoice getInvoiceById(int id);
	
	List<Invoice> getByCustomer_id(int id);
	
	List<Invoice> findByCreationDateBetween(LocalDate creationDate, LocalDate returnDate);
	
	List<Invoice>getByRental_id(int id);
	
	boolean existsByInvoiceNumber(String invoiceNumber);
}
