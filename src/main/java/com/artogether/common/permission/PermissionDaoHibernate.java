package com.artogether.common.permission;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;
import java.util.List;


@Repository
@Transactional
public class PermissionDaoHibernate implements PermissionDao{

	@Autowired
	EntityManagerFactory sessionFactory;
	
	@Override
	public int add(Permission permission) {
		Session session = sessionFactory.unwrap(Session.class);
		
			Integer id = (Integer) session.save(permission);
			return id;
		
	}

	@Override
	public int update(Permission permission) {
		Session session = sessionFactory.unwrap(Session.class);
		
			session.update(permission);
			return 1;
		
	}

	@Override
	public int delete(Integer id) {
		Session session = sessionFactory.unwrap(Session.class);
	
			Permission pm = session.get(Permission.class, id);
			return 1;
		
	}

	@Override
	public Permission findByPK(Integer id) {
		Session session = sessionFactory.unwrap(Session.class);
		
			Permission pm = session.get(Permission.class, id);
			return pm;
		
	}

	@Override
	public List<Permission> getAll() {
		Session session = sessionFactory.unwrap(Session.class);
		
			List<Permission> list = session.createQuery("from Permission", Permission.class).list();
			return list;
	}
}
