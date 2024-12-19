package com.artogether.controller.wynn;

import com.artogether.common.business_member.BusinessMember;
import com.artogether.common.business_member.BusinessService;
import com.artogether.event.event.Event;
import com.artogether.event.event.EventService;
import com.artogether.event.evt_img.EvtImg;
import com.artogether.event.evt_img.EvtImgService;
import com.artogether.event.evt_order.EvtOrder;
import com.artogether.event.evt_order.EvtOrderService;
import com.artogether.event.evt_track.EvtTrack;
import com.artogether.event.evt_track.EvtTrackService;
import com.artogether.event.my_evt_coup.MyEvtCoup;
import com.artogether.event.my_evt_coup.MyEvtCoupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/event")
public class Wynn_EventController {
	@Autowired
	private EventService eventService;
	@Autowired
	private EvtImgService evtImgService;
	@Autowired
	private EvtTrackService evtTrackService;
	@Autowired
	private BusinessService businessService;
	@Autowired
    MyEvtCoupService myEvtCoupService;
	@Autowired
    EvtOrderService evtOrderService;

	
	//===============轉換html的datatime-local為Timestamp=============
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
	public String checkEvent(Model model, @PathVariable("evtId") Integer eventId, HttpSession session) {
        Integer memberId = (Integer)session.getAttribute("member");
		Event evt = eventService.findById(eventId);
		List<EvtImg> evtImgList= evtImgService.findAllByEventId(eventId);
		model.addAttribute("evt", evt);
		model.addAttribute("evtImgList", evtImgList);
		
		//撈出報名中的訂單 (報名中則報名按鈕disable)
        Map<Event, EvtOrder> map = evtOrderService.getEventsToMyOrders(memberId, true);
        Predicate<Event> filter = p -> (Objects.equals(p.getId(), eventId));
        boolean match = map.keySet().stream().anyMatch(filter);

        if(match){
            model.addAttribute("match", true);
        } else {
            model.addAttribute("match", false);
        }

        //找出此會員所擁有此活動的優惠券
        List<MyEvtCoup> myEvtCoups = myEvtCoupService.getMyEvtCoupsByMemberIdAndEventId(memberId, eventId);

        //確認取出的優惠券是未使用的 status = 0
        List<MyEvtCoup> filteredMyEvtCoups = myEvtCoups.stream().filter(mec -> mec.getStatus() == 0).toList();
        model.addAttribute("eventObject", evt);

        Map<Integer, Double> evtCoupIdAndTypeMap = new HashMap<>();
        filteredMyEvtCoups.forEach(m -> evtCoupIdAndTypeMap.put(m.getEvtCoup().getId(), m.getEvtCoup().getRatio()== null ? m.getEvtCoup().getDeduction() : m.getEvtCoup().getRatio()));

        model.addAttribute("myEvtCoups", filteredMyEvtCoups);
        model.addAttribute("evtCoupIdAndTypeMap", evtCoupIdAndTypeMap);
		
		return "event/event_details";
	}
	
	// 商家：查看自己發布的所有活動
	@GetMapping("/MyPostedEvent")
	public String myPostedEvent(HttpSession session, Model model) {
		BusinessMember businessMember = (BusinessMember) session.getAttribute("presentBusinessMember");
		if(businessMember==null||businessMember.getId()==null) {
			return "redirect:/business_login";
		}
		String bMemberId = Integer.toString(businessMember.getId());
		PageRequest pr=PageRequest.of(0, 10, Sort.by("id").descending());
		Page<Event> eventPage = eventService.searchEvents(Map.of("businessId", bMemberId), pr);
		model.addAttribute("page",eventPage);
		return "event/event_myPostedEvent";
	}
	
	//==============編輯活動======================
	@GetMapping("/edit/{evtId}")
	public String editEvent(Model model, @PathVariable("evtId") Integer evtId) {
		//TODO: 須新增機制，防止非原商家編輯活動
		Event evt = eventService.findById(evtId);
		List<EvtImg> evtImgList= evtImgService.findAllByEventId(evtId);
//		String base64Image = evtImg.getImageFile() != null 
//	            ? "data:image/jpeg;base64," + Base64Utils.encodeToString(evtImg.getImageFile()) 
//	            : null;
		model.addAttribute("event", evt);
		model.addAttribute("evtImgList", evtImgList);
		return "event/event_edit";
	}
	
	//===編輯後，更新活動
	@PostMapping("/update")
	public String updateEvent(@ModelAttribute Event event, 
							  @RequestParam("evtImgList") List<MultipartFile> evtImgList,
							  HttpSession session) {

//		BusinessMember businessMember = businessService.findById(event.getBusinessMember().getId());
		BusinessMember businessMember = (BusinessMember) session.getAttribute("presentBusinessMember");
	    event.setBusinessMember(businessMember); // 設置 businessMember
		Event e =eventService.partialUpdate(event);
		evtImgService.saveMultipartList(e, evtImgList);

		return "redirect:/event/details/" + event.getId();
	}
	
	//======================================================
	
	@GetMapping("all_tracks")
	public String listAllEvtTracks(HttpSession session, Model model) {
		 Integer memberId = (Integer)session.getAttribute("member");
		List<EvtTrack> list=evtTrackService.findByMemberId(memberId);
		model.addAttribute("evtTrackList", list);
		return "event/event_list_all_tracks";
	}
	
	@GetMapping("DBImgReader/{evtImgId}")
	public void dBGifReader(@PathVariable("evtImgId") Integer evtImgId, HttpServletRequest req, HttpServletResponse res)
			                                                                                          throws IOException {
		res.setContentType("image/gif");
		ServletOutputStream out = res.getOutputStream();

		try {
			out.write(evtImgService.findById(evtImgId).getImageFile());
		} catch (Exception e) {
			ClassPathResource imgFile = new ClassPathResource("static/web_design/asset/images/picture_error.png");
	        byte[] imageBytes = imgFile.getInputStream().readAllBytes();
	        out.write(imageBytes);
		}
	}

	//===============新增活動=======================
	@GetMapping("/add")
	public String addEvent(Model model, HttpSession session) {
		Event evt = new Event();
		BusinessMember businessMember = (BusinessMember) session.getAttribute("presentBusinessMember");
		evt.setBusinessMember(businessMember);
		
		EvtImg evtImg = new EvtImg();
		model.addAttribute("event", evt);
		model.addAttribute("evtImg", evtImg);
		return "event/event_edit";
	}
	// 新增頁面後，實際創建
	@PostMapping("/create")
	public String createEvent(@ModelAttribute Event event,
							  @RequestParam("evtImgList") List<MultipartFile> evtImgList) {
		Event e = eventService.saveEvent(event);
		evtImgService.saveMultipartList(e, evtImgList);
		return "redirect:/event/details/"+ e.getId();
	}
	
	//====================搜尋活動========================
	@GetMapping("/search")
	public String searchEvents(@RequestParam Map<String, String> searchParams, Model model) {
		// 定義分頁與排序的參數名稱
	    List<String> paginationAndSortingKeys = List.of("page", "size", "sortField", "sortDirection");

	 // 構建 PageRequest
	    int page = Integer.parseInt(searchParams.getOrDefault("page", "0")); // 頁數從 0 開始
	    int size = Integer.parseInt(searchParams.getOrDefault("size", "10"));
	    String sortField = searchParams.getOrDefault("sortField", "startDate");
	    String sortDirection = searchParams.getOrDefault("sortDirection", "asc");
	    PageRequest pageRequest = PageRequest.of(page, size, 
	        "asc".equalsIgnoreCase(sortDirection) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending()
	    );

	    // 剩下的視為搜尋條件
	    Map<String, String> searchCriteria = searchParams.entrySet().stream()
	        .filter(entry -> !paginationAndSortingKeys.contains(entry.getKey()))
	        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

	    // 呼叫服務層處理
		Page p = eventService.searchEvents(searchCriteria,pageRequest);
		model.addAttribute("page", p);
		return "event/event_search";
	}
}
