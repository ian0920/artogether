package com.artogether.controller.ian;

import com.artogether.common.member.MemberService;
import com.artogether.common.system_mamager.SystemManager;
import com.artogether.common.system_mamager.SystemManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("platform")
public class PlatformController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private SystemManagerService systemManagerService;

    @GetMapping("home")
    public String index() {
        //先確認是否有登入

        return "platform/index";
    }


    //平台後臺管理員登入
    @GetMapping("login")
    public String login(){

        return "platform/login";

    }

    //管理員登入驗證
    @PostMapping("login")
    public String loginVerify(SystemManager systemManager, Model model, HttpSession session){

        List<String> errors = new ArrayList<>();
        SystemManager find = systemManagerService.findByAccount(systemManager.getAccount());


        if (find == null){
            errors.add("查無此帳號");
            model.addAttribute("errors", errors);
            return "platform/login";
        } else if (!find.getPassword().equals(systemManager.getPassword())) {
            errors.add("密碼錯誤");
            model.addAttribute("errors", errors);
            return "platform/login";
        }

        session.setAttribute("managerId", find.getId());



        return "platform/index";
    }

    //管理員登出
    @GetMapping("logout")
    public String logout(HttpSession session){

        session.removeAttribute("managerId");

        return "platform/login";
    }


}
