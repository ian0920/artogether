package com.artogether.product.prd_return;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
@Repository
public class ApplyPrdReturnDaoImpl implements ApplyPrdReturnDao {
	
	@Autowired
	EntityManagerFactory sessionFactory;
	
	@Override
	public int add(PrdReturn prdReturn) {
		Session session = sessionFactory.unwrap(Session.class);
		try {
			session.beginTransaction();
			Integer id = (Integer)session.save(prdReturn);
			session.getTransaction().commit();
			return id;
		}catch(Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		
		return -1;	
	}
	
	@Override
	public int update(PrdReturn prdReturn) {
		Session session = sessionFactory.unwrap(Session.class);
		try {
			session.beginTransaction();
			session.update(prdReturn);
			session.getTransaction().commit();
		}catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		
		return -1;
	}
	
	 @Override
	    public int delete(Integer id) {
	        Session session = sessionFactory.unwrap(Session.class);
	        try {
	            session.beginTransaction();
	            PrdReturn prdReturn = session.get(PrdReturn.class, id);
	            if (prdReturn != null) {
	                session.delete(prdReturn);
	                session.getTransaction().commit();
	                return 1; // 删除成功
	            }
	            session.getTransaction().rollback();
	        } catch (Exception e) {
	            e.printStackTrace();
	            session.getTransaction().rollback();
	        }
	        return -1; // 删除失敗
	    }

	    @Override
	    public PrdReturn findByPK(Integer id) {
	        Session session = sessionFactory.unwrap(Session.class);
	        try {
	            session.beginTransaction();
	            PrdReturn prdReturn = session.get(PrdReturn.class, id);
	            session.getTransaction().commit();
	            return prdReturn; // 返回找到的記錄
	        } catch (Exception e) {
	            e.printStackTrace();
	            session.getTransaction().rollback();
	        }
	        return null; // 查詢失敗
	    }

	    @Override
	    public List<PrdReturn> findByStatus(Integer status) {
	        Session session = sessionFactory.unwrap(Session.class);
	        try {
	            session.beginTransaction();
	            String hql = "FROM PrdReturn WHERE status = :status";
	            List<PrdReturn> list = session.createQuery(hql, PrdReturn.class)
	                                          .setParameter("status", status)
	                                          .list();
	            session.getTransaction().commit();
	            return list; // 返回符合條件的記錄
	        } catch (Exception e) {
	            e.printStackTrace();
	            session.getTransaction().rollback();
	        }
	        return List.of(); // 返回空列表
	    }

	    @Override
	    public List<PrdReturn> findByApplyDateRange(LocalDateTime startDate, LocalDateTime endDate) {
	        Session session = sessionFactory.unwrap(Session.class);
	        try {
	            session.beginTransaction();
	            String hql = "FROM PrdReturn WHERE applyDate BETWEEN :startDate AND :endDate";
	            List<PrdReturn> list = session.createQuery(hql, PrdReturn.class)
	                                          .setParameter("startDate", startDate)
	                                          .setParameter("endDate", endDate)
	                                          .list();
	            session.getTransaction().commit();
	            return list; // 返回符合時間範圍的記錄
	        } catch (Exception e) {
	            e.printStackTrace();
	            session.getTransaction().rollback();
	        }
	        return List.of(); // 返回空列表
	    }

	    @Override
	    public List<PrdReturn> findByType(Integer type) {
	        Session session = sessionFactory.unwrap(Session.class);
	        try {
	            session.beginTransaction();
	            String hql = "FROM PrdReturn WHERE type = :type";
	            List<PrdReturn> list = session.createQuery(hql, PrdReturn.class)
	                                          .setParameter("type", type)
	                                          .list();
	            session.getTransaction().commit();
	            return list; // 返回符合類型的記錄
	        } catch (Exception e) {
	            e.printStackTrace();
	            session.getTransaction().rollback();
	        }
	        return List.of(); // 返回空列表
	    }

	    @Override
	    public List<PrdReturn> findAllOrderByReturnDateDesc() {
	        Session session = sessionFactory.unwrap(Session.class);
	        try {
	            session.beginTransaction();
	            String hql = "FROM PrdReturn ORDER BY returnDate DESC";
	            List<PrdReturn> list = session.createQuery(hql, PrdReturn.class).list();
	            session.getTransaction().commit();
	            return list; // 返回按退/換貨日期降序排列的記錄
	        } catch (Exception e) {
	            e.printStackTrace();
	            session.getTransaction().rollback();
	        }
	        return List.of(); // 返回空列表
	    }

}
