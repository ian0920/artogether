package com.artogether.product.product;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.List;


@Repository
public class ProductDaoImpl implements ProductDao {

	@Autowired
	EntityManagerFactory sessionFactory;
	
	@Override
	public int add( Product  product) {
		Session session = sessionFactory.unwrap(Session.class);
		try{
			session.beginTransaction();
			Integer id = (Integer) session.save(product);
			session.getTransaction().commit();
			return id;
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		return -1;
	}
	
	@Override
	public int update(Product product) {
		Session session = sessionFactory.unwrap(Session.class);
		try {
			session.beginTransaction();
			session.update(product);
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
			Product  product = session.get(Product.class, id);
			if (product != null) {
				session.delete(product);
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
	public Product findByPK(Integer id) {
		Session session = sessionFactory.unwrap(Session.class);
		try {
			session.beginTransaction();
			Product  product = session.get(Product.class, id);
			session.getTransaction().commit();
			return product;
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		return null;
	}

	@Override
	public List<Product> getAll() {
		Session session = sessionFactory.unwrap(Session.class);
		try {
			session.beginTransaction();
			List<Product> list = session.createQuery("from Product", Product.class).list();
			session.getTransaction().commit();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		return null;
	}

	@Override
	public List<Product> findByBusinessMemberId(Integer businessMemberId) {
		Session session = sessionFactory.unwrap(Session.class);
		try {
			session.beginTransaction();
			String hql = "FROM Product p WHERE p.businessMember.id = :businessMemberId";
			List<Product> products = session.createQuery(hql, Product.class)
					.setParameter("businessMemberId", businessMemberId)
					.list();
			session.getTransaction().commit();
			return products;
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}
		return null;
	}

}
