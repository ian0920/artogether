package com.artogether.product.prd_report;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.List;

@Repository
public class Prd_ReportDaoImpl implements Prd_ReportDao {

	@Autowired
	EntityManagerFactory sessionFactory;
	
	@Override
	public int add(Prd_Report prd_report) {
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
	public int update(Prd_Report prd_report) {
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
			Prd_Report prd_report= session.get(Prd_Report.class, id);
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
	public Prd_Report findByPK(Integer id) {
		Session session = sessionFactory.unwrap(Session.class);
		try {
			session.beginTransaction();
			Prd_Report prd_report = session.get(Prd_Report.class, id);
			session.getTransaction().commit();
			return prd_report;
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		return null;
	}

	@Override
	public List<Prd_Report> getAll() {
		Session session = sessionFactory.unwrap(Session.class);
		try {
			session.beginTransaction();
			List<Prd_Report> list = session.createQuery("from Prd_Report", Prd_Report.class).list();
			session.getTransaction().commit();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		return null;
	}

}
