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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.artogether.common.business_member.BusinessMember;
import com.artogether.common.business_member.BusinessService;


@Controller
@RequestMapping("business")
public class Wynn_BusinessController {
	
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
	
	
}
