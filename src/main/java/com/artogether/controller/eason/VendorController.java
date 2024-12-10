package com.artogether.controller.eason;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.artogether.common.business_member.BusinessMember;
import com.artogether.common.business_member.BusinessService;
import com.artogether.product.product.Product;
import com.artogether.product.product.ProductDto;
import com.artogether.product.product.ProductService;

@Controller
@RequestMapping("/product")
public class VendorController {
	
	@Autowired
	private BusinessService businessService;
	
	@Autowired
	private ProductService productService;
	
	@GetMapping("/vendor")
	
	public String getAllVendor(Model model) {
		List<BusinessMember>vendors = businessService.findAll();
		model.addAttribute("vendors", vendors);
		return "product/vendor";
	}
	
	
	

	


}
