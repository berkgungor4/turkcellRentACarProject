package com.turkcell.rentACarProject.dataAccess.abstracts;

import java.util.List;



import com.turkcell.rentACarProject.entities.concretes.Brand;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandDao extends JpaRepository<Brand, Integer>{
	
	Brand getBrandById(int id);
	
	List<Brand> getBrandByName(String name);
}


