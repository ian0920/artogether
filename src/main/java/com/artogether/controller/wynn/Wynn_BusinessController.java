package com.artogether.controller.wynn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.artogether.common.business_member.BusinessMember;
import com.artogether.common.business_member.BusinessService;
import com.artogether.common.business_perm.BusinessPerm;
import com.artogether.common.business_perm.BusinessPermService;
import com.artogether.common.member.Member;
import com.artogether.common.member.MemberService;


@Controller
@RequestMapping("business")
public class Wynn_BusinessController {
	
	@Autowired
	private BusinessService businessService;
	
	@Autowired
	private BusinessPermService businessPermService;
	
	@Autowired
	private MemberService memberService;
	
	// 註冊成為商家
	@GetMapping("/registerBusiness")
	public String registerBusiness(HttpSession session, Model model) {
		Integer memberId = (Integer)session.getAttribute("member");
        if (memberId == null) { // 檢查 member 是否為 null
            // 導向登入會員
            return "redirect:/login";
        }
        BusinessMember bMember = new BusinessMember();
        model.addAttribute("businessMember",bMember);
        return "frontend/registerBusiness";
	}
	
	@PostMapping("/applyForBusiness")
	public String applyForBusiness(@ModelAttribute BusinessMember bMember,HttpSession session) {
		//TODO: 做資料驗證(地址那些)
		bMember.setStatus(Byte.valueOf((byte)0));
		BusinessMember dataBMember = businessService.save(bMember);
		Member member = memberService.findById((Integer)session.getAttribute("member"));
		businessPermService.register(member,dataBMember);
		return "homepage";
	}
	
	// 導向: 員工管理
	@GetMapping("/staff_management")
	public String goManageStaff(HttpSession session, Model model) {
		BusinessMember bm = (BusinessMember)session.getAttribute("presentBusinessMember");
		Integer memberId = (Integer)session.getAttribute("member");
		if(!businessPermService.findByPK(memberId, bm.getId()).isAdminPerm()) {
			model.addAttribute("errors", "僅有具有管理員權限者方可查看");
			return "/error";
		};
		List<BusinessPerm> bPerms = businessPermService.getAllByBusinessMember(bm.getId());
		model.addAttribute("bPerms", bPerms);
		return "business/staff_management";
	}
	
	// 更新員工權限
	@PostMapping("/updatePerms")
	public String updateStaff(@RequestParam Map<String, String> formData, HttpSession session) {
	    // 使用 Map<Integer, Map<String, Boolean>> 來儲存解析的數據
	    Map<Integer, Map<String, Boolean>> staffPerms = new HashMap<>();

	    formData.forEach((key, value) -> {
	        // 提取 ID 和權限名稱
	        String[] parts = key.split("\\[|\\]");
	        if (parts.length == 2) {
	            Integer memberId = Integer.parseInt(parts[0]);
	            String permType = parts[1];

	            // 初始化該 ID 的權限數據結構
	            staffPerms.putIfAbsent(memberId, new HashMap<>());

	            // 儲存權限，"on" 對應 true
	            staffPerms.get(memberId).put(permType, "on".equals(value));
	        }
	    });

	    // 從資料庫中重新獲取 BusinessMember
	    BusinessMember businessMember = (BusinessMember)session.getAttribute("presentBusinessMember");
	    Integer businessMemberId=businessMember.getId();
	    
	    // 將解析後的數據轉換為 List<BusinessPerm>
	    List<BusinessPerm> businessPerms = new ArrayList<>();

	    for (Map.Entry<Integer, Map<String, Boolean>> entry : staffPerms.entrySet()) {
	        Integer memberId = entry.getKey();
	        Map<String, Boolean> perms = entry.getValue();

	        // 設定PK
	        BusinessPerm.BusinessPermComposite compositeId = new BusinessPerm.BusinessPermComposite(businessMemberId, memberId);	        
	        BusinessPerm businessPerm = businessPermService.findByPK(memberId, businessMemberId);

//	        // BusinessMember 和 Member 的設置
//	        Member member = memberService.findById(memberId);
//	        businessPerm.setBusinessMember(businessMember);
//	        businessPerm.setMember(member);
	        
	        // 設定權限
	        businessPerm.setEvtPerm(perms.getOrDefault("evtPerm", false));
	        businessPerm.setPrdPerm(perms.getOrDefault("prdPerm", false));
	        businessPerm.setVnePerm(perms.getOrDefault("vnePerm", false));

	        businessPerms.add(businessPerm);
	    }

	    // 更新資料庫
	    businessPermService.saveAll(businessPerms);
	    

	    return "redirect:/business/staff_management";
	}
	
}
