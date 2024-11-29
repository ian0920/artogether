package com.artogether.product.prd_return;


import java.time.LocalDateTime;
import java.util.List;

public interface PrdReturnDao {

	int add(PrdReturn prd_return);
	int update(PrdReturn prd_return);
	int delete(Integer Id);
	PrdReturn findByPK(Integer Id);
	List<PrdReturn> getAll();
	List<PrdReturn> findByStatus(Integer status);//依狀態查詢：篩選 status 為 0（未退/換貨）或 1（已退/換貨）的記錄。
	List<PrdReturn> findByApplyDateRange(LocalDateTime startDate, LocalDateTime endDate);//依申請時間範圍查詢：篩選 apply_date 在某一段時間內的申請。
	List<PrdReturn> findByType(Integer type);//依退換貨類型查詢：根據 type（0：換貨、1：退貨）進行分類
	List<PrdReturn> findAllOrderByApplyDateDesc();//按 apply_date 降序排列
	List<PrdReturn> findAllOrderByReturnDateDesc();//按return_date 降序排列
	
	//狀態流轉：將 status 設計為多個階段（例如：0=未處理、1=完成退換貨）。每完成一個步驟，就更新 status。
	int updateStatus(Integer id, Integer status);//更新單筆記錄的狀態
	int batchUpdateStatus(List<Integer> ids, Integer status);//批量更新狀態
	List<PrdReturn> findExpiredReturns(LocalDateTime currentDate, Integer days);//自動過期處理

}
