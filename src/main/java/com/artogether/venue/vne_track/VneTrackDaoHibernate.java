package com.artogether.venue.vne_track;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class VneTrackDaoHibernate implements VneTrackDao {

	@Autowired
	EntityManagerFactory sessionFactory;
	
	@Override
	public VneTrack add(VneTrack.Vne_trackCom vneTrackCom) {
		Session session = sessionFactory.unwrap(Session.class);
	
		VneTrack vt = (VneTrack) session.save(vneTrackCom);
		return vt;
		
	}

	@Override
	public void update(VneTrack.Vne_trackCom vneTrackCom) {
		Session session = sessionFactory.unwrap(Session.class);
	
		session.update(vneTrackCom);

	}

	@Override
	public Boolean delete(VneTrack.Vne_trackCom vneTrackCom) {
		Session session = sessionFactory.unwrap(Session.class);

		VneTrack.Vne_trackCom vt = session.get(VneTrack.Vne_trackCom.class, vneTrackCom);
		return true;
			
	}

	@Override
	public VneTrack findByPK(VneTrack.Vne_trackCom vneTrackCom) {
		Session session = sessionFactory.unwrap(Session.class);
		
			VneTrack vt = session.get(VneTrack.class, vneTrackCom);
			return vt;
	
	}

	@Override
	public List<VneTrack> getAll() {
		Session session = sessionFactory.unwrap(Session.class);
		
			List<VneTrack> list = session.createQuery("from VneTrack", VneTrack.class).list();
			return list;

	}

}

