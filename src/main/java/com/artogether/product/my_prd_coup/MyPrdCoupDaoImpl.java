package com.artogether.product.my_prd_coup;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


import com.artogether.common.member.Member;
import com.artogether.product.prd_coup.PrdCoup;

import javax.persistence.EntityManagerFactory;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class MyPrdCoupDaoImpl implements MyPrdCoupDao {

	@Autowired
	EntityManagerFactory sessionFactory;
	
	@Override
	public MyPrdCoup.MyPrdCoupId add(MyPrdCoup myPrdCoup) {
	    Session session = sessionFactory.unwrap(Session.class);
	    try {
	        session.beginTransaction();
	        session.save(myPrdCoup);
	        session.getTransaction().commit();
	        return new MyPrdCoup.MyPrdCoupId(
	            myPrdCoup.getMember().getId(),
	            myPrdCoup.getPrdCoup().getId()
	        ); // 返回複合主鍵
	    } catch (Exception e) {
	        e.printStackTrace();
	        session.getTransaction().rollback();
	    }
	    return null; // 錯誤情況返回 null
	}

	
	@Override
	public MyPrdCoup.MyPrdCoupId update(MyPrdCoup myPrdCoup) {
	    Session session = sessionFactory.unwrap(Session.class);
	    try {
	        session.beginTransaction();
	        session.update(myPrdCoup);
	        session.getTransaction().commit();
	        return new MyPrdCoup.MyPrdCoupId(
	            myPrdCoup.getMember().getId(),
	            myPrdCoup.getPrdCoup().getId()
	        ); // 返回複合主鍵
	    } catch (Exception e) {
	        e.printStackTrace();
	        session.getTransaction().rollback();
	    }
	    return null; // 錯誤情況返回 null
	}

	
	@Override
	public boolean delete(MyPrdCoup.MyPrdCoupId memberPrdCoupId) {
	    Session session = sessionFactory.unwrap(Session.class);
	    try {
	        session.beginTransaction();
	        MyPrdCoup myPrdCoup = session.get(MyPrdCoup.class, memberPrdCoupId);
	        if (myPrdCoup != null) { // 檢查對象是否存在
	            session.delete(myPrdCoup);
	            session.getTransaction().commit();
	            return true; // 刪除成功
	        }
	        session.getTransaction().rollback();
	    } catch (Exception e) {
	        e.printStackTrace();
	        session.getTransaction().rollback();
	    }
	    return false; // 錯誤或對象不存在返回 false
	}


	@Override
	public MyPrdCoup findByPK(MyPrdCoup.MyPrdCoupId myPrdCoupId) {
	    Session session = sessionFactory.unwrap(Session.class);
	    try {
	        session.beginTransaction();
	        MyPrdCoup myPrdCoup = session.get(MyPrdCoup.class, myPrdCoupId);
	        session.getTransaction().commit();
	        return myPrdCoup; // 查詢成功返回對象
	    } catch (Exception e) {
	        e.printStackTrace();
	        session.getTransaction().rollback();
	    }
	    return null; // 錯誤情況返回 null
	}


	@Override
	public List<MyPrdCoup> getAll() {
	    Session session = sessionFactory.unwrap(Session.class);
	    try {
	        session.beginTransaction();
	        List<MyPrdCoup> list = session.createQuery("from MyPrdCoup ", MyPrdCoup.class).list();
	        session.getTransaction().commit();
	        return list; // 查詢成功返回列表
	    } catch (Exception e) {
	        e.printStackTrace();
	        session.getTransaction().rollback();
	    }
	    return List.of(); // 錯誤情況返回空列表
	}

	@Override
	public List<MyPrdCoup> findByMemberId(Integer memberId) {
	    Session session = sessionFactory.unwrap(Session.class);
	    try {
	        session.beginTransaction();
	        String hql = "FROM MyPrdCoup m WHERE m.member.id = :memberId";
	        List<MyPrdCoup> list = session.createQuery(hql, MyPrdCoup.class)
	                                      .setParameter("memberId", memberId)
	                                      .list();
	        session.getTransaction().commit();
	        return list; // 返回會員的優惠券
	    } catch (Exception e) {
	        e.printStackTrace();
	        session.getTransaction().rollback();
	    }
	    return List.of(); // 錯誤情況返回空列表
	}

	@Override
	public List<MyPrdCoup> findByMemberIdAndStatus(Integer memberId, Integer status) {
	    Session session = sessionFactory.unwrap(Session.class);
	    try {
	        session.beginTransaction();
	        String hql = "FROM MyPrdCoup m WHERE m.member.id = :memberId AND m.status = :status";
	        List<MyPrdCoup> list = session.createQuery(hql, MyPrdCoup.class)
	                                      .setParameter("memberId", memberId)
	                                      .setParameter("status", status)
	                                      .list();
	        session.getTransaction().commit();
	        return list; // 返回符合條件的優惠券
	    } catch (Exception e) {
	        e.printStackTrace();
	        session.getTransaction().rollback();
	    }
	    return List.of(); // 錯誤情況返回空列表
	}

	
	@Override
	public boolean hasMemberReceivedCoupon(MyPrdCoup.MyPrdCoupId myPrdCoupId) {
	    Session session = sessionFactory.unwrap(Session.class);
	    try {
	        session.beginTransaction();
	        MyPrdCoup myPrdCoup = session.get(MyPrdCoup.class, myPrdCoupId);
	        session.getTransaction().commit();
	        return myPrdCoup != null; // 如果存在，返回 true
	    } catch (Exception e) {
	        e.printStackTrace();
	        session.getTransaction().rollback();
	    }
	    return false; // 錯誤情況返回 false
	}

//	@Override
//	public List<MyPrdCoup> findCouponsExpiringSoonForMember(Integer memberId, Timestamp currentDate, Integer days) {
//	    Session session = sessionFactory.unwrap(Session.class);
//	    try {
//	        session.beginTransaction();
//	        String hql = "FROM MyPrdCoup m WHERE m.member.id = :memberId " +
//	                     "AND m.prdCoup.endDate BETWEEN :currentDate AND :expiryDate";
//	        List<MyPrdCoup> list = session.createQuery(hql, MyPrdCoup.class)
//	                                      .setParameter("memberId", memberId)
//	                                      .setParameter("currentDate", currentDate)
//	                                      .setParameter("expiryDate", currentDate.plusDays(days))
//	                                      .list();
//	        session.getTransaction().commit();
//	        return list; // 返回即將到期的優惠券
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	        session.getTransaction().rollback();
//	    }
//	    return List.of(); // 錯誤情況返回空列表
//	}

	@Override
	public boolean receiveCoupon(MyPrdCoup.MyPrdCoupId myPrdCoupId, Timestamp receiveDate) {
	    // 檢查輸入參數是否為空
	    if (myPrdCoupId == null || receiveDate == null) {
	        throw new IllegalArgumentException("myPrdCoupId and receiveDate must not be null");
	    }

	    Session session = null;
	    try {
	        // 取得 Session 並開啟交易
	        session = sessionFactory.unwrap(Session.class);
	        session.beginTransaction();

	        // 確認是否已有相同的紀錄
	        MyPrdCoup existingRecord = session.get(MyPrdCoup.class, myPrdCoupId);
	        if (existingRecord != null) {
	            // 如果紀錄已存在，直接返回 false 或根據需求進行更新
	            return false;
	        }

	        // 初始化 MyPrdCoup 實體
	        MyPrdCoup myPrdCoup = new MyPrdCoup();
	        
	        // 設置 Member 和 PrdCoup
	        Member member = session.get(Member.class, myPrdCoupId.getMember());
	        PrdCoup prdCoup = session.get(PrdCoup.class, myPrdCoupId.getPrdCoup());

	        if (member == null || prdCoup == null) {
	            throw new IllegalArgumentException("Invalid member or prdCoup ID");
	        }

	        myPrdCoup.setMember(member);
	        myPrdCoup.setPrdCoup(prdCoup);

	        // 設置領取日期和狀態
	        myPrdCoup.setReceiveDate(receiveDate);
	        myPrdCoup.setStatus(0); // 0 表示未使用

	        // 保存新紀錄
	        session.save(myPrdCoup);
	        session.getTransaction().commit();
	        return true; // 領取成功
	    } catch (Exception e) {
	        if (session != null) {
	            session.getTransaction().rollback(); // 發生例外時回滾交易
	        }
	        e.printStackTrace();
	    } finally {
	        if (session != null) {
	            session.close(); // 確保釋放資源
	        }
	    }
	    return false; // 出現錯誤返回 false
	}


	@Override
	public boolean useCoupon(MyPrdCoup.MyPrdCoupId myPrdCoupId, Timestamp useDate) {
	    Session session = sessionFactory.unwrap(Session.class);
	    try {
	        session.beginTransaction();
	        MyPrdCoup myPrdCoup = session.get(MyPrdCoup.class, myPrdCoupId);
	        if (myPrdCoup != null && myPrdCoup.getStatus() == 0) { // 未使用狀態才可更新
	            myPrdCoup.setUseDate(useDate);
	            myPrdCoup.setStatus(1); // 更新為已使用狀態
	            session.update(myPrdCoup);
	            session.getTransaction().commit();
	            return true; // 更新成功
	        }
	        session.getTransaction().rollback();
	    } catch (Exception e) {
	        e.printStackTrace();
	        session.getTransaction().rollback();
	    }
	    return false; // 錯誤或狀態不符返回 false
	}

	@Override
	public List<MyPrdCoup> findValidCouponsForMember(Integer memberId, Timestamp currentDate) {
	    Session session = sessionFactory.unwrap(Session.class);
	    try {
	        session.beginTransaction();
	        String hql = "FROM MyPrdCoup m WHERE m.member.id = :memberId " +
	                     "AND m.status = 0 " +
	                     "AND m.prdCoup.endDate >= :currentDate";
	        List<MyPrdCoup> list = session.createQuery(hql, MyPrdCoup.class)
	                                      .setParameter("memberId", memberId)
	                                      .setParameter("currentDate", currentDate)
	                                      .list();
	        session.getTransaction().commit();
	        return list; // 返回有效優惠券
	    } catch (Exception e) {
	        e.printStackTrace();
	        session.getTransaction().rollback();
	    }
	    return List.of(); // 錯誤情況返回空列表
	}


}
