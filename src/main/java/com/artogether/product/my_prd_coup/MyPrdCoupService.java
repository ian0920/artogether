package com.artogether.product.my_prd_coup;

import java.time.LocalDateTime;
import java.util.List;

public interface MyPrdCoupService {

	MyPrdCoup.MyPrdCoupId addMyPrdCoup(MyPrdCoup myPrdCoup);//新增優惠券	

	MyPrdCoup.MyPrdCoupId updateMyPrdCoup(MyPrdCoup myPrdCoup);//更新優惠券

	boolean deleteMyPrdCoup(MyPrdCoup.MyPrdCoupId myPrdCoupId);//刪除優惠券

	MyPrdCoup findMyPrdCoupById(MyPrdCoup.MyPrdCoupId myPrdCoupId);//尋找單筆優惠券

	List<MyPrdCoup> getAllCoupons(); // 查詢所有優惠券記錄

	List<MyPrdCoup> findCouponsByMemberId(Integer memberId); // 查詢會員已領取的優惠券

	List<MyPrdCoup> findCouponsByMemberIdAndStatus(Integer memberId, Integer status); // 查詢會員已使用或未使用的優惠券

	boolean hasMemberReceivedCoupon(MyPrdCoup.MyPrdCoupId myPrdCoupId); // 檢查會員是否已領取特定優惠券

	List<MyPrdCoup> findCouponsExpiringSoon(Integer memberId, LocalDateTime currentDate, Integer days); // 查詢會員即將到期的優惠券

	boolean receiveCoupon(MyPrdCoup.MyPrdCoupId myPrdCoupId, LocalDateTime receiveDate); // 領取優惠券

	boolean useCoupon(MyPrdCoup.MyPrdCoupId myPrdCoupId, LocalDateTime useDate); // 使用優惠券

	List<MyPrdCoup> findValidCouponsForMember(Integer memberId, LocalDateTime currentDate); // 查詢會員有效的優惠券
}
