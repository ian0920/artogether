package com.artogether.event.evt_track;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class EvtTrackService {

	@Autowired
	private EvtTrackDAO_interface dao;

	public EvtTrackService() {
		dao = new EvtTrackHibernateDAO();
	}

	public void addEvtTrack(EvtTrackVO evtTrackVO) {
		 dao.insert(evtTrackVO);
	}

	public void deleteEvent(Integer id) {
		dao.delete(id);
	}

	public EvtTrackVO getOneEvent(Integer id) {
		return dao.findByPrimaryKey(id);
	}

	public List<EvtTrackVO> getAll() {
		return dao.getAll();
	}
}
