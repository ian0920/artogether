package com.artogether.product.prd_coup;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class PrdCoupDaoImpl implements PrdCoupDao {

	@Autowired
	EntityManagerFactory sessionFactory;
	
	@Override
	public int add(PrdCoup prdCoup) {
		Session session = sessionFactory.unwrap(Session.class);
		try {
			session.beginTransaction();
			Integer id = (Integer) session.save(prdCoup);
			session.getTransaction().commit();
			return id;
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		return -1;
	}
	
	@Override
	public int update(PrdCoup prdCoup) {
		Session session = sessionFactory.unwrap(Session.class);
		try {
			session.beginTransaction();
			session.update(prdCoup);
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
			PrdCoup prdCoup= session.get(PrdCoup.class, id);
			if (prdCoup != null) {
				session.delete(prdCoup);
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
	public PrdCoup findByPK(Integer id) {
		Session session = sessionFactory.unwrap(Session.class);
		try {
			session.beginTransaction();
			PrdCoup prdCoup = session.get(PrdCoup.class, id);
			session.getTransaction().commit();
			return prdCoup;
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		return null;
	}

	@Override
	public List<PrdCoup> getAll() {
		Session session = sessionFactory.unwrap(Session.class);
		try {
			session.beginTransaction();
			List<PrdCoup> list = session.createQuery("from PrdCoup", PrdCoup.class).list();
			session.getTransaction().commit();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		return null;
	}

	@Override
	public List<PrdCoup> findByBusinessId(Integer businessId) {
	    Session session = sessionFactory.unwrap(Session.class);
	    try {
	        session.beginTransaction();
	        String hql = "FROM PrdCoup WHERE businessId = :businessId";
	        List<PrdCoup> list = session.createQuery(hql, PrdCoup.class)
	                                    .setParameter("businessId", businessId)
	                                    .list();
	        session.getTransaction().commit();
	        return list;
	    } catch (Exception e) {
	        e.printStackTrace();
	        session.getTransaction().rollback();
	    }
	    return null;
	}

	@Override
	public List<PrdCoup> findValidCoupons(LocalDateTime now) {
	    Session session = sessionFactory.unwrap(Session.class);
	    try {
	        session.beginTransaction();
	        String hql = "FROM PrdCoup WHERE startDate <= :now AND endDate >= :now AND status = 1";
	        List<PrdCoup> list = session.createQuery(hql, PrdCoup.class)
	                                    .setParameter("now", now)
	                                    .list();
	        session.getTransaction().commit();
	        return list;
	    } catch (Exception e) {
	        e.printStackTrace();
	        session.getTransaction().rollback();
	    }
	    return null;
	}

	@Override
	public List<PrdCoup> findCouponsByCriteria(String name, Integer type, Integer status, Integer threshold) {
	    Session session = sessionFactory.unwrap(Session.class);
	    try {
	        session.beginTransaction();
	        String hql = "FROM PrdCoup WHERE (:name IS NULL OR name LIKE :name) " +
	                     "AND (:type IS NULL OR type = :type) " +
	                     "AND (:status IS NULL OR status = :status) " +
	                     "AND (:threshold IS NULL OR threshold >= :threshold)";
	        List<PrdCoup> list = session.createQuery(hql, PrdCoup.class)
	                                    .setParameter("name", name != null ? "%" + name + "%" : null)
	                                    .setParameter("type", type)
	                                    .setParameter("status", status)
	                                    .setParameter("threshold", threshold)
	                                    .list();
	        session.getTransaction().commit();
	        return list;
	    } catch (Exception e) {
	        e.printStackTrace();
	        session.getTransaction().rollback();
	    }
	    return null;
	}

	@Override
	public List<PrdCoup> findCouponsExpiringSoon(LocalDateTime now, Integer days) {
	    Session session = sessionFactory.unwrap(Session.class);
	    try {
	        session.beginTransaction();
	        String hql = "FROM PrdCoup WHERE endDate BETWEEN :now AND :end";
	        List<PrdCoup> list = session.createQuery(hql, PrdCoup.class)
	                                    .setParameter("now", now)
	                                    .setParameter("end", now.plusDays(days))
	                                    .list();
	        session.getTransaction().commit();
	        return list;
	    } catch (Exception e) {
	        e.printStackTrace();
	        session.getTransaction().rollback();
	    }
	    return null;
	}
	
	@Override
	public List<PrdCoup> findCouponsByStatus(Integer status) {
	    Session session = sessionFactory.unwrap(Session.class);
	    try {
	        session.beginTransaction();
	        String hql = "FROM PrdCoup WHERE status = :status";
	        List<PrdCoup> list = session.createQuery(hql, PrdCoup.class)
	                                    .setParameter("status", status)
	                                    .list();
	        session.getTransaction().commit();
	        return list; // 返回符合狀態的優惠券列表
	    } catch (Exception e) {
	        e.printStackTrace();
	        session.getTransaction().rollback();
	    }
	    return null; // 發生錯誤時返回 null
	}
	
	@Override
	public List<PrdCoup> findCouponsByType(Integer type) {
	    Session session = sessionFactory.unwrap(Session.class);
	    try {
	        session.beginTransaction();
	        String hql = "FROM PrdCoup WHERE type = :type";
	        List<PrdCoup> list = session.createQuery(hql, PrdCoup.class)
	                                    .setParameter("type", type)
	                                    .list();
	        session.getTransaction().commit();
	        return list; // 返回符合類型的優惠券列表
	    } catch (Exception e) {
	        e.printStackTrace();
	        session.getTransaction().rollback();
	    }
	    return null; // 發生錯誤時返回 null
	}

	@Override
	public List<PrdCoup> findCouponsByMultipleCriteria(Integer status, Integer type, Integer threshold) {
	    Session session = sessionFactory.unwrap(Session.class);
	    try {
	        session.beginTransaction();
	        String hql = "FROM PrdCoup WHERE (:status IS NULL OR status = :status) " +
	                     "AND (:type IS NULL OR type = :type) " +
	                     "AND (:threshold IS NULL OR threshold <= :threshold)";
	        List<PrdCoup> list = session.createQuery(hql, PrdCoup.class)
	                                    .setParameter("status", status)
	                                    .setParameter("type", type)
	                                    .setParameter("threshold", threshold)
	                                    .list();
	        session.getTransaction().commit();
	        return list; // 返回符合條件的優惠券列表
	    } catch (Exception e) {
	        e.printStackTrace();
	        session.getTransaction().rollback();
	    }
	    return null; // 發生錯誤時返回 null
	}
	
	@Override
	public List<PrdCoup> findActiveCoupons(LocalDateTime currentDate) {
	    Session session = sessionFactory.unwrap(Session.class);
	    try {
	        session.beginTransaction();
	        String hql = "FROM PrdCoup WHERE startDate <= :currentDate AND endDate >= :currentDate AND status = 1";
	        List<PrdCoup> list = session.createQuery(hql, PrdCoup.class)
	                                    .setParameter("currentDate", currentDate)
	                                    .list();
	        session.getTransaction().commit();
	        return list; // 返回目前有效的優惠券列表
	    } catch (Exception e) {
	        e.printStackTrace();
	        session.getTransaction().rollback();
	    }
	    return null; // 發生錯誤時返回 null
	}

	

}
