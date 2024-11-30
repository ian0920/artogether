package com.artogether.controller;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.artogether.common.business_member.BusinessMember;
import com.artogether.common.business_member.BusinessMemberRepo;
import com.artogether.common.business_member.BusinessService;
import com.artogether.event.event.Event;
import com.artogether.event.event.EventService;
import com.artogether.event.evt_img.EvtImg;
import com.artogether.event.evt_img.EvtImgService;

@Controller
@RequestMapping("event")
public class WynnController {
	@Autowired
	private EventService eventService;
	@Autowired
	private EvtImgService evtImgService;
	@Autowired
	private BusinessService businessService;
	
	//轉換html的datatime-local為Timestamp
	@InitBinder
    public void initBinder(WebDataBinder binder) {
        // 定義自訂的 PropertyEditor，用於將表單中的日期字串轉換為 Timestamp
        binder.registerCustomEditor(Timestamp.class, new java.beans.PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                try {
                    // 定義日期格式，匹配 <input type="datetime-local">
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                    setValue(new Timestamp(dateFormat.parse(text).getTime()));
                } catch (ParseException e) {
                    setValue(null);
                }
            }
        });
    }

	//=========查看活動詳情================
	@GetMapping("/details/{evtId}")
	public String checkEvent(Model model, @PathVariable("evtId") Integer evtId) {
		Event evt = eventService.findById(evtId);
		EvtImg evtImg= evtImgService.findById(evtId);
		model.addAttribute("evt", evt);
		model.addAttribute("evtImg", evtImg);
		return "event/event_details";
	}
	
	//==============編輯活動======================
	@GetMapping("/edit/{evtId}")
	public String editEvent(Model model, @PathVariable("evtId") Integer evtId) {
		Event evt = eventService.findById(evtId);
		EvtImg evtImg= evtImgService.findById(evtId);
		String base64Image = evtImg.getImageFile() != null 
	            ? "data:image/jpeg;base64," + Base64Utils.encodeToString(evtImg.getImageFile()) 
	            : null;
		model.addAttribute("event", evt);
		model.addAttribute("evtImg", base64Image);
		return "event/event_edit";
	}
	
	//==============編輯後，更新活動===================
	@PostMapping("/update")
	public String updateEvent(@ModelAttribute Event event) {
		System.out.println(event);
//		System.out.println(event.getBusinessMember().getId());
		BusinessMember businessMember = businessService.findById(event.getBusinessMember().getId());
		System.out.println(businessMember);
	    event.setBusinessMember(businessMember); // 設置 businessMember
		eventService.saveEvent(event);

		return "redirect:/event/details/" + event.getId();
	}

	//===============新增活動=======================
//	@GetMapping("/create")
//	public String createEvent() {
//		return "event/event_edit";
//	}
	

}
