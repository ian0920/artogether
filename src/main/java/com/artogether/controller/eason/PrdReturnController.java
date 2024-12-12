package com.artogether.controller.eason;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.artogether.common.business_member.BusinessMember;
import com.artogether.common.business_member.BusinessService;
import com.artogether.product.prd_return.NewPrdReturnService;
import com.artogether.product.prd_return.PrdReturn;
import com.artogether.product.prd_return.PrdReturnDto;

import java.util.List;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/product")
public class PrdReturnController {

    @Autowired
    private NewPrdReturnService newPrdReturnService;
    @Autowired
    private BusinessService businessService;

    // ==============================
    // 退換貨列表頁面
    // ==============================
    @GetMapping("/list")
    public String getReturnsByBusinessMember(
    		@RequestParam(required = false) Integer testBusinessId,
            HttpSession session,  
            Model model) {

        BusinessMember presentBusinessMember = (BusinessMember) session.getAttribute("presentBusinessMember");

        if (presentBusinessMember == null && testBusinessId != null) {
            presentBusinessMember = businessService.findById(testBusinessId);
            if (presentBusinessMember == null) {
                throw new IllegalArgumentException("無效的商家 ID：" + testBusinessId);
            }
            session.setAttribute("presentBusinessMember", presentBusinessMember);
        }

        if (presentBusinessMember == null) {
            throw new IllegalStateException("未登入且未提供測試商家 ID");
        }

        List<PrdReturnDto> returns = newPrdReturnService.getAllReturns();
        model.addAttribute("returns", returns);

        return "product/list";
    }

    // ==============================
    // 新增退換貨頁面
    // ==============================
    @GetMapping("/add")
    public String showAddReturnForm(
            @RequestParam(required = false) Integer testBusinessId,
            HttpSession session,
            Model model) {
        BusinessMember presentBusinessMember = (BusinessMember) session.getAttribute("presentBusinessMember");
        if (presentBusinessMember == null && testBusinessId != null) {
            presentBusinessMember = businessService.findById(testBusinessId);
            if (presentBusinessMember == null) {
                throw new IllegalArgumentException("無效的商家 ID：" + testBusinessId);
            }
            session.setAttribute("presentBusinessMember", presentBusinessMember);
        }
        if (presentBusinessMember == null) {
            throw new IllegalStateException("未登入且未提供測試商家 ID");
        }

        model.addAttribute("presentBusinessMember", presentBusinessMember);
        model.addAttribute("prdReturn", new PrdReturn());

        return "product/add";
    }

    @PostMapping("/add")
    public String addPrdReturn(@ModelAttribute PrdReturn prdReturn, RedirectAttributes redirectAttributes) {
        try {
            PrdReturnDto result = newPrdReturnService.addPrdReturn(prdReturn);
           redirectAttributes.addFlashAttribute("message", "新增成功！ID: " + result.getId());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "新增失敗！" + e.getMessage());
        }
        return "redirect:/product/list";
    }

    // ==============================
    // 編輯退換貨頁面
    // ==============================
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
    	 
    	try {        	        	
            PrdReturnDto prdReturn = newPrdReturnService.findPrdReturnById(id);
            model.addAttribute("prdReturn", prdReturn);
            return "product/edit";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/product/list";
        }
    }

    @PostMapping("/edit/{id}")
    public String updatePrdReturn(@PathVariable Integer id, @ModelAttribute PrdReturn prdReturn, RedirectAttributes redirectAttributes) {
        try {
            PrdReturnDto result = newPrdReturnService.updatePrdReturn(id, prdReturn);
            redirectAttributes.addFlashAttribute("message", "更新成功！ID: " + result.getId());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "更新失敗！" + e.getMessage());
        }
        return "redirect:/product/list";
    }

    // ==============================
    // 刪除退換貨
    // ==============================
    @PostMapping("/delete/{id}")
    public String deletePrdReturn(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            newPrdReturnService.deletePrdReturn(id);
            redirectAttributes.addFlashAttribute("message", "刪除成功！");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "刪除失敗！" + e.getMessage());
        }
        return "redirect:/product/list";
    }

    // ==============================
    // 搜尋退換貨
    // ==============================
    @GetMapping("/search")
    public String showSearchReturnForm(
            @RequestParam(required = false) Integer testBusinessId,
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) Integer orderId,
            @RequestParam(required = false) String reason,
            @RequestParam(required = false) Integer type,
            @RequestParam(required = false) Integer status,
            HttpSession session,
            Model model) {

        BusinessMember presentBusinessMember = (BusinessMember) session.getAttribute("presentBusinessMember");
        if (presentBusinessMember == null && testBusinessId != null) {
            presentBusinessMember = businessService.findById(testBusinessId);
            session.setAttribute("presentBusinessMember", presentBusinessMember);
        }
        if (presentBusinessMember == null) {
            throw new IllegalStateException("未登入且未提供測試商家 ID");
        }

//        model.addAttribute("presentBusinessMember", presentBusinessMember);
//
//        if (orderId == null && reason == null && type == null && status == null) {
//            model.addAttribute("returns", null);
//            return "/product/search";
//        }

        List<PrdReturnDto> returns = newPrdReturnService.findCouponsByCriteria(
                id, orderId, reason, type, status);

        model.addAttribute("returns", returns);
        return "/product/search";
    }
}

