package com.turkcell.rentACarProject.dataAccess.abstracts;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.turkcell.rentACarProject.entities.concretes.Car;
import com.turkcell.rentACarProject.entities.concretes.Rental;

public interface RentalDao extends JpaRepository<Rental, Integer> {
	
    Rental getRentalsById(int id);
    List<Rental> getRentalsByCarId(Car car);
    List<Rental> getRentalsByCustomerId(int customerId);
}
