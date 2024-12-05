package com.artogether.controller.wynn;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.artogether.common.business_member.BusinessMember;
import com.artogether.common.business_member.BusinessService;


@Controller
public class BusinessController {
	
	@Autowired
	private BusinessService businessService;
	
	// 註冊成為商家
	@GetMapping("/registerBusiness")
	public String registerBusiness(HttpSession session, Model model) {
		Object member = session.getAttribute("member");
        if (member == null) { // 檢查 member 是否為 null
            // 導向登入會員
            return "redirect:/login";
        }
        BusinessMember bMember = new BusinessMember();
        model.addAttribute("businessMember",bMember);
        return "frontend/registerBusiness";
	}
	@PostMapping("/applyForBusiness")
	public String applyForBusiness(@ModelAttribute BusinessMember bMember) {
		//TODO: 做資料驗證(地址那些)
		bMember.setStatus((byte) 1);
		businessService.save(bMember);
		return "homepage";
	}
	
	@GetMapping("bMemberlistToReview")
	public String bMemberlistToReview(HttpSession session, Model model) {
		// 只有管理員能查看
//		Object sm = session.getAttribute("systemManager");
//		if(sm == null) {
//			model.addAttribute("errors", "權限不足");
//			return "/error";
//		}
		return "redirect:/platform/search_bmemb/status=1";
	}
	
	@GetMapping("/platform/search_bmemb")
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
		Page p = businessService.searchEvents(searchCriteria,pageRequest);
		model.addAttribute("page", p);
		return "platform/bMemberList";
	}
}
