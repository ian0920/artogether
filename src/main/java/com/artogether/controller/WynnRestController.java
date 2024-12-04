package com.artogether.controller;

import java.util.HashMap;
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

import com.artogether.common.member.Member;
import com.artogether.event.event.Event;
import com.artogether.event.event.EventService;
import com.artogether.event.evt_track.EvtTrack;
import com.artogether.event.evt_track.EvtTrackService;

@RestController
@RequestMapping("/api") 
public class WynnRestController {
	@Autowired
	private EventService eventService;
	
	@Autowired
	private EvtTrackService evtTrackService;

	@PostMapping("/event/toggleTrack")
	public ResponseEntity<Map> trackEvent(@RequestBody Map<String, Object> payload) {
        Integer evtId = (Integer) payload.get("eventId");
        Integer memberId = (Integer) payload.get("memberId");
		
		String msg=evtTrackService.toggleEvtTrack(memberId,evtId);
		 Map<String, String> response = new HashMap<>();
	        response.put("message", msg);
	        return ResponseEntity.ok(response);

	}
}
