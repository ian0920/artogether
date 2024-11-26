package com.artogether.product.my_prd_coup;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManagerFactory;
import java.util.List;

@Service
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


}
