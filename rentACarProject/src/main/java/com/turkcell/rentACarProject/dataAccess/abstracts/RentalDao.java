package com.turkcell.rentACarProject.dataAccess.abstracts;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.turkcell.rentACarProject.entities.concretes.Rental;

@Repository
public interface RentalDao extends JpaRepository<Rental, Integer> {
	
	Rental getByRentalId(int id);
	
	Rental getByCustomer_CustomerId(int customerId);

	List<Rental> getByCar_CarId(int carId);
	
}
