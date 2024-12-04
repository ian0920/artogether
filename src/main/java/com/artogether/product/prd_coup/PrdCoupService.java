package com.artogether.product.prd_coup;


import java.util.Date;
import java.util.List;

public interface PrdCoupService {

	int addPrdCoup(PrdCoup prdCoup);

	int updatePrdCoup(PrdCoup prdCoup);

	int deletePrdCoup(PrdCoup prdCoup);

	PrdCoup findCoupById(Integer id);

	List<PrdCoup> getAllCoupons();

	List<PrdCoup> findCouponsByBusinessId(Integer businessId); // 查詢特定商家的優惠券

	List<PrdCoup> findValidCoupons(Date now); // 查詢有效的優惠券

	List<PrdCoup> findCouponsByCriteria(String name, Integer type, Integer status, Integer threshold); // 根據條件查詢優惠券

	List<PrdCoup> findCouponsExpiringSoon(Date now, Integer days); // 查詢快過期的優惠券

	List<PrdCoup> findCouponsByStatus(Integer status); // 根據狀態查詢優惠券

	List<PrdCoup> findCouponsByType(Integer type); // 根據類型查詢優惠券

	List<PrdCoup> findCouponsByMultipleCriteria(Integer status, Integer type, Integer threshold); // 條件組合查詢

	List<PrdCoup> findActiveCoupons( Date currentDate); // 查詢有效期間內的優惠券

}
