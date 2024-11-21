package com.artogether.product.prd_report;


import java.util.List;

public interface Prd_ReportDao {

	int add(Prd_Report prd_report);
	int update(Prd_Report prd_report);
	int delete(Integer Id);
	Prd_Report findByPK(Integer Id);
	List<Prd_Report> getAll();
	
}
