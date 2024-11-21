package com.artogether.common.platform_msg;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class Platform_msgDaoHibernate implements Platform_msgDao{

	@Autowired
	EntityManagerFactory sessionFactory;
	
	@Override
	public int add(Platform_msg platformMsg) {
		Session session = sessionFactory.unwrap(Session.class);
		
			Integer id = (Integer) session.save(platformMsg);
			return id;
		
	}

	@Override
	public int update(Platform_msg platformMsg) {
		Session session = sessionFactory.unwrap(Session.class);
	
			session.update(platformMsg);
			return 1;
	
	}

	@Override
	public int delete(Integer id) {
		Session session = sessionFactory.unwrap(Session.class);
	
			Platform_msg pm = session.get(Platform_msg.class, id);
			return 1;
	
	}

	@Override
	public Platform_msg findByPK(Integer id) {
		Session session = sessionFactory.unwrap(Session.class);
	
			Platform_msg pm = session.get(Platform_msg.class, id);
			return pm;
	
	}

	@Override
	public List<Platform_msg> getAll() {
		Session session = sessionFactory.unwrap(Session.class);
		
			List<Platform_msg> list = session.createQuery("from Platform_msg", Platform_msg.class).list();
			return list;
	
	}
}
