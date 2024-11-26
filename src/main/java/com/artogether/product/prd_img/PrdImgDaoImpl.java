package com.artogether.product.prd_img;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.List;

@Repository
public class PrdImgDaoImpl implements PrdImgDao {

	@Autowired
	EntityManagerFactory sessionFactory;


	@Override
	public int add(PrdImg prd_img) {
		Session session = sessionFactory.unwrap(Session.class);
		try {
			session.beginTransaction();
			Integer id = (Integer) session.save(prd_img);
			session.getTransaction().commit();
			return id;
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		return -1;
	}
	
	@Override
	public int update(PrdImg prd_img) {
		Session session = sessionFactory.unwrap(Session.class);
		try {
			session.beginTransaction();
			session.update(prd_img);
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
			PrdImg prd_img= session.get(PrdImg.class, id);
			if (prd_img != null) {
				session.delete(prd_img);
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
	public PrdImg findByPK(Integer id) {
		Session session = sessionFactory.unwrap(Session.class);
		try {
			session.beginTransaction();
			PrdImg prd_img = session.get(PrdImg.class, id);
			session.getTransaction().commit();
			return prd_img;
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		return null;
	}

	@Override
	public List<PrdImg> getAll() {
		Session session = sessionFactory.unwrap(Session.class);
		try {
			session.beginTransaction();
			List<PrdImg> list = session.createQuery("from PrdImg", PrdImg.class).list();
			session.getTransaction().commit();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		return null;
	}
	

}
