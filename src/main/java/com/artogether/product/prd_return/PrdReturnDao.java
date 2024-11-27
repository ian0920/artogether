package com.artogether.product.prd_return;


import java.util.List;

public interface PrdReturnDao {

	int add(PrdReturn prd_return);
	int update(PrdReturn prd_return);
	int delete(Integer Id);
	PrdReturn findByPK(Integer Id);
	List<PrdReturn> getAll();
	
}
