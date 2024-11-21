package com.artogether.event.evt_img;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManagerFactory;
import java.util.List;

@Repository
public class EvtImgHibernateDAO implements EvtImgDAO_interface{
	private static final String GET_ALL_STMT = "FROM event ORDER BY id";

	@Autowired
	private EntityManagerFactory sessionFactory;

	@Override
	public void insert(EvtImgVO evtImgVO) {
		sessionFactory.unwrap(Session.class).save(evtImgVO);
	}

	@Override
	public void update(EvtImgVO evtImgVO) {
		sessionFactory.unwrap(Session.class).update(evtImgVO);
	}

	@Override
	public void delete(Integer id) {
		EvtImgVO evtImgVO=new EvtImgVO();
		evtImgVO.setId(id);
		Session session = sessionFactory.unwrap(Session.class);
	    session.delete(id);
	}

	@Override
	@Transactional(readOnly = true)
	public EvtImgVO findByPrimaryKey(Integer id) {
		Session session = sessionFactory.unwrap(Session.class);
		return session.get(EvtImgVO.class, id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<EvtImgVO> getAll() {
		Session session = sessionFactory.unwrap(Session.class);
		return session.createNativeQuery(GET_ALL_STMT, EvtImgVO.class).getResultList();
	}
}
