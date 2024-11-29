package com.artogether.product.prd_report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrdReportServiceImpl implements PrdReportService {

    @Autowired
    private PrdReportDao prdReportDao;

    @Override
    public int addReport(PrdReport prdReport) {
        try {
            return prdReportDao.add(prdReport); // 調用 DAO 新增方法
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // 失敗返回 -1
        }
    }

    @Override
    public int updateReport(PrdReport prdReport) {
        try {
            return prdReportDao.update(prdReport); // 調用 DAO 更新方法
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // 失敗返回 -1
        }
    }

    @Override
    public int deleteReport(Integer id) {
        try {
            return prdReportDao.delete(id); // 調用 DAO 刪除方法
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // 失敗返回 -1
        }
    }

    @Override
    public PrdReport findReportById(Integer id) {
        try {
            return prdReportDao.findByPK(id); // 調用 DAO 查詢方法
        } catch (Exception e) {
            e.printStackTrace();
            return null; // 發生錯誤返回 null
        }
    }

    @Override
    public List<PrdReport> getAllReports() {
        try {
            return prdReportDao.getAll(); // 調用 DAO 查詢所有檢舉記錄方法
        } catch (Exception e) {
            e.printStackTrace();
            return null; // 發生錯誤返回 null
        }
    }

    @Override
    public int submitReport(PrdReport prdReport) {
        try {
            return prdReportDao.submitReport(prdReport); // 調用 DAO 提交檢舉方法
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // 失敗返回 -1
        }
    }

    @Override
    public List<PrdReport> findReportsByMember(Integer memberId) {
        try {
            return prdReportDao.findReportsByMemberId(memberId); // 調用 DAO 查詢會員檢舉記錄方法
        } catch (Exception e) {
            e.printStackTrace();
            return null; // 發生錯誤返回 null
        }
    }

    @Override
    public boolean hasReportedProduct(Integer memberId, Integer prdId) {
        try {
            return prdReportDao.hasReportedProduct(memberId, prdId); // 調用 DAO 檢查檢舉記錄方法
        } catch (Exception e) {
            e.printStackTrace();
            return false; // 發生錯誤返回 false
        }
    }
}

