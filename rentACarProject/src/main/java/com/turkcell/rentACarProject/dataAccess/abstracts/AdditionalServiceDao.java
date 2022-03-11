package com.turkcell.rentACarProject.dataAccess.abstracts;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.turkcell.rentACarProject.entities.concretes.AdditionalService;
import com.turkcell.rentACarProject.entities.concretes.Rental;

public interface AdditionalServiceDao extends JpaRepository<AdditionalService, Integer> {

    AdditionalService getAdditionalServiceById(int id);
    List<AdditionalService> getAdditionalServicesByRentalId(Rental rental);
}
