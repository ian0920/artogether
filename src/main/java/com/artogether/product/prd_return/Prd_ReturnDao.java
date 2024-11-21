package com.artogether.product.prd_return;


import java.util.List;

public interface Prd_ReturnDao {

	int add(Prd_Return prd_return);
	int update(Prd_Return prd_return);
	int delete(Integer Id);
	Prd_Return findByPK(Integer Id);
	List<Prd_Return> getAll();
	
}
