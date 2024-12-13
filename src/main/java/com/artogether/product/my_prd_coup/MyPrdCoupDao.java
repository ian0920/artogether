package com.artogether.product.my_prd_coup;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public interface MyPrdCoupDao {

	MyPrdCoup.MyPrdCoupId add(MyPrdCoup myPrdCoup);

	MyPrdCoup.MyPrdCoupId update(MyPrdCoup myPrdCoup);

	boolean delete(MyPrdCoup.MyPrdCoupId myPrdCoupId);

	MyPrdCoup findByPK(MyPrdCoup.MyPrdCoupId myPrdCoupId);

	List<MyPrdCoup> getAll();

	List<MyPrdCoup> findByMemberId(Integer memberId);// 依會員查詢已領取的優惠券

	List<MyPrdCoup> findByMemberIdAndStatus(Integer memberId, Integer status);// 查詢會員已使用或未使用的優惠券

	boolean hasMemberReceivedCoupon(MyPrdCoup.MyPrdCoupId myPrdCoupId);// 檢查會員是否已領取特定優惠券

//	List<MyPrdCoup> findCouponsExpiringSoonForMember(Integer memberId, Timestamp currentDate, Integer days);// 查詢即將到期的優惠券

	boolean receiveCoupon(MyPrdCoup.MyPrdCoupId myPrdCoupId, Timestamp receiveDate);// 新增領取優惠券功能

	boolean useCoupon(MyPrdCoup.MyPrdCoupId myPrdCoupId, Timestamp useDate);// 更新優惠券的使用狀態

	List<MyPrdCoup> findValidCouponsForMember(Integer memberId, Timestamp currentDate);// 查詢會員有效的優惠券

}
