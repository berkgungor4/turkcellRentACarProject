package com.turkcell.rentACarProject.dataAccess.abstracts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.turkcell.rentACarProject.entities.concretes.Payment;

@Repository
public interface PaymentDao extends JpaRepository<Payment, Integer> {
	
	Payment getByPaymentId(int id);
	
	Payment getByInvoice_invoiceId(int invoiceId);
	
	Payment getByOrderedAdditionalService_orderedAdditionalServiceId(int orderedAdditionalServiceId);
}
