package com.artogether.controller.wynn;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.artogether.common.business_member.BusinessMember;
import com.artogether.common.business_member.BusinessService;
import com.artogether.common.member.Member;
import com.artogether.event.event.Event;
import com.artogether.event.event.EventService;
import com.artogether.event.evt_img.EvtImgService;
import com.artogether.event.evt_track.EvtTrack;
import com.artogether.event.evt_track.EvtTrackService;

@RestController
@RequestMapping("/api") 
public class Wynn_RestController {
	@Autowired
	private EventService eventService;
	
	@Autowired
	private EvtTrackService evtTrackService;

	@Autowired
	private EvtImgService evtImgService;
	
	@Autowired
	private BusinessService businessService;

	//==========================================================
	//         				活動相關
	//==========================================================
	
	// 追蹤、取消追蹤活動
	@PostMapping("/event/toggleTrack")
	public ResponseEntity<Map> trackEvent(@RequestBody Map<String, Object> payload) {
        Integer evtId = (Integer) payload.get("eventId");
        Integer memberId = (Integer) payload.get("memberId");
        // TODO: 會員ONLY
		
		String msg=evtTrackService.toggleEvtTrack(memberId,evtId);
		Map<String, String> response = new HashMap<>();
        response.put("message", msg);
        return ResponseEntity.ok(response);
	}
	
	// 刪除活動圖片
	@PostMapping("/event/deleteEvtImg")
	public ResponseEntity<Map> deleteEvtImg(@RequestBody Map<String, Object> payload){
		List<Integer> imgList = (List<Integer>) payload.get("evtImgId");
		System.out.println(imgList);
		evtImgService.deleteByIdList(imgList);
		Map<String, String> response = new HashMap<>();
        response.put("message", "成功刪除");
        return ResponseEntity.ok(response);
		
	}
	
	// (商家)更新活動狀態
	@PostMapping("/event/updateStatus")
	public ResponseEntity<Map> updateStatus(@RequestBody Map<String, Object> payload){
		Integer evtId = (Integer) payload.get("eventId");
        Byte status = ((Integer)payload.get("status")).byteValue();
        Event e=  Event.builder().id(evtId).status(status).build();
		eventService.statusUpdate(e);
		Map<String, String> response = new HashMap<>();
        response.put("message", "成功更新狀態");
        return ResponseEntity.ok(response);
		
	}
	
	//==========================================================
	//         				平台相關
	//==========================================================
	
	// 更新商家會員狀態(審核、停權、啟用)
	@PostMapping("/platform/updateBMemberStatus")
	public ResponseEntity<Map> updateBMemberStatus(@RequestBody Map<String, Object> payload){
		Integer bMembId = (Integer) payload.get("bMembId");
		Byte status = ((Integer)payload.get("status")).byteValue();
		BusinessMember b = BusinessMember.builder().id(bMembId).status(status).build();
		businessService.statusUpdate(b);
		Map<String, String> response = new HashMap<>();
		response.put("message", "成功更新狀態");
		return ResponseEntity.ok(response);
		
	}
}
