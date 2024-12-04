package com.artogether.common.perm_desc;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class PermDescDaoHibernate implements PermDescDao {

	@Autowired
	EntityManagerFactory sessionFactory;

	@Override
	@Transactional
	public int add(PermDesc permDesc) {
		Session session = sessionFactory.unwrap(Session.class);

		Integer id = (Integer) session.save(permDesc);
		return id;

	}

	@Override
	public int update(PermDesc permDesc) {
		Session session = sessionFactory.unwrap(Session.class);

			session.saveOrUpdate(permDesc);
			return 1;
		
	}

	@Override
	public int delete(Integer id) {
		Session session = sessionFactory.unwrap(Session.class);
		
			PermDesc pd = session.get(PermDesc.class, id);
			return 1;

	}

	@Override
	public PermDesc findByPK(Integer id) {
		Session session = sessionFactory.unwrap(Session.class);
	
			PermDesc pd = session.get(PermDesc.class, id);
			return pd;
		
	}

	@Override
	public List<PermDesc> getAll() {
		Session session = sessionFactory.unwrap(Session.class);
		
			List<PermDesc> list = session.createQuery("from PermDesc", PermDesc.class).list();
			return list;
	
	}
}
