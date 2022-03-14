package com.turkcell.rentACarProject.dataAccess.abstracts;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.turkcell.rentACarProject.entities.concretes.OrderedAdditionalService;

public interface OrderedAdditionalServiceDao extends JpaRepository<OrderedAdditionalService, Integer>{
	
	List<OrderedAdditionalService> findAllByRentalId(int RentalId);

}
