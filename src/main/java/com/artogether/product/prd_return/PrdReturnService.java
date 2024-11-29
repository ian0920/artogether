package com.artogether.product.prd_return;

import java.time.LocalDateTime;
import java.util.List;

public interface PrdReturnService {

    // 基本 CRUD 操作
    int addPrdReturn(PrdReturn prdReturn); // 新增退換貨申請
    int updatePrdReturn(PrdReturn prdReturn); // 更新退換貨申請
    int deletePrdReturn(Integer id); // 刪除退換貨申請
    PrdReturn findPrdReturnById(Integer id); // 查詢單筆退換貨申請
    List<PrdReturn> getAllPrdReturns(); // 查詢所有退換貨申請

    // 查詢操作
    List<PrdReturn> findPrdReturnsByStatus(Integer status); // 依狀態查詢
    List<PrdReturn> findPrdReturnsByApplyDateRange(LocalDateTime startDate, LocalDateTime endDate); // 依申請時間範圍查詢
    List<PrdReturn> findPrdReturnsByType(Integer type); // 依退換貨類型查詢
    List<PrdReturn> findAllPrdReturnsOrderByApplyDateDesc(); // 按申請時間降序排列
    List<PrdReturn> findAllPrdReturnsOrderByReturnDateDesc(); // 按退換貨完成時間降序排列
    List<PrdReturn> findExpiredReturns(LocalDateTime currentDate, Integer days); // 查詢即將過期的退換貨申請

    // 狀態管理
    int updatePrdReturnStatus(Integer id, Integer status); // 單筆記錄的狀態流轉
    int batchUpdatePrdReturnStatus(List<Integer> ids, Integer status); // 批量更新狀態
}

