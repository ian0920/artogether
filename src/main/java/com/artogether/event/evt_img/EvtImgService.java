package com.artogether.event.evt_img;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.artogether.event.event.Event;
import com.artogether.event.event.EventRepo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.mail.Multipart;


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
	
	public void saveAllEvtImg(List<EvtImg> list) {
		repo.saveAll(list);
	}
	public void saveMultipartList(Event event, List<MultipartFile> list) {

	    List<EvtImg> newImages = new ArrayList<>();
		for (MultipartFile file : list) {
	        if (!file.isEmpty()) {
	            try {
	                EvtImg evtImg = new EvtImg();
	                evtImg.setImageFile(file.getBytes());
	                evtImg.setEvent(event); // 設定關聯
	                newImages.add(evtImg);
	            } catch (IOException e) {
	                e.printStackTrace();
	                // 處理文件上傳失敗的情況
	            }
	        }
	    }
		repo.saveAll(newImages);
	}

//	public void updateEvtImg(EvtImg evtImgVO) {
//		repo.update(evtImgVO);
//	}

	public void deleteEvtImg(Integer id) {
		repo.deleteById(id);
	}
	
	public void deleteByIdList(List<Integer> ids) {
		repo.deleteAllById(ids);
	}

	public EvtImg findById(Integer id) {
		return repo.findById(id).orElse(new EvtImg());
	}

	public List<EvtImg> findAllByEventId(Integer eid) {
		return repo.findAllByEvent_Id(eid);
	}
}
