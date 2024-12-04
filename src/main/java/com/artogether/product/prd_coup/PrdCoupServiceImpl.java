package com.artogether.product.prd_coup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class PrdCoupServiceImpl implements PrdCoupService {

    @Autowired
    private PrdCoupDao prdCoupDao;

    @Override
    @Transactional
    public int addPrdCoup(PrdCoup prdCoup) {
        return prdCoupDao.add(prdCoup); // 調用 DAO 層新增方法
    }

    @Override
    @Transactional
    public int updatePrdCoup(PrdCoup prdCoup) {
        return prdCoupDao.update(prdCoup); // 調用 DAO 層更新方法
    }

    @Override
    @Transactional
    public int deletePrdCoup(PrdCoup prdCoup) {
        if (prdCoup.getId() == null) {
            throw new IllegalArgumentException("ID 不能為空");
        }
        return prdCoupDao.delete(prdCoup.getId()); // 調用 DAO 層刪除方法
    }

    @Override
    @Transactional(readOnly = true)
    public PrdCoup findCoupById(Integer id) {
        PrdCoup prdCoup = prdCoupDao.findByPK(id); // 調用 DAO 層查詢方法
        if (prdCoup == null) {
            throw new IllegalArgumentException("找不到 ID 為 " + id + " 的優惠券");
        }
        return prdCoup;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrdCoup> getAllCoupons() {
        return prdCoupDao.getAll(); // 調用 DAO 層查詢所有優惠券
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrdCoup> findCouponsByBusinessId(Integer businessId) {
        return prdCoupDao.findByBusinessId(businessId); // 調用 DAO 層查詢特定商家優惠券
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrdCoup> findValidCoupons(Date now) {
        return prdCoupDao.findValidCoupons(now); // 調用 DAO 層查詢有效的優惠券
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrdCoup> findCouponsByCriteria(String name, Integer type, Integer status, Integer threshold) {
        return prdCoupDao.findCouponsByCriteria(name, type, status, threshold); // 調用 DAO 層查詢符合條件的優惠券
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrdCoup> findCouponsExpiringSoon(Date now, Integer days) {
        return prdCoupDao.findCouponsExpiringSoon(now, days); // 調用 DAO 層查詢快過期的優惠券
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrdCoup> findCouponsByStatus(Integer status) {
        return prdCoupDao.findCouponsByStatus(status); // 調用 DAO 層查詢特定狀態的優惠券
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrdCoup> findCouponsByType(Integer type) {
        return prdCoupDao.findCouponsByType(type); // 調用 DAO 層查詢特定類型的優惠券
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrdCoup> findCouponsByMultipleCriteria(Integer status, Integer type, Integer threshold) {
        return prdCoupDao.findCouponsByMultipleCriteria(status, type, threshold); // 調用 DAO 層條件組合查詢優惠券
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrdCoup> findActiveCoupons( Date currentDate) {
        return prdCoupDao.findActiveCoupons(currentDate); // 調用 DAO 層查詢有效期間內的優惠券
    }
}
