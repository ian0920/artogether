package com.artogether.product.my_prd_coup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MyPrdCoupServiceImpl implements MyPrdCoupService {

    @Autowired
    private MyPrdCoupDao myPrdCoupDao;

    @Override
    public MyPrdCoup.MyPrdCoupId addMyPrdCoup(MyPrdCoup myPrdCoup) {
        try {
            return myPrdCoupDao.add(myPrdCoup);
        } catch (Exception e) {
            e.printStackTrace();
            return null; // 新增失敗返回 null
        }
    }

    @Override
    public MyPrdCoup.MyPrdCoupId updateMyPrdCoup(MyPrdCoup myPrdCoup) {
        try {
            return myPrdCoupDao.update(myPrdCoup);
        } catch (Exception e) {
            e.printStackTrace();
            return null; // 更新失敗返回 null
        }
    }

    @Override
    public boolean deleteMyPrdCoup(MyPrdCoup.MyPrdCoupId myPrdCoupId) {
        try {
            return myPrdCoupDao.delete(myPrdCoupId);
        } catch (Exception e) {
            e.printStackTrace();
            return false; // 刪除失敗返回 false
        }
    }

    @Override
    public MyPrdCoup findMyPrdCoupById(MyPrdCoup.MyPrdCoupId myPrdCoupId) {
        try {
            return myPrdCoupDao.findByPK(myPrdCoupId);
        } catch (Exception e) {
            e.printStackTrace();
            return null; // 查詢失敗返回 null
        }
    }

    @Override
    public List<MyPrdCoup> getAllCoupons() {
        try {
            return myPrdCoupDao.getAll();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // 查詢失敗返回空列表
        }
    }

    @Override
    public List<MyPrdCoup> findCouponsByMemberId(Integer memberId) {
        try {
            return myPrdCoupDao.findByMemberId(memberId);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // 查詢失敗返回空列表
        }
    }

    @Override
    public List<MyPrdCoup> findCouponsByMemberIdAndStatus(Integer memberId, Integer status) {
        try {
            return myPrdCoupDao.findByMemberIdAndStatus(memberId, status);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // 查詢失敗返回空列表
        }
    }

    @Override
    public boolean hasMemberReceivedCoupon(MyPrdCoup.MyPrdCoupId myPrdCoupId) {
        try {
            return myPrdCoupDao.hasMemberReceivedCoupon(myPrdCoupId);
        } catch (Exception e) {
            e.printStackTrace();
            return false; // 檢查失敗返回 false
        }
    }

//    @Override
//    public List<MyPrdCoup> findCouponsExpiringSoon(Integer memberId, LocalDateTime currentDate, Integer days) {
//        try {
//            return myPrdCoupDao.findCouponsExpiringSoonForMember(memberId, currentDate, days);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return List.of(); // 查詢失敗返回空列表
//        }
//    }

    @Override
    public boolean receiveCoupon(MyPrdCoup.MyPrdCoupId myPrdCoupId, Timestamp receiveDate) {
        try {
            return myPrdCoupDao.receiveCoupon(myPrdCoupId, receiveDate);
        } catch (Exception e) {
            e.printStackTrace();
            return false; // 領取失敗返回 false
        }
    }

    @Override
    public boolean useCoupon(MyPrdCoup.MyPrdCoupId myPrdCoupId, Timestamp useDate) {
        try {
            return myPrdCoupDao.useCoupon(myPrdCoupId, useDate);
        } catch (Exception e) {
            e.printStackTrace();
            return false; // 使用失敗返回 false
        }
    }

    @Override
    public List<MyPrdCoup> findValidCouponsForMember(Integer memberId, Timestamp currentDate) {
        try {
            return myPrdCoupDao.findValidCouponsForMember(memberId, currentDate);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // 查詢失敗返回空列表
        }
    }
}

