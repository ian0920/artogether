package com.artogether.controller.GC;

import com.artogether.common.member.Member;
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
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping ("/order")
public class OrderController {
    private final PrdOrderService prdOrderService;
    private  final PrdOrderDetailService prdOrderDetailService;
    private final PrdOrderDetailRepository prdOrderDetailRepository;
    private final PrdOrderRepository prdOrderRepository;
    private final ProductService productService;

    @Autowired
    private PrdImgRepository prdImgRepository;

    @Autowired
    public OrderController(PrdOrderService prdOrderService, PrdOrderDetailService prdOrderDetailService, PrdOrderDetailRepository prdOrderDetailRepository, PrdOrderRepository prdOrderRepository, ProductService productService) {
        this.prdOrderService = prdOrderService;
        this.prdOrderDetailService = prdOrderDetailService;
        this.prdOrderDetailRepository = prdOrderDetailRepository;
        this.prdOrderRepository = prdOrderRepository;
        this.productService = productService;
    }

    @GetMapping("/page")
    public String orderPage(HttpSession session, Model model) {

        Integer memberId = (Integer) session.getAttribute("member");
        System.out.println("Session memberId: " + memberId);
        if(memberId == null) {
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
