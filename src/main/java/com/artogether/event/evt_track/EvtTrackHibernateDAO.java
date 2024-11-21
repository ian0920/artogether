package com.artogether.event.evt_track;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManagerFactory;
import java.util.List;


@Repository
public class EvtTrackHibernateDAO implements EvtTrackDAO_interface{

	private static final String GET_ALL_STMT = "FROM evt_track";

	@Autowired
	private EntityManagerFactory sessionFactory;


	@Override
	public void insert(EvtTrackVO evtTrackVO) {
		sessionFactory.unwrap(Session.class).save(evtTrackVO);
	}

	@Override
	public void update(EvtTrackVO evtTrackVO) {
		sessionFactory.unwrap(Session.class).update(evtTrackVO);
	}

	@Override
	public void delete(Integer id) {
		EvtTrackVO evtTrackVO=new EvtTrackVO();
		evtTrackVO.setId(id);
		Session session = sessionFactory.unwrap(Session.class);
		session.delete(id);

	}

	@Override
	@Transactional(readOnly = true)
	public EvtTrackVO findByPrimaryKey(Integer id) {
		Session session = sessionFactory.unwrap(Session.class);
		return session.get(EvtTrackVO.class, id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<EvtTrackVO> getAll() {
		Session session = sessionFactory.unwrap(Session.class);
		return session.createNativeQuery(GET_ALL_STMT, EvtTrackVO.class).getResultList();
	}


}
