package com.artogether.product.prd_report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrdReportMgServiceImpl implements PrdReportMgService {

    @Autowired
    private PrdReportMgDao prdReportMgDao;

    @Override
    public int addPrdReport(PrdReport prdReport) {
        try {
            return prdReportMgDao.add(prdReport);
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // 失敗返回 -1
        }
    }

    @Override
    public int updatePrdReport(PrdReport prdReport) {
        try {
            return prdReportMgDao.update(prdReport);
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // 失敗返回 -1
        }
    }

    @Override
    public int deletePrdReport(Integer id) {
        try {
            return prdReportMgDao.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // 失敗返回 -1
        }
    }

    @Override
    public PrdReport findPrdReportById(Integer id) {
        try {
            return prdReportMgDao.findByPK(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null; // 發生錯誤返回 null
        }
    }

    @Override
    public List<PrdReport> getAllReports() {
        try {
            return prdReportMgDao.getAll();
        } catch (Exception e) {
            e.printStackTrace();
            return null; // 發生錯誤返回 null
        }
    }

    @Override
    public List<PrdReport> findReportsByMember(Integer memberId) {
        try {
            return prdReportMgDao.findByMemberId(memberId);
        } catch (Exception e) {
            e.printStackTrace();
            return null; // 發生錯誤返回 null
        }
    }

    @Override
    public List<PrdReport> findReportsByOrder(Integer orderId) {
        try {
            return prdReportMgDao.findByOrderId(orderId);
        } catch (Exception e) {
            e.printStackTrace();
            return null; // 發生錯誤返回 null
        }
    }

    @Override
    public List<PrdReport> findReportsByProduct(Integer prdId) {
        try {
            return prdReportMgDao.findByProductId(prdId);
        } catch (Exception e) {
            e.printStackTrace();
            return null; // 發生錯誤返回 null
        }
    }

    @Override
    public List<PrdReport> findReportsByStatus(Integer status) {
        try {
            return prdReportMgDao.findByStatus(status);
        } catch (Exception e) {
            e.printStackTrace();
            return null; // 發生錯誤返回 null
        }
    }

    @Override
    public int updateStatusBatch(List<Integer> reportIds, Integer status) {
        try {
            return prdReportMgDao.updateStatusBatch(reportIds, status);
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // 失敗返回 -1
        }
    }

    @Override
    public boolean hasMemberReportedProduct(Integer memberId, Integer prdId) {
        try {
            return prdReportMgDao.hasMemberReportedProduct(memberId, prdId);
        } catch (Exception e) {
            e.printStackTrace();
            return false; // 發生錯誤返回 false
        }
    }

    @Override
    public int deleteReportsBatch(List<Integer> reportIds) {
        try {
            return prdReportMgDao.deleteReportsBatch(reportIds);
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // 失敗返回 -1
        }
    }
}

