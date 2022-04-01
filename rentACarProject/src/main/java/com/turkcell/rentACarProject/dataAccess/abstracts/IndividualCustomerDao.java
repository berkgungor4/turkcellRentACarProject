package com.turkcell.rentACarProject.dataAccess.abstracts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.turkcell.rentACarProject.entities.concretes.IndividualCustomer;

@Repository
public interface IndividualCustomerDao extends JpaRepository<IndividualCustomer, Integer> {

	IndividualCustomer getByIndividualCustomerId(int id);
	
	IndividualCustomer findByEmail(String email);
	
	IndividualCustomer findByIdentityNumber(String identityNmuber);
}
