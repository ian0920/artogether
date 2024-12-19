package com.artogether.controller.GC;


import com.artogether.common.business_member.BusinessMember;
import com.artogether.controller.GC.model.BusinessDTO;
import com.artogether.product.prd_img.PrdImg;
import com.artogether.product.prd_img.PrdImgRepository;
import com.artogether.product.prd_order.model.PrdOrder;
import com.artogether.product.prd_order.model.PrdOrderRepository;
import com.artogether.product.prd_order.model.PrdOrderService;
import com.artogether.product.prd_order_detail.PrdOrderDetail;
import com.artogether.product.prd_order_detail.PrdOrderDetailRepository;
import com.artogether.product.prd_order_detail.PrdOrderDetailService;
import com.artogether.product.product.Product;
import com.artogether.product.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {
    private final PrdOrderService prdOrderService;
    private final PrdOrderDetailRepository prdOrderDetailRepository;

    @Autowired
    private PrdImgRepository prdImgRepository;

    @Autowired
    private BusinessWEIService businessService;

    @Autowired
    public OrderController(PrdOrderService prdOrderService, PrdOrderDetailService prdOrderDetailService, PrdOrderDetailRepository prdOrderDetailRepository, PrdOrderRepository prdOrderRepository, ProductService productService) {
        this.prdOrderService = prdOrderService;
        this.prdOrderDetailRepository = prdOrderDetailRepository;
    }

    @GetMapping("/page")
    public String orderPage(HttpSession session, Model model) {

        Integer memberId = (Integer) session.getAttribute("member");
        System.out.println("Session memberId: " + memberId);
        if (memberId == null) {
            return "redirect:/login";
        }

        List<PrdOrder> orderList = prdOrderService.getOrderByMemberId(memberId);
        if (orderList == null) {
            orderList = new ArrayList<>();
        }


        model.addAttribute("orderList", orderList);


        return "product/memberOrder";
    }

    @GetMapping("/detail/{orderId}")
    public String orderDetailPage(@PathVariable Integer orderId, Model model, HttpSession session) {
        PrdOrder order = prdOrderService.getOrderById(orderId);
        if (order == null) {
            // 如果订单不存在，重定向或返回错误页面
            return "redirect:/error";
        }
        List<PrdOrderDetail> orderDetail = prdOrderDetailRepository.findByPrdOrder(order);
        orderDetail.forEach((item -> {
            setProductsImg(item.getProduct());
        }));
        model.addAttribute("order", order);
        model.addAttribute("orderDetail", orderDetail);
        return "product/memberOrderDetail";
    }

    public void setProductsImg(Product products) {
        // 获取图片列表并处理可能为空的情况
        List<PrdImg> prdImgs = prdImgRepository.getPrdImgByProductId(products.getId());
        if (prdImgs != null && !prdImgs.isEmpty()) {
            byte[] prdImgData = prdImgs.get(0).getImageFile();
            if (prdImgData != null) {
                String base64Img = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(prdImgData);
                products.setImg(base64Img); // 假设 `img` 字段已更改为 `String`
            }
        }
    }


    // 以下為測試
//    @GetMapping("/businessOrder")
//    public String businessOrder(Model model) {
//
//
//        List<PrdOrder> orderList = prdOrderService.getOrderByMemberId(8);
//        if (orderList == null) {
//            orderList = new ArrayList<>();
//        }
//
//
//        model.addAttribute("orderList", orderList);
//
//
//        return "product/businessOrder";
//    }
    @GetMapping("/businessOrder")
    public String getBusinessDTOListByBs(HttpSession session, Model model) {
        BusinessMember businessId = (BusinessMember) session.getAttribute("presentBusinessMember");
        System.out.println("Session businessId: " + businessId);
        if (businessId != null && businessId.getId() != null) {
            List<BusinessDTO> businessDTOList = businessService.getOrderByBusinessId(businessId.getId());
            model.addAttribute("businessDTOList", businessDTOList);
        } else {
            System.out.println("businessId is null");
        }

        return "/product/businessOrder";
    }


    @GetMapping("/businessOrderDetail/{orderId}")
    public String businessOrderDetail(@PathVariable Integer orderId, Model model, HttpSession session) {

        BusinessMember businessId = (BusinessMember) session.getAttribute("presentBusinessMember");

        List<BusinessDTO> businessDTOList = businessService.getOrderByBusinessId(businessId.getId());
        PrdOrder order = businessDTOList.stream()
                .flatMap(businessDTO -> businessDTO.getPrdOrderDetails().stream())
                .map(PrdOrderDetail::getPrdOrder)
                .filter(prdOrder -> prdOrder.getId().equals(orderId))
                .findFirst()
                .orElse(null);

        model.addAttribute("prdOrder", order);
        model.addAttribute("businessDTOList", businessDTOList);

        return "product/businessOrderDetail";
    }

    @PostMapping("/editOrder/{id}")
    public String updateOrder(
            @PathVariable Integer id,
            @RequestParam("status") String status,
            @RequestParam("shipDate") String shipDateStr,
            HttpSession session) {
        try {
            // 解析前端的 datetime-local 格式字符串為 LocalDateTime
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            LocalDateTime localDateTime = LocalDateTime.parse(shipDateStr, formatter);

            // 轉換為 java.sql.Timestamp
            Timestamp shipDate = Timestamp.valueOf(localDateTime);


            // 從數據庫中查找現有的訂單
            PrdOrder existingOrder = prdOrderService.getOrderById(id);
            if (existingOrder == null) {
                return "redirect:/error";
            }

            // 更新訂單狀態和運送日期
            existingOrder.setStatus(status);
            existingOrder.setShipDate(shipDate);

            // 保存更新的訂單
            prdOrderService.savePrdOrder(existingOrder);

            return "redirect:/order/businessOrder";
        } catch (Exception e) {
            return "redirect:/error";
        }
    }


//    @GetMapping("/detail/{orderId}")
//    public String orderDetailPage(@PathVariable Integer orderId, Model model) {
//        PrdOrder order = prdOrderService.getOrderById(orderId);
//        if (order == null) {
//            // 如果订单不存在，重定向或返回错误页面
//            return "redirect:/error";
//        }
//        List<PrdOrderDetail> orderDetailList = prdOrderService.getOrderByMemberId(orderId);
//        model.addAttribute("orderDetailList", orderDetailList);
//        return "product/orderDetail";
//    }


}
