package com.artogether.event.evt_track;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.artogether.common.member.Member;
import com.artogether.common.member.MemberService;
import com.artogether.event.event.Event;
import com.artogether.event.event.EventService;

import java.sql.Date;
import java.sql.Timestamp;
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
	
	@Autowired
	private EventService evtService;
	
	@Autowired
	private MemberService memberService;
	

	public EvtTrack findById(Integer memberId, Integer evtId) {
		EvtTrackPK id = EvtTrackPK.builder().evtId(evtId).memberId(memberId).build();
		return repo.findById(id).orElse(new EvtTrack());
	}
	
	public EvtTrack addEvtTrack(Integer memberId, Integer evtId) {
		Event event = evtService.findById(evtId);
		Member member = memberService.findById(memberId);
		EvtTrackPK id = EvtTrackPK.builder().evtId(event.getId()).memberId(member.getId()).build();
		EvtTrack evtTrack=EvtTrack.builder().evtTrackPK(id)
											.event(event)
											.member(member)
											.trackDate(new Timestamp(System.currentTimeMillis()))
											.build();
		return repo.save(evtTrack);
	}

	public void deleteEvtTrack(Integer memberId, Integer evtId) {
		EvtTrackPK id = EvtTrackPK.builder().evtId(evtId).memberId(memberId).build();
		repo.deleteById(id);
	}
	
	public String toggleEvtTrack(Integer memberId, Integer evtId) {
		if(findById(memberId, evtId).getEvtTrackPK()!=null) {
			deleteEvtTrack(memberId,evtId);
			return "已取消追蹤";
		}else{
			addEvtTrack(memberId, evtId);
			return "成功追蹤";
		}
	}

	public List<EvtTrack> findByMemberId(Integer memberId) {
		return repo.findByMember_Id(memberId);
	}
}
