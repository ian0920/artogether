package com.artogether.product.prd_return;

import java.time.LocalDateTime;
import java.util.List;

public interface ApplyPrdReturnDao {

	int add(PrdReturn prdReturn);

	int update(PrdReturn prdReturn);// 軟刪除

	int delete(Integer id);

	PrdReturn findByPK(Integer id); // 查詢單筆申請

	List<PrdReturn> findByStatus(Integer status); // 查詢按狀態篩選的記錄

	List<PrdReturn> findByApplyDateRange(LocalDateTime startDate, LocalDateTime endDate);

	List<PrdReturn> findByType(Integer type); // 0=換貨，1=退貨

	List<PrdReturn> findAllOrderByReturnDateDesc();

}

