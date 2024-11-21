package com.artogether.product.prd_coup;

import java.util.List;


public interface Prd_CoupDao {
	
	int add(Prd_Coup prd_coup);
	int update(Prd_Coup prd_coup);
	int delete(Integer Id);
	Prd_Coup findByPK(Integer Id);
	List<Prd_Coup> getAll();
	
	
}
