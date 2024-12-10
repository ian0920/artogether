package com.artogether.product.prd_catalog;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.List;


@Repository
public class PrdCatalogDaoImpl implements PrdCatalogDao {

	@Autowired
	EntityManagerFactory sessionFactory;

	@Autowired
	public PrdCatalogRepository prdCatalogRepository;
	
	@Override
	public int add(PrdCatalog prdCatalog) {
		Session session = sessionFactory.unwrap(Session.class);
		try {
			session.beginTransaction();
			Integer id = (Integer) session.save(prdCatalog);
			session.getTransaction().commit();
			return id;
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		return -1;
	}
	
	@Override
	public int update(PrdCatalog prdCatalog) {
		Session session = sessionFactory.unwrap(Session.class);
		try {
			session.beginTransaction();
			session.update(prdCatalog);
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
			PrdCatalog prdCatalog= session.get(PrdCatalog.class, id);
			if (prdCatalog != null) {
				session.delete(prdCatalog);
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
	public PrdCatalog findByPK(Integer id) {
		Session session = sessionFactory.unwrap(Session.class);
		try {
			session.beginTransaction();
			PrdCatalog prdCatalog = session.get(PrdCatalog.class, id);
			session.getTransaction().commit();
			return prdCatalog;
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		return null;
	}

	@Override
	public List<PrdCatalog> getAll() {
		Session session = sessionFactory.unwrap(Session.class);
		try {
			session.beginTransaction();
			List<PrdCatalog> list = session.createQuery("from PrdCatalog ", PrdCatalog.class).list();
			session.getTransaction().commit();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		return null;
	}

}
