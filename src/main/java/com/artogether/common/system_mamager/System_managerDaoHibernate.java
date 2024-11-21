package com.artogether.common.system_mamager;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;


@Repository
@Transactional
public class System_managerDaoHibernate implements System_managerDao {

	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	public int add(System_manager systemManager) {
		Session session = sessionFactory.getCurrentSession();
	
			Integer id = (Integer) session.save(systemManager);
			return id;
		
	}

	@Override
	public int update(System_manager systemManager) {
		Session session = sessionFactory.getCurrentSession();
	
			session.update(systemManager);
			return 1;
	
	}

	@Override
	public int delete(Integer id) {
		Session session = sessionFactory.getCurrentSession();

			System_manager sys = session.get(System_manager.class, id);
			return 1;
			
	}

	@Override
	public System_manager findByPK(Integer id) {
		Session session = sessionFactory.getCurrentSession();
		
			System_manager sys = session.get(System_manager.class, id);
			return sys;
	
	}

	@Override
	public List<System_manager> getAll() {
		Session session = sessionFactory.getCurrentSession();
		
			List<System_manager> list = session.createQuery("from system_manager", System_manager.class).list();
			return list;

	}

}
