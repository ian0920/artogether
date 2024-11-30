package com.artogether.event.evt_track;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class EvtTrackService {

//	@Autowired
//	private EvtTrackDAO_interface dao;
//
//	public EvtTrackService() {
//		dao = new EvtTrackHibernateDAO();
//	}
	
	@Autowired
	private EvtTrackRepo repo;

	public EvtTrack addEvtTrack(EvtTrack evtTrackVO) {
		return repo.save(evtTrackVO);
	}

	public void deleteEvtTrack(Integer id) {
		repo.deleteById(id);
	}

	public EvtTrack findById(Integer id) {
		return repo.findById(id).orElse(new EvtTrack());
	}

	public List<EvtTrack> findAllEvtTracks() {
		return repo.findAll();
	}
}
