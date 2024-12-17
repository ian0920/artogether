package com.artogether.controller.ian;

import com.artogether.common.member.MemberService;
import com.artogether.common.permission.PermissionService;
import com.artogether.common.system_manager.SystemManager;
import com.artogether.common.system_manager.SystemManagerService;
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

    @Autowired
    private PermissionService permissionService;

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

        List<Integer> p = permissionService.findByDescId(find) ; // 回傳List<Interger>代表這個人擁有的權限

        if (p.isEmpty()){
            errors.add("沒有管理員權限，無法進入頁面");
            model.addAttribute("errors", errors);
            return "platform/login"; // 沒有權限 回到登入頁面
        }
        return "platform/index";
    }

    //管理員登出
    @GetMapping("logout")
    public String logout(HttpSession session){

        session.removeAttribute("managerId");

        return "platform/login";
    }



    //活動款項查核
    @GetMapping("event/accounting")
    public String eventAccounting(HttpSession session){


        return "platform/event_accounting";
    }


}
