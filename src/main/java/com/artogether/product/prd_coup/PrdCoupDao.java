package com.artogether.product.prd_coup;


import java.util.Date;
import java.util.List;

public interface PrdCoupDao {

	int add(PrdCoup prdCoup);

	int update(PrdCoup prdCoup);

	int delete(Integer id);

	PrdCoup findByPK(Integer id);

	List<PrdCoup> getAll();

	List<PrdCoup> findByBusinessId(Integer businessId);// 查詢特定商家的優惠券

	List<PrdCoup> findValidCoupons(Date now);// 查詢有效的優惠券

	List<PrdCoup> findCouponsByCriteria(String name, Integer type, Integer status, Integer threshold);// 條件查詢優惠券

	List<PrdCoup> findCouponsExpiringSoon(Date now, Integer days);// 計算快過期的優惠券

	List<PrdCoup> findCouponsByStatus(Integer status);// 依優惠券狀態查詢

	List<PrdCoup> findCouponsByType(Integer type);// 依折扣類型查詢

	List<PrdCoup> findCouponsByMultipleCriteria(Integer status, Integer type, Integer threshold);// 條件組合查詢

	List<PrdCoup> findActiveCoupons(Date currentDate);// 查詢有效期間內的優惠券

}
