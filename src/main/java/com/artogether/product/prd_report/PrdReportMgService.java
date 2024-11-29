package com.artogether.product.prd_report;

import java.util.List;

public interface PrdReportMgService {

    int addPrdReport(PrdReport prdReport); // 新增檢舉記錄

    int updatePrdReport(PrdReport prdReport); // 更新檢舉記錄

    int deletePrdReport(Integer id); // 刪除檢舉記錄

    PrdReport findPrdReportById(Integer id); // 查詢單筆檢舉記錄

    List<PrdReport> getAllReports(); // 查詢所有檢舉記錄

    List<PrdReport> findReportsByMember(Integer memberId); // 查詢特定會員的檢舉記錄

    List<PrdReport> findReportsByOrder(Integer orderId); // 查詢特定訂單的檢舉記錄

    List<PrdReport> findReportsByProduct(Integer prdId); // 查詢特定商品的檢舉記錄

    List<PrdReport> findReportsByStatus(Integer status); // 根據狀態查詢檢舉記錄

    int updateStatusBatch(List<Integer> reportIds, Integer status); // 批量更新檢舉狀態

    boolean hasMemberReportedProduct(Integer memberId, Integer prdId); // 檢查會員是否已檢舉過商品

    int deleteReportsBatch(List<Integer> reportIds); // 批量刪除檢舉記錄
}

