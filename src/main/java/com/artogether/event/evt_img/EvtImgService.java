package com.artogether.event.evt_img;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.artogether.event.event.EventRepo;

import java.util.List;


@Service
public class EvtImgService {

//	@Autowired
//	private EvtImgDAO_interface dao;

//	public EvtImgService() {
//		dao = new EvtImgHibernateDAO();
//	}
	
  @Autowired
    private EvtImgRepo repo;

	public void saveEvtImg(EvtImg evtImgVO) {
		repo.save(evtImgVO);
	}

//	public void updateEvtImg(EvtImg evtImgVO) {
//		repo.update(evtImgVO);
//	}

	public void deleteEvtImg(Integer id) {
		repo.deleteById(id);
	}

	public EvtImg findById(Integer id) {
		return repo.findById(id).orElse(new EvtImg());
	}

	public List<EvtImg> findAll() {
		return repo.findAll();
	}
}
