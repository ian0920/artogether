package com.artogether.common.perm_desc;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class Perm_descDaoHibernate implements Perm_descDao{

	@Autowired
	EntityManagerFactory sessionFactory;

	@Override
	@Transactional
	public int add(Perm_desc permDesc) {
		Session session = sessionFactory.unwrap(Session.class);

		Integer id = (Integer) session.save(permDesc);
		return id;

	}

	@Override
	public int update(Perm_desc permDesc) {
		Session session = sessionFactory.unwrap(Session.class);

			session.saveOrUpdate(permDesc);
			return 1;
		
	}

	@Override
	public int delete(Integer id) {
		Session session = sessionFactory.unwrap(Session.class);
		
			Perm_desc pd = session.get(Perm_desc.class, id);
			return 1;

	}

	@Override
	public Perm_desc findByPK(Integer id) {
		Session session = sessionFactory.unwrap(Session.class);
	
			Perm_desc pd = session.get(Perm_desc.class, id);
			return pd;
		
	}

	@Override
	public List<Perm_desc> getAll() {
		Session session = sessionFactory.unwrap(Session.class);
		
			List<Perm_desc> list = session.createQuery("from Perm_desc", Perm_desc.class).list();
			return list;
	
	}
}
