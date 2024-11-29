package com.artogether.product.prd_return;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ApplyPrdReturnServiceImpl implements ApplyPrdReturnService {

    @Autowired
    private ApplyPrdReturnDao applyPrdReturnDao;

    @Override
    public int addPrdReturn(PrdReturn prdReturn) {
        try {
            return applyPrdReturnDao.add(prdReturn); // 呼叫 DAO 層新增方法
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // 新增失敗返回 -1
        }
    }

    @Override
    public int updatePrdReturn(PrdReturn prdReturn) {
        try {
            return applyPrdReturnDao.update(prdReturn); // 呼叫 DAO 層更新方法
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // 更新失敗返回 -1
        }
    }

    @Override
    public int deletePrdReturn(Integer id) {
        try {
            return applyPrdReturnDao.delete(id); // 呼叫 DAO 層刪除方法
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // 刪除失敗返回 -1
        }
    }

    @Override
    public PrdReturn findPrdReturnById(Integer id) {
        try {
            return applyPrdReturnDao.findByPK(id); // 呼叫 DAO 層查詢單筆記錄方法
        } catch (Exception e) {
            e.printStackTrace();
            return null; // 查詢失敗返回 null
        }
    }

    @Override
    public List<PrdReturn> findPrdReturnsByStatus(Integer status) {
        try {
            return applyPrdReturnDao.findByStatus(status); // 呼叫 DAO 層按狀態篩選方法
        } catch (Exception e) {
            e.printStackTrace();
            return null; // 查詢失敗返回 null
        }
    }

    @Override
    public List<PrdReturn> findPrdReturnsByApplyDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        try {
            return applyPrdReturnDao.findByApplyDateRange(startDate, endDate); // 呼叫 DAO 層時間範圍查詢方法
        } catch (Exception e) {
            e.printStackTrace();
            return null; // 查詢失敗返回 null
        }
    }

    @Override
    public List<PrdReturn> findPrdReturnsByType(Integer type) {
        try {
            return applyPrdReturnDao.findByType(type); // 呼叫 DAO 層按類型查詢方法
        } catch (Exception e) {
            e.printStackTrace();
            return null; // 查詢失敗返回 null
        }
    }

    @Override
    public List<PrdReturn> findAllPrdReturnsOrderedByReturnDate() {
        try {
            return applyPrdReturnDao.findAllOrderByReturnDateDesc(); // 呼叫 DAO 層按日期降序排列查詢方法
        } catch (Exception e) {
            e.printStackTrace();
            return null; // 查詢失敗返回 null
        }
    }
}

