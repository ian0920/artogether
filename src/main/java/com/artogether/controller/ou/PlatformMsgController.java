package com.artogether.controller.ou;

import com.artogether.common.platform_msg.PlatformMsg;
import com.artogether.common.platform_msg.PlatformMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/platformMsg")
public class PlatformMsgController {

    @Autowired
    private PlatformMsgService platformMsgService;

    // 查全部訊息
    @GetMapping("/allMessage")
    public String allMsg(Model model) {
        List<PlatformMsg> platformMsg = platformMsgService.findAll();
        model.addAttribute("platformAllMsg", platformMsg);
        return "platform/platformMsg";
    }

    /*=========
     寄出單一會員
     ==========*/

    @GetMapping("/sendMemberMsg")
    public String sendOne(Model model) {
        model.addAttribute("memberMsg", new PlatformMsg());
        return "/platform/sendMemberMsg";  // JSP/HTML file for adding a new system manager
    }

    @PostMapping("/sendMemberMsg")
    public String sendMessage(Integer id, String message) {
        platformMsgService.sendToOne(message, id);
//        platformMsgRepo.save(pms);
//        List<PlatformMsg> memberMessage = platformMsgService.findAll();
//        model.addAttribute("platformMsg", memberMessage);
        return "redirect:/profile/allMembers";
    }

    /*========
     寄出全部會員
     =========*/

    @GetMapping("/sendAllMemberMsg")
    public String sendAllMember(Model model) {
        model.addAttribute("memberAllMsg", new PlatformMsg());
        return "/platform/sendAllMemberMsg";
    }

    @PostMapping("/sendAllMemberMsg")
    public String allMemberMessage(String message, Model model) {
        platformMsgService.sendToAll(message);
        List<PlatformMsg> memberAllMessage = platformMsgService.findAll();
        model.addAttribute("memberAllMsg", memberAllMessage);
        return "redirect:/profile/allMembers";
    }

    /*========
     寄出全部商家
     =========*/

    @GetMapping("/sendAllBusinessMsg")
    public String sendAllBusiness(Model model) {
        model.addAttribute("businessAllMsg", new PlatformMsg());
        return "/platform/sendAllBusinessMsg";
    }

    @PostMapping("/sendAllBusinessMsg")
    public String allBusinessMessage(PlatformMsg platformMsg, Model model) {
        platformMsgService.sendToAllBusinesses(platformMsg.getMessage());
        List<PlatformMsg> businessAllMessage = platformMsgService.findAll();
        model.addAttribute("businessAllMsg", businessAllMessage);
        return "redirect:/profile/allMembers";
    }

    /*===========================
    =           收件匣           =
    =============================*/

    // 會員收件匣
    @GetMapping("memberMessage")
    public String memberMessage(HttpSession session, Model model) {
        Integer memberId = (Integer) session.getAttribute("member");
        List<PlatformMsg> mbMessage = platformMsgService.findByMember(memberId);
        System.out.println(mbMessage.size());
        model.addAttribute("memberMessages", mbMessage);
        return "platform/memberMsg";
    }

    // 商家收件夾
    @GetMapping("/businessMessage")
    public String openBusinessMessage(HttpSession session, Model model) {
        Integer businessId = (Integer) session.getAttribute("businessMember");
        List<PlatformMsg> bnMessage = platformMsgService.findByBusinessMember(businessId);
        model.addAttribute("businessMessage", bnMessage);
        return "platform/businessMsg";
    }

}
