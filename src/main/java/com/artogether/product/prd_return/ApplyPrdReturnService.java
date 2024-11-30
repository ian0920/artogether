package com.artogether.product.prd_return;

import java.time.LocalDateTime;
import java.util.List;

public interface ApplyPrdReturnService {

    // 1. 新增退換貨申請
    int addPrdReturn(PrdReturn prdReturn);

    // 2. 更新退換貨申請（支持軟刪除）
    int updatePrdReturn(PrdReturn prdReturn);

    // 3. 刪除退換貨申請
    int deletePrdReturn(Integer id);

    // 4. 根據主鍵查詢單筆退換貨申請
    PrdReturn findPrdReturnById(Integer id);

    // 5. 根據狀態查詢退換貨申請記錄
    List<PrdReturn> findPrdReturnsByStatus(Integer status);

    // 6. 查詢指定時間範圍內的退換貨申請記錄
    List<PrdReturn> findPrdReturnsByApplyDateRange(LocalDateTime startDate, LocalDateTime endDate);

    // 7. 根據類型查詢退換貨申請（0=換貨，1=退貨）
    List<PrdReturn> findPrdReturnsByType(Integer type);

    // 8. 按退換貨日期降序排序，查詢所有記錄
    List<PrdReturn> findAllPrdReturnsOrderedByReturnDate();
}

