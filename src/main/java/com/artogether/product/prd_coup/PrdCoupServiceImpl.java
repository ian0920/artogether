package com.artogether.product.prd_coup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PrdCoupServiceImpl implements PrdCoupService {

    @Autowired
    private PrdCoupDao prdCoupDao;

    @Override
    public int addPrdCoup(PrdCoup prdCoup) {
        try {
            return prdCoupDao.add(prdCoup); // 調用 DAO 的新增方法
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // 發生錯誤返回 -1
        }
    }

    @Override
    public int updatePrdCoup(PrdCoup prdCoup) {
        try {
            return prdCoupDao.update(prdCoup); // 調用 DAO 的更新方法
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // 發生錯誤返回 -1
        }
    }

    @Override
    public int deletePrdCoup(PrdCoup prdCoup) {
        try {
            return prdCoupDao.delete(prdCoup.getId()); // 調用 DAO 的刪除方法
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // 發生錯誤返回 -1
        }
    }

    @Override
    public PrdCoup findCoupById(Integer id) {
        try {
            return prdCoupDao.findByPK(id); // 調用 DAO 的查詢方法
        } catch (Exception e) {
            e.printStackTrace();
            return null; // 查詢失敗返回 null
        }
    }

    @Override
    public List<PrdCoup> getAllCoupons() {
        try {
            return prdCoupDao.getAll(); // 調用 DAO 查詢所有優惠券
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // 查詢失敗返回空列表
        }
    }

    @Override
    public List<PrdCoup> findCouponsByBusinessId(Integer businessId) {
        try {
            return prdCoupDao.findByBusinessId(businessId); // 查詢特定商家的優惠券
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // 查詢失敗返回空列表
        }
    }

    @Override
    public List<PrdCoup> findValidCoupons(LocalDateTime now) {
        try {
            return prdCoupDao.findValidCoupons(now); // 查詢有效的優惠券
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // 查詢失敗返回空列表
        }
    }

    @Override
    public List<PrdCoup> findCouponsByCriteria(String name, Integer type, Integer status, Integer threshold) {
        try {
            return prdCoupDao.findCouponsByCriteria(name, type, status, threshold); // 根據條件查詢優惠券
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // 查詢失敗返回空列表
        }
    }

    @Override
    public List<PrdCoup> findCouponsExpiringSoon(LocalDateTime now, Integer days) {
        try {
            return prdCoupDao.findCouponsExpiringSoon(now, days); // 查詢快過期的優惠券
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // 查詢失敗返回空列表
        }
    }

    @Override
    public List<PrdCoup> findCouponsByStatus(Integer status) {
        try {
            return prdCoupDao.findCouponsByStatus(status); // 根據狀態查詢優惠券
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // 查詢失敗返回空列表
        }
    }

    @Override
    public List<PrdCoup> findCouponsByType(Integer type) {
        try {
            return prdCoupDao.findCouponsByType(type); // 根據類型查詢優惠券
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // 查詢失敗返回空列表
        }
    }

    @Override
    public List<PrdCoup> findCouponsByMultipleCriteria(Integer status, Integer type, Integer threshold) {
        try {
            return prdCoupDao.findCouponsByMultipleCriteria(status, type, threshold); // 根據條件組合查詢
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // 查詢失敗返回空列表
        }
    }

    @Override
    public List<PrdCoup> findActiveCoupons(LocalDateTime currentDate) {
        try {
            return prdCoupDao.findActiveCoupons(currentDate); // 查詢有效期間內的優惠券
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // 查詢失敗返回空列表
        }
    }
}

