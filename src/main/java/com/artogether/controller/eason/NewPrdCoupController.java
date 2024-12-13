package com.artogether.controller.eason;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.artogether.common.business_member.BusinessMember;
import com.artogether.common.business_member.BusinessService;
import com.artogether.product.prd_coup.NewPrdCoupService;
import com.artogether.product.prd_coup.PrdCoup;
import com.artogether.product.prd_coup.PrdCoupDto;
import com.artogether.product.prd_coup.PrdCoupService;

import java.util.List;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/coupons")
public class NewPrdCoupController {

    @Autowired
    private NewPrdCoupService newPrdCoupService;
    @Autowired
    private BusinessService businessService;
    @Autowired
    private PrdCoupService prdCoupService;
    
    // 優惠券列表頁面
    
    @GetMapping("/list")
    public String getCouponsByBusinessMember(HttpSession session, Model model){
         
        // 嘗試從 Session 中取得當前商家
        BusinessMember presentBusinessMember = (BusinessMember) session.getAttribute("presentBusinessMember");


        // 如果仍然沒有商家，提示用戶需要登入
        if (presentBusinessMember == null) {
            throw new IllegalStateException("未登入且未提供測試商家 ID");
        }

        // 根據當前商家 ID 查詢該商家的優惠券
        List<PrdCoupDto> coupons = newPrdCoupService.getCouponsByMemberId(presentBusinessMember.getId());
        model.addAttribute("coupons", coupons);

//        // 將商家信息傳遞到前端
//        model.addAttribute("presentBusinessMember", presentBusinessMember);
//
//        // 可選：列出其他商家（測試模式不考慮）
//        List<BusinessMember> businessMembers = (List<BusinessMember>) session.getAttribute("businessMembers");
//        model.addAttribute("businessMembers", businessMembers);

        return "coupons/list"; // 返回優惠券管理頁面
    }



    
    // 新增優惠券頁面
    
    @GetMapping("/add")
    public String showAddCouponForm(HttpSession session,Model model) {
        
        BusinessMember presentBusinessMember = (BusinessMember) session.getAttribute("presentBusinessMember");
        
        if (presentBusinessMember == null) {
            throw new IllegalStateException("未登入且未提供測試商家 ID");
        }
        // 傳遞當前商家到模板
        model.addAttribute("presentBusinessMember", presentBusinessMember);

        // 傳遞空的 PrdCoup 對象供表單使用
        model.addAttribute("prdCoup", new PrdCoup());

        return "coupons/add";
    }


    @PostMapping("/add")
    public String addPrdCoup(@ModelAttribute PrdCoup prdCoup, RedirectAttributes redirectAttributes) {
        try {
            PrdCoupDto result = newPrdCoupService.addPrdCoup(prdCoup); // 調用 Service 新增方法
            redirectAttributes.addFlashAttribute("message", "新增成功！ID: " + result.getId());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "新增失敗！" + e.getMessage());
        }
        return "redirect:/coupons/list"; // 新增成功後重定向至列表頁面
    }

    
    // 編輯優惠券頁面
   
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            PrdCoupDto prdCoup = newPrdCoupService.findCoupById(id);
            model.addAttribute("prdCoup", prdCoup);
            List<BusinessMember> vendors = businessService.findAll();
            model.addAttribute("vendors", vendors);
            return "coupons/edit";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/coupons/list";
        }
    }

    @PostMapping("/edit/{id}")
    public String updatePrdCoup(@PathVariable Integer id, @ModelAttribute PrdCoup prdCoup, RedirectAttributes redirectAttributes) {
        try {
        	System.out.println("Received BusinessMemberId: " + prdCoup.getBusinessMember());
            PrdCoupDto result = newPrdCoupService.updatePrdCoup(id, prdCoup);
            redirectAttributes.addFlashAttribute("message", "更新成功！ID: " + result.getId());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "更新失敗！" + e.getMessage());
        }
        return "redirect:/coupons/list";
    }

    
    // 刪除優惠券
    
    @PostMapping("/delete/{id}")
    public String deletePrdCoup(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            newPrdCoupService.deletePrdCoup(id);
            redirectAttributes.addFlashAttribute("message", "刪除成功！");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "刪除失敗！" + e.getMessage());
        }
        return "redirect:/coupons/list";
    }
    
     // 搜尋優惠券

    @GetMapping("/search")
    public String findCouponsByCriteria(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer type,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Integer threshold,
            Model model) {
        List<PrdCoup> coupons = prdCoupService.findCouponsByCriteria(name, type, status, threshold); // 調用 Service 查詢方法
        model.addAttribute("coupons", coupons);
        return "coupons/search"; // 返回查詢結果的 Thymeleaf 模板
    }
    
    
    
    
//    @GetMapping("/search")
//    public String showSearchCouponForm(    		
////    		@RequestParam(required = false) Integer testBusinessId,   	          
//            @RequestParam(required = false) String name,
//            @RequestParam(required = false) Integer type,
//            @RequestParam(required = false) Integer status,
//            @RequestParam(required = false) Integer threshold,
//            HttpSession session,
//            Model model) {
//        
////        BusinessMember presentBusinessMember = (BusinessMember) session.getAttribute("presentBusinessMember");
////        if (presentBusinessMember == null && testBusinessId != null) {
////            presentBusinessMember = businessService.findById(testBusinessId);
////            session.setAttribute("presentBusinessMember", presentBusinessMember);
////        }
////        if (presentBusinessMember == null) {
////            throw new IllegalStateException("未登入且未提供測試商家 ID");
////        }
////
////        // 傳遞當前商家到模板
////        model.addAttribute("presentBusinessMember", presentBusinessMember);
////
////        if (name == null && status == null && type == null && threshold == null) {
////            model.addAttribute("coupons", null);
////            return "coupons/search";
////        }
////        
//        List<PrdCoupDto> coupons = newPrdCoupService.findCouponsByCriteria(
//                name, type, status, threshold);
//        
//        System.out.println("查詢結果數量: " + (coupons != null ? coupons.size() : 0));
//
//                                                                                                                                 
//        model.addAttribute("coupons", coupons);
//
//        return "coupons/search";       
//            }
//        model.addAttribute("vendors", businessService.findAll());
//        PrdCoup searchCriteria = new PrdCoup(); // 創建一個空的對象供模板使用
//        model.addAttribute("searchCriteria", new PrdCoup());


    
    
//    @GetMapping("/list")  如果啟用登入再換這支
//    public String getCouponsByLoggedInMember(HttpSession session, Model model) {
//        // 從 Session 中獲取會員資料
//        BusinessMember loggedInMember = (BusinessMember) session.getAttribute("loggedInMember");
//        if (loggedInMember == null) {
//            throw new IllegalStateException("用戶未登入");
//        }
//
//        Integer memberId = loggedInMember.getId();
//        List<PrdCoupDto> coupons = newPrdCoupService.getCouponsByMemberId(memberId);
//        model.addAttribute("coupons", coupons);
//
//        return "coupons/list";
//    }
    
    


    
    
}
