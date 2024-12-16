package com.artogether.controller.ou;

import com.artogether.common.member.Member;
import com.artogether.common.member.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/profile")
public class memberProfilesController {

    @Autowired
    private MemberService memberService;

    // 會員個人資料管理
    @GetMapping("/memberprofiles")
    public String memberProfiles(HttpSession session, Model model) {
        Integer memberId = (Integer) session.getAttribute("memberId");
        Member member = memberService.findById(memberId);
        model.addAttribute("member", member);
        return "platform/test";
    }

    // 會員個人資料更新
    @PostMapping("/profoiles")
    public String profoileupdate(Member member, Model model) {
        Member updatedMember = memberService.save(member);
        model.addAttribute("member", updatedMember);
        return "platform/test";
    }

    // 顯示全部會員
    @GetMapping("/allMembers") // 網址
    public String allMember(Model model) {
        List<Member> members = memberService.findAll();

        model.addAttribute("memberAll", members);
        return "platform/allMember"; //位置
    }

//    @PostMapping("mbupdate")
//    public String mbupdate(Member member, Model model) {
//        memberService.save(member);
//        List<Member> members = memberService.findAll();
//        model.addAttribute("members", members);
//        return "redirect:/memberprofiles";
//    }

    // 會員狀態更新
    @PostMapping("mbupdate")
    public String mbupdate(Member member, Model model) {
        // id抓狀態 狀態更新 儲存狀態
        memberService.statusUpdate(member);
        List<Member> members = memberService.findAll();
        model.addAttribute("members", members);
        return "redirect:/memberprofiles";
    }
}
