package com.artogether.event.evt_img;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvtImgRepo extends JpaRepository<EvtImg, Integer>{
	
	List<EvtImg> findAllByEvent_Id(Integer evtId);

	List<EvtImg> findAllByEvent_IdIn(List<Integer> evtIds);
}



