package com.zy.product.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zy.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{

	
	
}
