package com.artogether.product.prd_report;

import java.util.List;

public interface PrdReportMgDao {

	int add(PrdReport prd_report);

	int update(PrdReport prd_report);

	int delete(Integer Id);

	PrdReport findByPK(Integer Id);

	List<PrdReport> getAll();

	List<PrdReport> findByMemberId(Integer memberId);// 查詢特定會員的檢舉記錄

	List<PrdReport> findByOrderId(Integer orderId);// 查詢特定訂單的檢舉記錄

	List<PrdReport> findByProductId(Integer prdId);// 查詢特定商品的檢舉記錄

	List<PrdReport> findByStatus(Integer status);// 依檢舉狀態篩選記錄

	int updateStatusBatch(List<Integer> reportIds, Integer status);// 批量更新檢舉狀態

	boolean hasMemberReportedProduct(Integer memberId, Integer prdId);// 檢查特定會員是否已檢舉過某商品

	int deleteReportsBatch(List<Integer> reportIds);// 批量刪除檢舉記錄

}
