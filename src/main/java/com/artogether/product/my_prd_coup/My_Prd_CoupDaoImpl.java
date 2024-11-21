package com.artogether.product.my_prd_coup;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManagerFactory;
import java.util.List;

@Service
public class My_Prd_CoupDaoImpl implements My_Prd_CoupDao {

	@Autowired
	EntityManagerFactory sessionFactory;
	
	@Override
	public My_Prd_Coup.CompositeCoup add(My_Prd_Coup my_prd_coup) {
	    Session session = sessionFactory.unwrap(Session.class);
	    try {
	        session.beginTransaction();
	        session.save(my_prd_coup); 
	        session.getTransaction().commit();
	        return new My_Prd_Coup.CompositeCoup(
	            my_prd_coup.getMember().getId(),
	            my_prd_coup.getPrd_coup().getId()
	        ); // 返回複合主鍵
	    } catch (Exception e) {
	        e.printStackTrace();
	        session.getTransaction().rollback();
	    }
	    return null; // 錯誤情況返回 null
	}

	
	@Override
	public My_Prd_Coup.CompositeCoup update(My_Prd_Coup my_prd_coup) {
	    Session session = sessionFactory.unwrap(Session.class);
	    try {
	        session.beginTransaction();
	        session.update(my_prd_coup);
	        session.getTransaction().commit();
	        return new My_Prd_Coup.CompositeCoup(
	            my_prd_coup.getMember().getId(),
	            my_prd_coup.getPrd_coup().getId()
	        ); // 返回複合主鍵
	    } catch (Exception e) {
	        e.printStackTrace();
	        session.getTransaction().rollback();
	    }
	    return null; // 錯誤情況返回 null
	}

	
	@Override
	public boolean delete(My_Prd_Coup.CompositeCoup member_prd_coup_id) {
	    Session session = sessionFactory.unwrap(Session.class);
	    try {
	        session.beginTransaction();
	        My_Prd_Coup my_prd_coup = session.get(My_Prd_Coup.class, member_prd_coup_id);
	        if (my_prd_coup != null) { // 檢查對象是否存在
	            session.delete(my_prd_coup);
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
	public My_Prd_Coup findByPK(My_Prd_Coup.CompositeCoup member_prd_coup_id) {
	    Session session = sessionFactory.unwrap(Session.class);
	    try {
	        session.beginTransaction();
	        My_Prd_Coup my_prd_coup = session.get(My_Prd_Coup.class, member_prd_coup_id);
	        session.getTransaction().commit();
	        return my_prd_coup; // 查詢成功返回對象
	    } catch (Exception e) {
	        e.printStackTrace();
	        session.getTransaction().rollback();
	    }
	    return null; // 錯誤情況返回 null
	}


	@Override
	public List<My_Prd_Coup> getAll() {
	    Session session = sessionFactory.unwrap(Session.class);
	    try {
	        session.beginTransaction();
	        List<My_Prd_Coup> list = session.createQuery("from My_Prd_Coup", My_Prd_Coup.class).list();
	        session.getTransaction().commit();
	        return list; // 查詢成功返回列表
	    } catch (Exception e) {
	        e.printStackTrace();
	        session.getTransaction().rollback();
	    }
	    return List.of(); // 錯誤情況返回空列表
	}


}
