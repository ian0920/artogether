package com.artogether.controller.ou;

import com.artogether.common.member.Member;
import com.artogether.common.member.MemberService;
import com.mysql.cj.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/profile")
public class memberProfilesController {

    @Autowired
    private MemberService memberService;

    /*============
     會員個人資料管理
     =============*/
    @GetMapping("/memberProfiles")
    public String memberProfiles(HttpSession session, Model model) {

        Integer memberId =(Integer) session.getAttribute("member");
//        System.out.println(memberId);

        Member member = memberService.findById(memberId);
        model.addAttribute("member", member);
//        System.out.println(member.getName());
        return "platform/memberProfiles";
    }

    /*============
     會員個人資料更新
     =============*/
    @GetMapping("/memberProfiles1")
    public String profileUpdate(String phone, Model model, HttpSession session) {
        Integer memberId =(Integer) session.getAttribute("member");
        Member member = memberService.findById(memberId);
        member.setPhone(phone);
        Member updatedMember = memberService.save(member);
        model.addAttribute("member", updatedMember);
        return "platform/memberProfiles";
//        return "redirect:/profile/memberProfiles";
    }

    /*======================================================================*/


    /*=========
     顯示全部會員
     ==========*/
    @GetMapping("/allMembers") // 網址
    public String allMember(Model model) {
        List<Member> members = memberService.findAll();

        model.addAttribute("memberAll", members);
        return "platform/allMember"; //位置
    }


    /*============
     登入會員個人資料
     ============*/
//    @PostMapping("mbProfile")
//    public String memberProfile(Member member, Model model) {
//        memberService.save(member);
//        List<Member> members = memberService.findAll();
//        model.addAttribute("members", members);
//        return "redirect:/memberprofiles";
//    }

    /*=====================
     更新會員個人狀態(啟用/停權)
     ======================*/
    @GetMapping("mbUpdate")
    public String mbUpdate(@RequestParam Integer id, Model model) {
        // id抓狀態 狀態更新 儲存狀態
//        System.out.println(id);
        Member member = memberService.findById(id);
        if (member.getStatus() == 0) {
            member.setStatus((byte) 1);
        }else {
            member.setStatus((byte) 0);
        }
        memberService.statusUpdate(member);
        List<Member> members = memberService.findAll();
        model.addAttribute("memberAll", members);
//        return "platform/allMember";
        return "redirect:/profile/allMembers";
    }
}
