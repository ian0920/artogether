package com.artogether.controller.GC;


import com.artogether.controller.GC.model.BusinessDTO;
import com.artogether.product.prd_order.model.PrdOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping ("/business")
public class BusinessWEIController {


    @Autowired
    public BusinessWEIService businessService;

    @GetMapping("/getBusinessDTOList")
    public ResponseEntity<?> getBusinessDTOList(Model model) {
        List<BusinessDTO> businessDTOList = businessService.getBusinessDTOList();

        model.addAttribute("businessDTOList", businessDTOList);
        return ResponseEntity.ok(businessDTOList);
    }

    @GetMapping("/getBusinessDTOList/{businessId}")
    public ResponseEntity<?> getBusinessDTOListByBs(@PathVariable Integer businessId, Model model) {
        List<BusinessDTO> businessDTOList = businessService.getOrderByBusinessId(businessId);
        model.addAttribute("businessDTOList", businessDTOList);

        return ResponseEntity.ok(businessDTOList);
    }


//    @GetMapping("/businessOrderDetail/{orderId}")
//    public ResponseEntity<?> businessOrderDetail(@PathVariable Integer orderId, Model model, HttpSession session) {
//        PrdOrder order = prdOrderService.getOrderById(orderId);
//        List<BusinessDTO> businessDTOList = businessService.getOrderByBusinessId(orderId);
//        model.addAttribute("businessDTOList", businessDTOList);
//
//        return "product/businessOrderDetail";
//    }


}
