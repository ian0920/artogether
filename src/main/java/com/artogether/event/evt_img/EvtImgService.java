package com.artogether.event.evt_img;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class EvtImgService {

	@Autowired
	private EvtImgDAO_interface dao;

	public EvtImgService() {
		dao = new EvtImgHibernateDAO();
	}

	public void addEvent(EvtImgVO evtImgVO) {
		 dao.insert(evtImgVO);
	}

	public void updateEvent(EvtImgVO evtImgVO) {
		 dao.update(evtImgVO);
	}

	public void deleteEvent(Integer id) {
		dao.delete(id);
	}

	public EvtImgVO getOneEvent(Integer id) {
		return dao.findByPrimaryKey(id);
	}

	public List<EvtImgVO> getAll() {
		return dao.getAll();
	}
}
