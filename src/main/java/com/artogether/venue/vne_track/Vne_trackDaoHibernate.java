package com.artogether.venue.vne_track;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class Vne_trackDaoHibernate implements Vne_trackDao{

	@Autowired
	EntityManagerFactory sessionFactory;
	
	@Override
	public Vne_track add(Vne_track.Vne_trackCom vneTrackCom) {
		Session session = sessionFactory.unwrap(Session.class);
	
		Vne_track vt = (Vne_track) session.save(vneTrackCom);
		return vt;
		
	}

	@Override
	public void update(Vne_track.Vne_trackCom vneTrackCom) {
		Session session = sessionFactory.unwrap(Session.class);
	
		session.update(vneTrackCom);

	}

	@Override
	public Boolean delete(Vne_track.Vne_trackCom vneTrackCom) {
		Session session = sessionFactory.unwrap(Session.class);

		Vne_track.Vne_trackCom vt = session.get(Vne_track.Vne_trackCom.class, vneTrackCom);
		return true;
			
	}

	@Override
	public Vne_track findByPK(Vne_track.Vne_trackCom vneTrackCom) {
		Session session = sessionFactory.unwrap(Session.class);
		
			Vne_track vt = session.get(Vne_track.class, vneTrackCom);
			return vt;
	
	}

	@Override
	public List<Vne_track> getAll() {
		Session session = sessionFactory.unwrap(Session.class);
		
			List<Vne_track> list = session.createQuery("from Vne_track", Vne_track.class).list();
			return list;

	}

}

