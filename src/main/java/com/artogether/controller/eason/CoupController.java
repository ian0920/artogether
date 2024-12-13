package com.artogether.controller.eason;

import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.artogether.common.business_member.BusinessMember;
import com.artogether.common.member.Member;
import com.artogether.product.my_prd_coup.MyPrdCoup;
import com.artogether.product.my_prd_coup.MyPrdCoupDto;
import com.artogether.product.my_prd_coup.NewMyPrdCoupService;
import com.artogether.product.prd_coup.PrdCoupDto;

@Controller
@RequestMapping("/coupons")
public class CoupController {
	
	@Autowired 
	private NewMyPrdCoupService newMyPrdCoupService;
	
	@GetMapping("/membercoupons")
	public String viewCouponsByMemberId(HttpSession session, Model model) {
		
		Integer memberId = (Integer) session.getAttribute("member");
		
		if (memberId == null) {
            throw new IllegalStateException("未登入");
        }
		
	    List<MyPrdCoupDto> coupons = newMyPrdCoupService.findCouponsByMemberId(memberId);
	    
	    if (coupons == null || coupons.isEmpty()) {
	        throw new RuntimeException("No coupons found for member ID: " + memberId); // 如果找不到優惠券
	    }
	    
	    model.addAttribute("coupons", coupons);
	    model.addAttribute("memberId",memberId);
	    
	    return "coupons/membercoupons";
	}

	
	@GetMapping("/available")
	public String getAvailableCoupons(HttpSession session, Model model) {
		
		Integer memberId = (Integer) session.getAttribute("member");
		// 調用 Service 獲取尚未領取的優惠券
		List<PrdCoupDto> availableCoupons = newMyPrdCoupService.getAvailableCoupons(memberId);

		// 將優惠券列表和會員 ID 添加到 Model
		model.addAttribute("coupons", availableCoupons);
		model.addAttribute("memberId", memberId);

		// 返回 Thymeleaf 模板
		return "coupons/available"; // 對應的 HTML 頁面名稱
	}

	@PostMapping("/claim")
	public String claimCoupon(HttpSession session, @RequestParam Integer couponId) {
	    
		Integer memberId = (Integer) session.getAttribute("member");
		// 創建 DTO
	    MyPrdCoupDto dto = new MyPrdCoupDto();
	    dto.setMemberId(memberId);
	    dto.setCouponId(couponId);
	    dto.setStatus(1); // 1 表示未使用
	    dto.setReceiveDate(new Timestamp(System.currentTimeMillis())); // 設置當前時間為領取時間

	    // 調用 Service 保存記錄
	    newMyPrdCoupService.saveFromDto(dto);

	    // 重定向回尚未領取的優惠券頁面
	    return "redirect:/coupons/available?memberId=" + memberId;
	}

    

	
	
	

}
