package com.artogether.product.prd_coup;

import java.util.List;


public interface PrdCoupDao {
	
	int add(PrdCoup prd_coup);
	int update(PrdCoup prd_coup);
	int delete(Integer Id);
	PrdCoup findByPK(Integer Id);
	List<PrdCoup> getAll();
	
	
}
