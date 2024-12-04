package com.artogether.controller.eason;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/PrdCoup/coupons")
public class PrdCoupViewController {

    @GetMapping
    public String viewPrdCoupPage() {
        return "PrdCoup/html/PrdCoup"; // 返回位於 templates/product/html/ 的 PrdCoup.html
    }
}