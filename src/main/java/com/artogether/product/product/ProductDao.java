package com.artogether.product.product;


import java.util.List;

public interface ProductDao {

	int add(Product product);
	int update(Product product);
	int delete(Integer Id);
	Product findByPK(Integer Id);
	List<Product> getAll();
	
}
