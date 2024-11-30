package com.artogether.product.prd_report;

import java.util.List;

public interface PrdReportDao {

	int add(PrdReport prdReport);

	int update(PrdReport prdReport);

	int delete(Integer Id);

	PrdReport findByPK(Integer Id);

	List<PrdReport> getAll();

	int submitReport(PrdReport prdReport);// 提交檢舉

	List<PrdReport> findReportsByMemberId(Integer memberId);// 查詢自己的檢舉記錄

	boolean hasReportedProduct(Integer memberId, Integer prdId);// 檢查會員是否已檢舉過某商品

}
