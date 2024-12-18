package com.artogether.controller.eason;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.artogether.common.business_member.BusinessMember;
import com.artogether.common.business_member.BusinessService;
import com.artogether.product.prd_order_detail.PrdOrderDetailDto;
import com.artogether.product.prd_return.NewPrdReturnService;
import com.artogether.product.prd_return.PrdReturn;
import com.artogether.product.prd_return.PrdReturnDto;

import java.sql.Timestamp;
import java.util.Collections;
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
    public String getReturnsByBusinessMember(HttpSession session, Model model) {
        BusinessMember presentBusinessMember = (BusinessMember) session.getAttribute("presentBusinessMember");

        if (presentBusinessMember == null) {
            throw new IllegalStateException("未登入商家會員");
        }

        // 調用 service 方法，根據商家會員 ID 獲取退換貨列表
        List<PrdReturnDto> returns = newPrdReturnService.getReturnsForBusiness(presentBusinessMember.getId());
        model.addAttribute("returns", returns);

        return "product/list";
    }

    


    // ==============================
    // 新增退換貨頁面
    // ==============================
    @GetMapping("/add")
    public String showAddReturnForm(@RequestParam("productId")  Integer productId ,HttpSession session, Model model) {
    	Integer memberId = (Integer) session.getAttribute("member");
    	
    	if (memberId == null) {
            throw new IllegalStateException("未登入");
        }

        // 檢查是否登入
        if (memberId == null) {
            throw new IllegalStateException("未登入一般會員");
        }

        PrdReturnDto prdReturnDto = newPrdReturnService.findPrdReturnById(productId);
        List<PrdReturnDto> singleProductList = Collections.singletonList(prdReturnDto);
        
        model.addAttribute("member", memberId);
        model.addAttribute("prdReturn", new PrdReturn());
        model.addAttribute("prdList", singleProductList);
        
        return "product/add";
    }

    @PostMapping("/add")
    public String addPrdReturn(@ModelAttribute PrdReturn prdReturn, RedirectAttributes redirectAttributes) {
        try {
            // 設置當前時間為申請時間
            if (prdReturn.getApplyDate() == null) {
                prdReturn.setApplyDate(new Timestamp(System.currentTimeMillis()));
            }

            PrdReturnDto result = newPrdReturnService.addPrdReturn(prdReturn);
            redirectAttributes.addFlashAttribute("message", "新增成功！ID: " + result.getId());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "新增失敗！" + e.getMessage());
        }
        return "redirect:/product/user_return_list";
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
    public String updatePrdReturn(
            @PathVariable Integer id, 
            @ModelAttribute PrdReturn prdReturn, 
            @RequestParam(value = "updateReturnDate", required = false) String updateReturnDate,
            RedirectAttributes redirectAttributes) {
        try {
            // 根據勾選框決定是否覆蓋 applyDate
            if ("true".equals(updateReturnDate)) {
                prdReturn.setReturnDate(new Timestamp(System.currentTimeMillis()));
            }

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



//一般會員查看退換貨列表
@GetMapping("/user_return_list")
public String getReturnsByGeneralMember(HttpSession session, Model model) {
    // 從 session 取得登入中的一般會員資訊
	Integer memberId = (Integer) session.getAttribute("member");
	
	if (memberId == null) {
        throw new IllegalStateException("未登入");
    }

    // 檢查是否登入
    if (memberId == null) {
        throw new IllegalStateException("未登入一般會員");
    }

    // 調用 service 方法，根據一般會員 ID 獲取退換貨列表
    List<PrdReturnDto> userReturns = newPrdReturnService.getReturnsForMember(memberId);
    model.addAttribute("userReturns", userReturns);

    // 返回視圖頁面 (例如 "returns/user_list.html")
    return "product/user_return_list";
	}


	@GetMapping("/apply")
	public String showAddReturnForm(@RequestParam("orderId") Integer orderId, Model model) {
    // 透過 orderId 查詢訂單詳情
    List<PrdOrderDetailDto> orderDetails = newPrdReturnService.getOrderDetailsByOrderId(orderId);

    if (orderDetails == null || orderDetails.isEmpty()) {
        model.addAttribute("error", "未找到訂單詳情，請確認訂單編號！");
        return "error"; // 返回錯誤頁面
    }

    model.addAttribute("orderDetails", orderDetails); // 訂單詳情
    model.addAttribute("prdReturn", new PrdReturn()); // 退換貨物件
    model.addAttribute("orderId", orderId); // 傳遞 orderId

    return "product/apply"; // 返回 Thymeleaf 頁面
}

// 提交退換貨申請
@PostMapping("/apply")
public String addPrdReturn(@ModelAttribute PrdReturn prdReturn,
                           @RequestParam("orderId") Integer orderId,
                           RedirectAttributes redirectAttributes) {
    try {
        // 設置當前時間為申請時間
        if (prdReturn.getApplyDate() == null) {
            prdReturn.setApplyDate(new Timestamp(System.currentTimeMillis()));
        }
        // 保存退換貨記錄
        PrdReturnDto result = newPrdReturnService.addPrdReturn(prdReturn);

        redirectAttributes.addFlashAttribute("message", "退換貨申請成功！ID: " + result.getId());
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("message", "退換貨申請失敗！" + e.getMessage());
    }
    return "redirect:/product/user_return_list";
}



}


