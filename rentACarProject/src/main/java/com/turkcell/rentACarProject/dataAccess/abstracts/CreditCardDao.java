package com.turkcell.rentACarProject.dataAccess.abstracts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.turkcell.rentACarProject.entities.concretes.CreditCard;

@Repository
public interface CreditCardDao extends JpaRepository<CreditCard, Integer> {
	
	CreditCard getCreditCardById(int id);
}
