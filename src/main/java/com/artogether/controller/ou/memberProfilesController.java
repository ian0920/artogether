package com.artogether.controller.ou;

import com.artogether.common.member.Member;
import com.artogether.common.member.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class memberProfilesController {

    @Autowired
    private MemberService memberService;

    @GetMapping("/memberprofiles")
    public String memberProfiles(HttpSession session, Model model) {
        Integer memberId = (Integer) session.getAttribute("memberId");
        Member member = memberService.findById(memberId);
        model.addAttribute("member", member);
        return "/platform";
    }

    @PostMapping("/profoile")
    public String profoileupdate(Member member, Model model) {

        Member updatedMember = memberService.save(member);
        model.addAttribute("member", updatedMember);
        return "/memberprofiles";
    }

    @GetMapping("/allMember")
    public String allMember(Model model) {
        List<Member> members = memberService.findAll();
        model.addAttribute("members", members);
        return "/memberprofiles";
    }

//    @PostMapping("mbupdate")
//    public String mbupdate(Member member, Model model) {
//        memberService.save(member);
//        List<Member> members = memberService.findAll();
//        model.addAttribute("members", members);
//        return "redirect:/memberprofiles";
//    }

        @PostMapping("mbupdate")
        public String mbupdate(Member member, Model model) {
            // id抓狀態 狀態更新 儲存狀態
            memberService.statusUpdate(member);
            List<Member> members = memberService.findAll();
            model.addAttribute("members", members);
            return "redirect:/memberprofiles";
        }
}
