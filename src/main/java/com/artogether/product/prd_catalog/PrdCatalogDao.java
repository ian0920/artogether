package com.artogether.product.prd_catalog;

import java.util.List;


public interface PrdCatalogDao {
	
	int add(PrdCatalog prdCatalog);
	int update(PrdCatalog prdCatalog);
	int delete(Integer Id);
	PrdCatalog findByPK(Integer Id);
	List<PrdCatalog> getAll();

}
