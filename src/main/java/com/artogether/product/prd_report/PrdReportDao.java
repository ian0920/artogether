package com.artogether.product.prd_report;


import java.util.List;

public interface PrdReportDao {

	int add(PrdReport prd_report);
	int update(PrdReport prd_report);
	int delete(Integer Id);
	PrdReport findByPK(Integer Id);
	List<PrdReport> getAll();
	
}
