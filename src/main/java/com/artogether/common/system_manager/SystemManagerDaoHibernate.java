package com.artogether.common.system_manager;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;


@Repository
@Transactional
public class SystemManagerDaoHibernate implements SystemManagerDao {

	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	public int add(SystemManager systemManager) {
		Session session = sessionFactory.getCurrentSession();
	
			Integer id = (Integer) session.save(systemManager);
			return id;
		
	}

	@Override
	public int update(SystemManager systemManager) {
		Session session = sessionFactory.getCurrentSession();
	
			session.update(systemManager);
			return 1;
	
	}

	@Override
	public int delete(Integer id) {
		Session session = sessionFactory.getCurrentSession();

			SystemManager sys = session.get(SystemManager.class, id);
			return 1;
			
	}

	@Override
	public SystemManager findByPK(Integer id) {
		Session session = sessionFactory.getCurrentSession();
		
			SystemManager sys = session.get(SystemManager.class, id);
			return sys;
	
	}

	@Override
	public List<SystemManager> getAll() {
		Session session = sessionFactory.getCurrentSession();
		
			List<SystemManager> list = session.createQuery("from system_manager", SystemManager.class).list();
			return list;

	}

}
