package com.artogether.controller.ou;

import com.artogether.common.platform_msg.PlatformMsg;
import com.artogether.common.platform_msg.PlatformMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/platformMsg")
public class PlatformMsgController {

    @Autowired
    private PlatformMsgService platformMsgService;

    // 查全部訊息
    @GetMapping("/pfmessage")
    public String allMsg(Model model) {
        List<PlatformMsg> platformMsgs = platformMsgService.findAll();
        model.addAttribute("platformMsgs", platformMsgs);
        return "platform/platformMsg";
    }



}
