package com.artogether.product.cart.controller;

import com.artogether.common.member.Member;
import com.artogether.product.cart.model.Cart;
import com.artogether.product.cart.model.CartService;
import com.artogether.product.prd_coup.PrdCoup;
import com.artogether.product.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add")
    public Cart addProductToCart(@RequestParam Integer memberId, @RequestParam Integer productId, @RequestParam Integer qty) {
        Member member = new Member();
        member.setId(memberId);
        Product product = new Product();
        product.setId(productId);
        return cartService.addProductToCart(member, product, qty);

    }

    @DeleteMapping("/remove/{memberId}/{productId}")
    public void removeProductFromCart(@PathVariable Integer memberId, @PathVariable Integer productId) {
        Member member = new Member();
        member.setId(memberId);
        Product product = new Product();
        product.setId(productId);
        cartService.removeProductFromCart(member, product);
    }

    @GetMapping("/{memberId}")
    public List<Cart> getCartByMember(@PathVariable Integer memberId) {
        Member member = new Member();
        member.setId(memberId);
        return cartService.getCartByMember(member);
    }

    @DeleteMapping("/clear/{memberId}")
    public void clearCart(@PathVariable Integer memberId) {
        Member member = new Member();
        member.setId(memberId);
        cartService.clearCart(member);
    }

    @PutMapping("/update")
    public Cart updateCart(@RequestBody Cart cart) {
        return cartService.updateCart(cart);
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



}
