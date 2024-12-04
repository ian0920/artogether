package com.artogether.common.platform_msg;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class PlatformMsgDaoHibernate implements PlatformMsgDao {

	@Autowired
	EntityManagerFactory sessionFactory;
	
	@Override
	public int add(PlatformMsg platformMsg) {
		Session session = sessionFactory.unwrap(Session.class);
		
			Integer id = (Integer) session.save(platformMsg);
			return id;
		
	}

	@Override
	public int update(PlatformMsg platformMsg) {
		Session session = sessionFactory.unwrap(Session.class);
	
			session.update(platformMsg);
			return 1;
	
	}

	@Override
	public int delete(Integer id) {
		Session session = sessionFactory.unwrap(Session.class);
	
			PlatformMsg pm = session.get(PlatformMsg.class, id);
			return 1;
	
	}

	@Override
	public PlatformMsg findByPK(Integer id) {
		Session session = sessionFactory.unwrap(Session.class);
	
			PlatformMsg pm = session.get(PlatformMsg.class, id);
			return pm;
	
	}

	@Override
	public List<PlatformMsg> getAll() {
		Session session = sessionFactory.unwrap(Session.class);
		
			List<PlatformMsg> list = session.createQuery("from PlatformMsg", PlatformMsg.class).list();
			return list;
	
	}
}
