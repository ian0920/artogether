package com.artogether.event.evt_track;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


public interface EvtTrackRepo extends JpaRepository<EvtTrack, EvtTrackPK>{
	// 使用 member 作為屬性名，並查詢其內部的 id
    List<EvtTrack> findByMember_Id(Integer memberId);

}
