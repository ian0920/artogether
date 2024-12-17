package com.artogether.product.cart.controller;

import com.artogether.common.member.Member;
import com.artogether.product.cart.model.Cart;
import com.artogether.product.cart.model.CartService;
import com.artogether.product.cart.model.PrdCoupForCartDTO;
import com.artogether.product.prd_coup.PrdCoup;
import com.artogether.product.prd_img.PrdImg;
import com.artogether.product.prd_img.PrdImgRepository;
import com.artogether.product.prd_order.model.PrdOrderService;
import com.artogether.product.prd_order_detail.PrdOrderDetailRepository;
import com.artogether.product.product.Product;
import com.artogether.product.product.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.artogether.product.prd_order.model.PrdOrder;
import com.artogether.product.prd_order_detail.PrdOrderDetail;
import java.sql.Timestamp;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    @Autowired
    private PrdImgRepository prdImgRepository;

    @Autowired
    private ProductServiceImpl productServiceImpl;
    @Autowired
    private PrdOrderDetailRepository prdOrderDetailRepository;


    @Autowired
    public CartController(CartService cartService, PrdOrderService prdOrderService) {
        this.cartService = cartService;
        this.prdOrderService = prdOrderService;
    }

    @PostMapping("/add")
    public String  addProductToCart(@RequestParam(value = "productId", required = true) Integer productId, @RequestParam(value = "qty", required = false, defaultValue = "1") Integer qty, HttpSession session) {
        Integer memberId = (Integer) session.getAttribute("member");

        if (memberId == null) {
            return "redirect:/login";
        }

        Member member = new Member();
        member.setId(memberId);
        Product product = new Product();
        product.setId(productId);
        cartService.addProductToCart(member, product, qty);

        return "redirect:/cart/page";

    }

    @PostMapping("/remove")
    public String removeCartItem(
            @RequestParam Integer productId,
            HttpSession session) {
        Integer memberId = (Integer) session.getAttribute("member");
        if (memberId == null) {
            return "redirect:/login";
        }

        Member member = new Member();
        member.setId(memberId);

        Product product = new Product();
        product.setId(productId);

        cartService.removeProductFromCart(member, product);

        return "redirect:/cart/page";
    }


    @PostMapping("/clear")
    public String clearCart(HttpSession session) {
        Integer memberId = (Integer) session.getAttribute("member");
        if (memberId == null) {
            return "redirect:/login";
        }

        Member member = new Member();
        member.setId(memberId);
        cartService.clearCart(member);

        return "redirect:/cart/page";
    }

    @PostMapping("/updateQty")
    public String updateCartQty(
            @RequestParam Integer productId,
            @RequestParam Integer qty,
            HttpSession session) {
        Integer memberId = (Integer) session.getAttribute("member");
        if (memberId == null) {
            return "redirect:/login";
        }

        Member member = new Member();
        member.setId(memberId);

        Product product = new Product();
        product.setId(productId);

        List<Cart> cartItems = cartService.getCartByMember(member);
        int currentQty = cartItems.stream()
                .filter(cart -> cart.getProduct().getId().equals(productId))
                .mapToInt(Cart::getQty)
                .findFirst()
                .orElse(0);

        int diff = qty - currentQty;

        if (diff > 0) {
            cartService.addProductToCart(member, product, diff);
        } else if (diff < 0) {
            cartService.decreaseProductQty(member, product, -diff);
        }

        return "redirect:/cart/page";
    }

    @GetMapping("/totalPrice/{memberId}")
    public int totalPrice(@PathVariable Integer memberId) {
        Member member = new Member();
        member.setId(memberId);
        return cartService.getTotalPriceInCart(member);
    }

    @GetMapping("/coupons/{memberId}")
    public List<PrdCoup> getAvailableCoupons(@PathVariable Integer memberId) {
        Member member = new Member();
        member.setId(memberId);
        return cartService.getMyPrdCoupByMember(member);
    }

    @GetMapping("/checkout/{memberId}")
    public int checkoutWithCoupon(@PathVariable Integer memberId, @RequestParam Integer couponId) {
        Member member = new Member();
        member.setId(memberId);

        PrdCoup prdCoup = new PrdCoup();
        prdCoup.setId(couponId);
        return cartService.finalPriceWithCoupon(member, prdCoup);
    }

    @GetMapping("/page")
    public String getCartPage(HttpSession session, Model model) {
        Integer memberId = (Integer) session.getAttribute("member"); // 從 HttpSession 獲取 memberId

        System.out.println("Session memberId: " + memberId);
        if (memberId == null) {
            return "redirect:/login"; // 如果未登入，重定向到登入頁面
        }

        Member member = new Member();
        member.setId(memberId);

        // 獲取會員購物車內容
        List<Cart> cartItems = cartService.getCartByMember(member);
        if (cartItems == null) {
            cartItems = new ArrayList<>();
        }
        // 計算總金額
        int totalPrice = cartItems.stream()
                .mapToInt(cart -> cart.getProduct().getPrice() * cart.getQty())
                .sum();

        cartItems.forEach((item -> {
            setProductsImg(item.getProduct());
        }));

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalPrice", totalPrice);



        return "/prdMall/member/cart"; // 返回購物車頁面模板

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

    @PostMapping("/cartCheck")
    public String getCartCheckPage(@RequestParam List<Integer> productsIdInCart, HttpSession session, Model model) {
        Integer memberId = (Integer) session.getAttribute("member"); // 從 HttpSession 獲取 memberId

        if (memberId == null) {
            return "redirect:/login";
        }

        Member member = new Member();
        member.setId(memberId);

        // 獲取會員購物車內容
        List<Cart> cartItems = cartService.getCartByMember(member);
        if (cartItems == null) {
            cartItems = new ArrayList<>();
        }
        // 計算總金額
        int totalPrice = cartItems.stream()
                .mapToInt(cart -> cart.getProduct().getPrice() * cart.getQty())
                .sum();

        //Map<BusinessId, List<PrdCoupForCartDTO>> 商家Id, 該會員對於該商家的所有優惠券資訊

        Map<Integer, List<PrdCoupForCartDTO>> businessMembersInCart =
                productServiceImpl.findAllBusinessMember(productsIdInCart, memberId);

        businessMembersInCart.keySet().forEach(id -> System.out.println(id));


        session.setAttribute("cartItems", cartItems);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("businessMembersInCart", businessMembersInCart);

        return "/prdMall/member/cartCheck"; // 返回購物車頁面模板

    }

    @Autowired
    private PrdOrderService prdOrderService;

    @PostMapping("/confirmOrder")
    public String confirmOrder(
            @RequestParam String orderName,
            @RequestParam String orderPhone,
            @RequestParam String orderAddress,
            @RequestParam String paymentMethod,
            @RequestParam Integer totalPrice,
            @RequestParam Integer discount,
            @RequestParam Integer paid,

            HttpSession session, Model model) {

        System.out.println("orderName: " + orderName);
        System.out.println("orderPhone: " + orderPhone);
        System.out.println("orderAddress: " + orderAddress);
        System.out.println("paymentMethod: " + paymentMethod);
        System.out.println("totalPrice: " + totalPrice);
        System.out.println("discount: " + discount);
        System.out.println("paid: " + paid);

        //從session取出購物車清單 後續方便存入購物明細
        List<Cart> cartItems = (List<Cart>) session.getAttribute("cartItems");

        Integer memberId = (Integer) session.getAttribute("member"); // 從 HttpSession 獲取 memberId
        if (memberId == null) {
            return "redirect:/login"; // 如果未登入，重定向到登入頁面
        }
        Member member = new Member();
        member.setId(memberId);

        PrdOrder order = new PrdOrder();
        order.setMember(member);
        order.setOrderName(orderName);
        order.setOrderPhone(orderPhone);
        order.setOrderAddress(orderAddress);
        order.setPaymentMethod(paymentMethod);
        order.setOrderDate(Timestamp.valueOf(LocalDateTime.now()));
        order.setStatus("未付款");




//        List<Cart> cartItems = cartService.getCartByMember(member);
//        int totalPrice = cartItems.stream()
//                .mapToInt(cart -> cart.getProduct().getPrice() * cart.getQty())
//                .sum();

        order.setTotalPrice(totalPrice);
        order.setDiscount(discount);
        order.setPaid(paid);




        try {
            // 保存訂單
            PrdOrder newOrder = prdOrderService.savePrdOrder(order);

            cartItems.forEach(cart -> {
                PrdOrderDetail orderDetail = new PrdOrderDetail();
                orderDetail.setPrdOrder(newOrder);
                orderDetail.setProduct(cart.getProduct());
                orderDetail.setQty(cart.getQty());
                orderDetail.setPrice(cart.getProduct().getPrice());

                prdOrderDetailRepository.save(orderDetail);
            });



            // 清空購物車
            cartService.clearCart(member);

            // 傳遞訂單信息到頁面
            model.addAttribute("order", order);
            return "/prdMall/member/orderSuccess";
        } catch (Exception e) {
            model.addAttribute("error", "訂單提交失敗，請稍後再試");
            return "/prdMall/member/cartCheck";
        }

//        return "Test_success";
    }

}