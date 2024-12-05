package com.artogether.event.evt_img;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EvtImgRepo extends JpaRepository<EvtImg, Integer>{
	
	List<EvtImg> findAllByEvent_Id(Integer evtId);

}



