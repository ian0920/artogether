package com.artogether.controller.eason;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.artogether.common.business_member.BusinessMember;
import com.artogether.common.business_member.BusinessService;
import java.util.List;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/prdMall/member")

public class BusinessMemberManageController {
    @Autowired
    BusinessService businessService;
    
    @GetMapping("/memberdetails")
    public String viewMember(@RequestParam Integer id, Model model) {
        BusinessMember member = businessService.findById(id);
        if (member == null) {
            throw new RuntimeException("Member not found with id: " + id); // 防止 null 返回
        }
        model.addAttribute("member", member);
         return "prdMall/member/memberdetails"; // 對應 memberdetails.html
    }
    
    // 顯示會員編輯頁面
    @GetMapping("/businessmemberedit")
    public String editMember(@RequestParam Integer id, Model model) {
        BusinessMember member = businessService.findById(id);
        if (member == null) {
            throw new RuntimeException("Member not found with id: " + id);
        }
        model.addAttribute("member", member);
        return "prdMall/member/businessmemberedit"; // 對應 businessmemberedit.html
    }

    // 保存編輯後的會員資料
    @PostMapping("/businessmemberedit")
    public String saveMember(@ModelAttribute BusinessMember member) {
        businessService.save(member);
        return "redirect:/prdMall/member/memberdetails?id=" + member.getId(); // 保存後返回詳細頁
    }
}


	


