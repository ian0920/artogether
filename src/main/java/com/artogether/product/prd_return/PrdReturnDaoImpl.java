package com.artogether.product.prd_return;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class PrdReturnDaoImpl implements PrdReturnDao {

	@Autowired
	EntityManagerFactory sessionFactory;
	
	@Override
	public int add(PrdReturn prdReturn) {
		Session session = sessionFactory.unwrap(Session.class);
		try {
			session.beginTransaction();
			Integer id = (Integer) session.save(prdReturn);
			session.getTransaction().commit();
			return id;
		} catch (Exception e) {
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
			return 1;
		} catch (Exception e) {
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
			PrdReturn prdReturn= session.get(PrdReturn.class, id);
			if (prdReturn != null) {
				session.delete(prdReturn);
			}
			session.getTransaction().commit();
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		return -1;
	}

	@Override
	public PrdReturn findByPK(Integer id) {
		Session session = sessionFactory.unwrap(Session.class);
		try {
			session.beginTransaction();
			PrdReturn prdReturn = session.get(PrdReturn.class, id);
			session.getTransaction().commit();
			return prdReturn;
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		return null;
	}

	@Override
	public List<PrdReturn> getAll() {
		Session session = sessionFactory.unwrap(Session.class);
		try {
			session.beginTransaction();
			List<PrdReturn> list = session.createQuery("from PrdReturn", PrdReturn.class).list();
			session.getTransaction().commit();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		return null;
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
	        return list; // 返回符合狀態的記錄
	    } catch (Exception e) {
	        e.printStackTrace();
	        session.getTransaction().rollback();
	    }
	    return null; // 發生錯誤返回 null
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
	        return list; // 返回時間範圍內的記錄
	    } catch (Exception e) {
	        e.printStackTrace();
	        session.getTransaction().rollback();
	    }
	    return null; // 發生錯誤返回 null
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
	    return null; // 發生錯誤返回 null
	}
	
	@Override
	public List<PrdReturn> findAllOrderByApplyDateDesc() {
	    Session session = sessionFactory.unwrap(Session.class);
	    try {
	        session.beginTransaction();
	        String hql = "FROM PrdReturn ORDER BY applyDate DESC";
	        List<PrdReturn> list = session.createQuery(hql, PrdReturn.class).list();
	        session.getTransaction().commit();
	        return list; // 返回按申請日期降序排列的記錄
	    } catch (Exception e) {
	        e.printStackTrace();
	        session.getTransaction().rollback();
	    }
	    return null; // 發生錯誤返回 null
	}

	@Override
	public List<PrdReturn> findAllOrderByReturnDateDesc() {
	    Session session = sessionFactory.unwrap(Session.class);
	    try {
	        session.beginTransaction();
	        String hql = "FROM PrdReturn ORDER BY returnDate DESC";
	        List<PrdReturn> list = session.createQuery(hql, PrdReturn.class).list();
	        session.getTransaction().commit();
	        return list; // 返回按退換貨日期降序排列的記錄
	    } catch (Exception e) {
	        e.printStackTrace();
	        session.getTransaction().rollback();
	    }
	    return null; // 發生錯誤返回 null
	}
	
	@Override
	public int updateStatus(Integer id, Integer status) {
	    Session session = sessionFactory.unwrap(Session.class);
	    try {
	        session.beginTransaction();
	        String hql = "UPDATE PrdReturn SET status = :status WHERE id = :id";
	        int updatedRows = session.createQuery(hql)
	                                 .setParameter("status", status)
	                                 .setParameter("id", id)
	                                 .executeUpdate();
	        session.getTransaction().commit();
	        return updatedRows; // 返回更新的行數
	    } catch (Exception e) {
	        e.printStackTrace();
	        session.getTransaction().rollback();
	    }
	    return -1; // 發生錯誤返回 -1
	}
	
	@Override
	public int batchUpdateStatus(List<Integer> ids, Integer status) {
	    Session session = sessionFactory.unwrap(Session.class);
	    try {
	        session.beginTransaction();
	        String hql = "UPDATE PrdReturn SET status = :status WHERE id IN (:ids)";
	        int updatedRows = session.createQuery(hql)
	                                 .setParameter("status", status)
	                                 .setParameterList("ids", ids)
	                                 .executeUpdate();
	        session.getTransaction().commit();
	        return updatedRows; // 返回更新的行數
	    } catch (Exception e) {
	        e.printStackTrace();
	        session.getTransaction().rollback();
	    }
	    return -1; // 發生錯誤返回 -1
	}

	@Override
	public List<PrdReturn> findExpiredReturns(LocalDateTime currentDate, Integer days) {
	    Session session = sessionFactory.unwrap(Session.class);
	    try {
	        session.beginTransaction();
	        String hql = "FROM PrdReturn WHERE returnDate <= :expiryDate";
	        LocalDateTime expiryDate = currentDate.plusDays(days);
	        List<PrdReturn> list = session.createQuery(hql, PrdReturn.class)
	                                      .setParameter("expiryDate", expiryDate)
	                                      .list();
	        session.getTransaction().commit();
	        return list; // 返回即將過期的記錄
	    } catch (Exception e) {
	        e.printStackTrace();
	        session.getTransaction().rollback();
	    }
	    return null; // 發生錯誤返回 null
	}



}
