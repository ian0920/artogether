package com.artogether.product.prd_return;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PrdReturnServiceImpl implements PrdReturnService {

    @Autowired
    private PrdReturnDao prdReturnDao;

    // 新增退換貨申請
    @Override
    @Transactional
    public int addPrdReturn(PrdReturn prdReturn) {
        try {
            return prdReturnDao.add(prdReturn);
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // 新增失敗返回 -1
        }
    }

    // 更新退換貨申請
    @Override
    @Transactional
    public int updatePrdReturn(PrdReturn prdReturn) {
        try {
            return prdReturnDao.update(prdReturn);
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // 更新失敗返回 -1
        }
    }

    // 刪除退換貨申請
    @Override
    @Transactional
    public int deletePrdReturn(Integer id) {
        try {
            return prdReturnDao.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // 刪除失敗返回 -1
        }
    }

    // 查詢單筆退換貨申請
    @Override
    @Transactional(readOnly = true)
    public PrdReturn findPrdReturnById(Integer id) {
        try {
            return prdReturnDao.findByPK(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null; // 查詢失敗返回 null
        }
    }

    // 查詢所有退換貨申請
    @Override
    @Transactional(readOnly = true)
    public List<PrdReturn> getAllPrdReturns() {
        try {
            return prdReturnDao.getAll();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // 查詢失敗返回空列表
        }
    }

    // 按狀態查詢退換貨申請
    @Override
    @Transactional(readOnly = true)
    public List<PrdReturn> findPrdReturnsByStatus(Integer status) {
        try {
            return prdReturnDao.findByStatus(status);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // 查詢失敗返回空列表
        }
    }

    // 按申請時間範圍查詢退換貨申請
    @Override
    @Transactional(readOnly = true)
    public List<PrdReturn> findPrdReturnsByApplyDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        try {
            return prdReturnDao.findByApplyDateRange(startDate, endDate);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // 查詢失敗返回空列表
        }
    }

    // 按退換貨類型查詢
    @Override
    @Transactional(readOnly = true)
    public List<PrdReturn> findPrdReturnsByType(Integer type) {
        try {
            return prdReturnDao.findByType(type);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // 查詢失敗返回空列表
        }
    }

    // 按申請時間降序排列
    @Override
    @Transactional(readOnly = true)
    public List<PrdReturn> findAllPrdReturnsOrderByApplyDateDesc() {
        try {
            return prdReturnDao.findAllOrderByApplyDateDesc();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // 查詢失敗返回空列表
        }
    }

    // 按退換貨完成時間降序排列
    @Override
    @Transactional(readOnly = true)
    public List<PrdReturn> findAllPrdReturnsOrderByReturnDateDesc() {
        try {
            return prdReturnDao.findAllOrderByReturnDateDesc();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // 查詢失敗返回空列表
        }
    }

    // 查詢即將過期的退換貨申請
    @Override
    @Transactional(readOnly = true)
    public List<PrdReturn> findExpiredReturns(LocalDateTime currentDate, Integer days) {
        try {
            return prdReturnDao.findExpiredReturns(currentDate, days);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // 查詢失敗返回空列表
        }
    }

    // 單筆更新退換貨申請狀態
    @Override
    @Transactional
    public int updatePrdReturnStatus(Integer id, Integer status) {
        try {
            return prdReturnDao.updateStatus(id, status);
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // 更新失敗返回 -1
        }
    }

    // 批量更新退換貨申請狀態
    @Override
    @Transactional
    public int batchUpdatePrdReturnStatus(List<Integer> ids, Integer status) {
        try {
            return prdReturnDao.batchUpdateStatus(ids, status);
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // 更新失敗返回 -1
        }
    }
}

