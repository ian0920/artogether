package com.artogether.product.prd_catalog;

import java.util.List;


public interface Prd_CatalogDao {
	
	int add(Prd_Catalog prd_catalog);
	int update(Prd_Catalog prd_catalog);
	int delete(Integer Id);
	Prd_Catalog findByPK(Integer Id);
	List<Prd_Catalog> getAll();

}
