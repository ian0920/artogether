package com.artogether.controller.wynn;

import com.artogether.common.business_member.BusinessService;
import com.artogether.common.system_manager.SystemManager;
import com.artogether.event.event.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;



@Controller
@RequestMapping("/platform")
public class Wynn_PlatformController {
	
	@Autowired
	private BusinessService businessService;
	
	@Autowired
	private EventService eventService;
	
	//==========查詢活動，以審核或更新狀態==================
	@GetMapping("/eventListToReview")
	public String eventListToReview(Model model) {
		// 管理員才能使用
		// TODO: 此處為假管理員，後續須改為透過session獲取等方式
		SystemManager sm = new SystemManager();
		sm.setId(1);
		model.addAttribute("systemManager",sm);
		return "redirect:/platform/search_events";
	}
	
	@GetMapping("/search_events")
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
		return "platform/event_search";
	}
	
	
	//==========查詢商家會員，以審核或更新狀態==================

	@GetMapping("/bMemberlistToReview")
	public String bMemberlistToReview(HttpSession session, Model model) {
		// 只有管理員能查看
//		Object sm = session.getAttribute("systemManager");
//		if(sm == null) {
//			model.addAttribute("errors", "權限不足");
//			return "/error";
//		}
		return "redirect:/platform/search_bmemb?/status=1";
	}
	
	@GetMapping("/search_bmemb")
	public String searchBMembers(@RequestParam Map<String, String> searchParams, Model model) {
		// 定義分頁與排序的參數名稱
	    List<String> paginationAndSortingKeys = List.of("page", "size", "sortField", "sortDirection");
	    System.out.println(searchParams);

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
	    System.out.println(searchCriteria);

	    // 呼叫服務層處理
		Page p = businessService.searchBMembs(searchCriteria,pageRequest);
		model.addAttribute("page", p);
		return "platform/bMemberList";
	}
}
