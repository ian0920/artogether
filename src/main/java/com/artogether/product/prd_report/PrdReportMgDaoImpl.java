package com.artogether.product.prd_report;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.List;

@Repository
public class PrdReportMgDaoImpl implements PrdReportMgDao {

	@Autowired
	EntityManagerFactory sessionFactory;
	
	@Override
	public int add(PrdReport prd_report) {
		Session session =  sessionFactory.unwrap(Session.class);
		try {
			session.beginTransaction();
			Integer id = (Integer) session.save( prd_report);
			session.getTransaction().commit();
			return id;
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		return -1;
	}
	
	@Override
	public int update(PrdReport prd_report) {
		Session session =  sessionFactory.unwrap(Session.class);
		try {
			session.beginTransaction();
			session.update(prd_report);
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
			PrdReport prd_report= session.get(PrdReport.class, id);
			if (prd_report != null) {
				session.delete(prd_report);
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
	public PrdReport findByPK(Integer id) {
		Session session = sessionFactory.unwrap(Session.class);
		try {
			session.beginTransaction();
			PrdReport prd_report = session.get(PrdReport.class, id);
			session.getTransaction().commit();
			return prd_report;
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		return null;
	}

	@Override
	public List<PrdReport> getAll() {
		Session session = sessionFactory.unwrap(Session.class);
		try {
			session.beginTransaction();
			List<PrdReport> list = session.createQuery("from PrdReport", PrdReport.class).list();
			session.getTransaction().commit();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		return null;
	}
	
	@Override
	public List<PrdReport> findByMemberId(Integer memberId) {
	    Session session = sessionFactory.unwrap(Session.class);
	    try {
	        session.beginTransaction();
	        String hql = "FROM PrdReport WHERE member.id = :memberId";
	        List<PrdReport> list = session.createQuery(hql, PrdReport.class)
	                                      .setParameter("memberId", memberId)
	                                      .list();
	        session.getTransaction().commit();
	        return list; // 返回符合條件的記錄
	    } catch (Exception e) {
	        e.printStackTrace();
	        session.getTransaction().rollback();
	    }
	    return null; // 發生錯誤返回 null
	}
	
	@Override
	public List<PrdReport> findByOrderId(Integer orderId) {
	    Session session = sessionFactory.unwrap(Session.class);
	    try {
	        session.beginTransaction();
	        String hql = "FROM PrdReport WHERE order.id = :orderId";
	        List<PrdReport> list = session.createQuery(hql, PrdReport.class)
	                                      .setParameter("orderId", orderId)
	                                      .list();
	        session.getTransaction().commit();
	        return list; // 返回符合條件的記錄
	    } catch (Exception e) {
	        e.printStackTrace();
	        session.getTransaction().rollback();
	    }
	    return null; // 發生錯誤返回 null
	}
	
	@Override
	public List<PrdReport> findByProductId(Integer prdId) {
	    Session session = sessionFactory.unwrap(Session.class);
	    try {
	        session.beginTransaction();
	        String hql = "FROM PrdReport WHERE product.id = :prdId";
	        List<PrdReport> list = session.createQuery(hql, PrdReport.class)
	                                      .setParameter("prdId", prdId)
	                                      .list();
	        session.getTransaction().commit();
	        return list; // 返回符合條件的記錄
	    } catch (Exception e) {
	        e.printStackTrace();
	        session.getTransaction().rollback();
	    }
	    return null; // 發生錯誤返回 null
	}
	
	@Override
	public List<PrdReport> findByStatus(Integer status) {
	    Session session = sessionFactory.unwrap(Session.class);
	    try {
	        session.beginTransaction();
	        String hql = "FROM PrdReport WHERE status = :status";
	        List<PrdReport> list = session.createQuery(hql, PrdReport.class)
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
	public int updateStatusBatch(List<Integer> reportIds, Integer status) {
	    Session session = sessionFactory.unwrap(Session.class);
	    try {
	        session.beginTransaction();
	        String hql = "UPDATE PrdReport SET status = :status WHERE id IN (:reportIds)";
	        int updatedRows = session.createQuery(hql)
	                                 .setParameter("status", status)
	                                 .setParameterList("reportIds", reportIds)
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
	public boolean hasMemberReportedProduct(Integer memberId, Integer prdId) {
	    Session session = sessionFactory.unwrap(Session.class);
	    try {
	        session.beginTransaction();
	        String hql = "SELECT COUNT(*) FROM PrdReport WHERE member.id = :memberId AND product.id = :prdId";
	        Long count = session.createQuery(hql, Long.class)
	                            .setParameter("memberId", memberId)
	                            .setParameter("prdId", prdId)
	                            .uniqueResult();
	        session.getTransaction().commit();
	        return count != null && count > 0; // 返回 true 表示已有檢舉
	    } catch (Exception e) {
	        e.printStackTrace();
	        session.getTransaction().rollback();
	    }
	    return false; // 發生錯誤返回 false
	}
	
	@Override
	public int deleteReportsBatch(List<Integer> reportIds) {
	    Session session = sessionFactory.unwrap(Session.class);
	    try {
	        session.beginTransaction();
	        String hql = "DELETE FROM PrdReport WHERE id IN (:reportIds)";
	        int deletedRows = session.createQuery(hql)
	                                 .setParameterList("reportIds", reportIds)
	                                 .executeUpdate();
	        session.getTransaction().commit();
	        return deletedRows; // 返回刪除的行數
	    } catch (Exception e) {
	        e.printStackTrace();
	        session.getTransaction().rollback();
	    }
	    return -1; // 發生錯誤返回 -1
	}


}
