package com.zy.product.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.zy.entity.Product;


public interface ProductRepository extends JpaRepository<Product, Long>{

	
	
}
