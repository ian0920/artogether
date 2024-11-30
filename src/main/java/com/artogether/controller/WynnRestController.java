package com.artogether.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

	@PostMapping("/track")
	public ResponseEntity<EvtTrack> trackEvent(Model model) {
		EvtTrack evtTrack=evtTrackService.addEvtTrack(null);
		return ResponseEntity.ok(evtTrack);

	}
}
