package com.artogether.product.prd_report;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.List;

@Repository
public class PrdReportDaoImpl implements PrdReportDao {

	@Autowired
	EntityManagerFactory sessionFactory;
	
	@Override
	public int add(PrdReport prdReport) {
		Session session =  sessionFactory.unwrap(Session.class);
		try {
			session.beginTransaction();
			Integer id = (Integer) session.save( prdReport);
			session.getTransaction().commit();
			return id;
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		return -1;
	}
	
	@Override
	public int update(PrdReport prdReport) {
		Session session =  sessionFactory.unwrap(Session.class);
		try {
			session.beginTransaction();
			session.update(prdReport);
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
			PrdReport prdReport= session.get(PrdReport.class, id);
			if (prdReport != null) {
				session.delete(prdReport);
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
			PrdReport prdReport = session.get(PrdReport.class, id);
			session.getTransaction().commit();
			return prdReport;
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
	public int submitReport(PrdReport prdReport) {
	    Session session = sessionFactory.unwrap(Session.class);
	    try {
	        session.beginTransaction();
	        Integer id = (Integer) session.save(prdReport); // 保存檢舉記錄
	        session.getTransaction().commit();
	        return id; // 返回生成的檢舉 ID
	    } catch (Exception e) {
	        e.printStackTrace();
	        session.getTransaction().rollback();
	    }
	    return -1; // 發生錯誤返回 -1
	}

	@Override
	public List<PrdReport> findReportsByMemberId(Integer memberId) {
	    Session session = sessionFactory.unwrap(Session.class);
	    try {
	        session.beginTransaction();
	        String hql = "FROM PrdReport WHERE member.id = :memberId";
	        List<PrdReport> list = session.createQuery(hql, PrdReport.class)
	                                      .setParameter("memberId", memberId)
	                                      .list(); // 查詢該會員的檢舉記錄
	        session.getTransaction().commit();
	        return list; // 返回檢舉記錄列表
	    } catch (Exception e) {
	        e.printStackTrace();
	        session.getTransaction().rollback();
	    }
	    return null; // 發生錯誤返回 null
	}

	@Override
	public boolean hasReportedProduct(Integer memberId, Integer prdId) {
	    Session session = sessionFactory.unwrap(Session.class);
	    try {
	        session.beginTransaction();
	        String hql = "SELECT COUNT(*) FROM PrdReport WHERE member.id = :memberId AND product.id = :prdId";
	        Long count = session.createQuery(hql, Long.class)
	                            .setParameter("memberId", memberId)
	                            .setParameter("prdId", prdId)
	                            .uniqueResult(); // 查詢記錄數量
	        session.getTransaction().commit();
	        return count != null && count > 0; // 如果有記錄，返回 true
	    } catch (Exception e) {
	        e.printStackTrace();
	        session.getTransaction().rollback();
	    }
	    return false; // 發生錯誤返回 false
	}

	
}
