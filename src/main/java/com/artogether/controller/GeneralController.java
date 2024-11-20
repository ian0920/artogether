package com.artogether.controller;

import com.artogether.common.member.Member;
import com.artogether.common.member.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
public class GeneralController {

    @Autowired
    private MemberService memberService;

    @RequestMapping("/jsp")
    public String hello(HttpServletRequest request, HttpServletResponse response) {

        request.setAttribute("message", "It's able to get attribute from request");
        return "forward:/WEB-INF/views/homeTest.jsp";
    }


    @RequestMapping("/")
    public String hello2() {


        return "home";
    }

    @PostMapping("register")
    public String register (@Valid @RequestBody Member member){
        /*
            填寫資料驗證v
            錯誤驗證回傳
            驗證信箱
            驗證碼
        */

        member.setStatus((byte) 0);
        memberService.save(member);


        return "success";
    }

}
