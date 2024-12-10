package com.artogether.product.prd_coup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrdCoupServiceImpl implements PrdCoupService {

    @Autowired
    private PrdCoupDao prdCoupDao;

    // 新增優惠券
    @Override
    @Transactional
    public int addPrdCoup(PrdCoup prdCoup) {
        return prdCoupDao.add(prdCoup);
    }

    // 更新優惠券
    @Override
    @Transactional
    public int updatePrdCoup(PrdCoup prdCoup) {
        return prdCoupDao.update(prdCoup);
    }

    // 刪除優惠券
    @Override
    @Transactional
    public int deletePrdCoup(PrdCoup prdCoup) {
        if (prdCoup.getId() == null) {
            throw new IllegalArgumentException("ID 不能為空");
        }
        return prdCoupDao.delete(prdCoup.getId());
    }

    // 根據 ID 查詢優惠券
    @Override
    @Transactional(readOnly = true)
    public PrdCoup findCoupById(Integer id) {
        PrdCoup prdCoup = prdCoupDao.findByPK(id);
        if (prdCoup == null) {
            throw new IllegalArgumentException("找不到 ID 為 " + id + " 的優惠券");
        }
        return prdCoup;
    }

    // 查詢所有優惠券
    @Override
    @Transactional(readOnly = true)
    public List<PrdCoup> getAllCoupons() {
        return prdCoupDao.getAll();
    }

    // 根據商家 ID 查詢優惠券
    @Override
    @Transactional(readOnly = true)
    public List<PrdCoup> findCouponsByBusinessId(Integer businessId) {
        return prdCoupDao.findByBusinessId(businessId);
    }

    // 查詢有效優惠券
    @Override
    @Transactional(readOnly = true)
    public List<PrdCoup> findValidCoupons(Date now) {
        return prdCoupDao.findValidCoupons(now);
    }

    // 根據條件查詢優惠券
    @Override
    @Transactional(readOnly = true)
    public List<PrdCoup> findCouponsByCriteria(String name, Integer type, Integer status, Integer threshold) {
        return prdCoupDao.findCouponsByCriteria(name, type, status, threshold);
    }

    // 查詢即將過期的優惠券
    @Override
    @Transactional(readOnly = true)
    public List<PrdCoup> findCouponsExpiringSoon(Date now, Integer days) {
        return prdCoupDao.findCouponsExpiringSoon(now, days);
    }

    // 根據狀態查詢優惠券
    @Override
    @Transactional(readOnly = true)
    public List<PrdCoup> findCouponsByStatus(Integer status) {
        return prdCoupDao.findCouponsByStatus(status);
    }

    // 根據類型查詢優惠券
    @Override
    @Transactional(readOnly = true)
    public List<PrdCoup> findCouponsByType(Integer type) {
        return prdCoupDao.findCouponsByType(type);
    }

    // 根據多重條件查詢優惠券
    @Override
    @Transactional(readOnly = true)
    public List<PrdCoup> findCouponsByMultipleCriteria(Integer status, Integer type, Integer threshold) {
        return prdCoupDao.findCouponsByMultipleCriteria(status, type, threshold);
    }

    // 查詢有效期間內的優惠券
    @Override
    @Transactional(readOnly = true)
    public List<PrdCoup> findActiveCoupons(Date currentDate) {
        return prdCoupDao.findActiveCoupons(currentDate);
    }

    // 新增: 將 PrdCoup 轉換為 PrdCoupDto
    public PrdCoupDto toPrdCoupDto(PrdCoup prdCoup) {
        return new PrdCoupDto(
            prdCoup.getId(),
            prdCoup.getBusinessMember() != null ? prdCoup.getBusinessMember().getName() : null,
            prdCoup.getBusinessMember() != null ? prdCoup.getBusinessMember().getId() : null,
            prdCoup.getName(),
            prdCoup.getStatus(),
            prdCoup.getType(),
            prdCoup.getDeduction(),
            prdCoup.getRatio(),
            prdCoup.getStartDate(),
            prdCoup.getEndDate(),
            prdCoup.getDuration(),
            prdCoup.getThreshold()
        );
    }

    // 新增: 將列表中的 PrdCoup 轉換為 PrdCoupDto 列表
    public List<PrdCoupDto> toPrdCoupDtoList(List<PrdCoup> prdCoups) {
        return prdCoups.stream()
                       .map(this::toPrdCoupDto)
                       .collect(Collectors.toList());
    }
}
