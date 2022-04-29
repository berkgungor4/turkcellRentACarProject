package com.turkcell.rentACarProject.dataAccess.abstracts;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.turkcell.rentACarProject.entities.concretes.OrderedAdditionalService;

@Repository
public interface OrderedAdditionalServiceDao extends JpaRepository<OrderedAdditionalService, Integer> {

	OrderedAdditionalService getOrderedAdditionalServiceById(int id);

	List<OrderedAdditionalService> getByRental_id(int id);

	List<OrderedAdditionalService> getByAdditionalService_id(int id);
}
