package com.artogether.product.cart.model;

import com.artogether.common.member.Member;
import com.artogether.product.my_prd_coup.MyPrdCoup;
import com.artogether.product.my_prd_coup.MyPrdCoupDaoImpl;
import com.artogether.product.my_prd_coup.MyPrdCoupRepository;
import com.artogether.product.prd_coup.PrdCoup;
import com.artogether.product.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final MyPrdCoupRepository myPrdCoupRepository;
    private final MyPrdCoupDaoImpl myPrdCoupDaoImpl;

    @Autowired
    public CartService(CartRepository cartRepository, MyPrdCoupRepository myPrdCoupRepository, MyPrdCoupDaoImpl myPrdCoupDaoImpl) {
        this.cartRepository = cartRepository;
        this.myPrdCoupRepository = myPrdCoupRepository;
        this.myPrdCoupDaoImpl = myPrdCoupDaoImpl;
    }

    // 添加到購物車
    @Transactional
    public Cart addProductToCart(Member member, Product product, Integer qty){
        if (member == null || product == null || qty == null || qty <= 0) {
            throw new IllegalArgumentException("Invalid input for adding product to cart");
        }
        Cart existingCart = cartRepository.findByProductAndMember(product, member);
        if(existingCart != null){
            existingCart.setQty(existingCart.getQty() + qty);
            return cartRepository.save(existingCart);
        }
        Cart newCart = new Cart();
        newCart.setMember(member);
        newCart.setProduct(product);
        newCart.setQty(qty);
        return cartRepository.save(newCart);
    }

    // 從購物車移除
    @Transactional
    public void removeProductFromCart(Member member, Product product){
        Cart existingCart = cartRepository.findByProductAndMember(product, member);
        if(existingCart != null){
            cartRepository.delete(existingCart);
        }
    }

    // 減少商品數量
    @Transactional
    public void decreaseProductQty(Member member, Product product, int qty) {
        Cart existingCart = cartRepository.findByProductAndMember(product, member);
        if (existingCart != null) {
            int currentQty = existingCart.getQty();
            if (currentQty > qty) {
                existingCart.setQty(currentQty - qty);
                cartRepository.save(existingCart); // 更新數量
            } else {
                cartRepository.delete(existingCart); // 如果數量不足，刪除整個記錄
            }
        }
    }

    // 查看會員購物車
    public List<Cart> getCartByMember(Member member){
        return cartRepository.findByMember(member);
    }

    // 清空購物車
    @Transactional
    public void clearCart(Member member){
        cartRepository.deleteByMember(member);
    }

    // 更新購物車
    public Cart updateCart(Cart cart){
        return cartRepository.save(cart);
    }

    // 計算購物車總金額
    public int getTotalPriceInCart(Member member){
        return cartRepository.findByMember(member).stream()
                .mapToInt(cart -> cart.getProduct().getPrice() * cart.getQty())
                .sum();
    }

        // 獲取會員可用優惠券
    public List<PrdCoup> getMyPrdCoupByMember(Member member){
        return myPrdCoupDaoImpl.findByMemberIdAndStatus(member.getId(), 0).stream()
                .map(MyPrdCoup::getPrdCoup)
                .filter(this::isAvailable)
                .collect(Collectors.toList());

    }


    // 計算套用優惠券的最終金額
    public int finalPriceWithCoupon(Member member, PrdCoup prdCoup) {
        if (member == null || prdCoup == null) {
            throw new IllegalArgumentException("Invalid member or coupon");
        }

        int totalPrice = getTotalPriceInCart(member);
        if (!isAvailable(prdCoup)) {
            throw new IllegalArgumentException("Coupon is not available");
        }

        int finalPrice = switch (prdCoup.getType()) {
            case 1 -> totalPrice - prdCoup.getDeduction(); //固定金額
            case 2 -> (int) (totalPrice * (100 - prdCoup.getRatio().doubleValue() / 100.0));
            default -> throw new IllegalArgumentException("Unknown coupon type");
        };
        return Math.max(0, finalPrice);
    }

    // 檢查優惠券是否有效
    private boolean isAvailable(PrdCoup prdCoup){
        Timestamp now = new Timestamp(System.currentTimeMillis());
        return prdCoup.getStatus() == 1 &&
                (prdCoup.getStartDate() == null || prdCoup.getStartDate().getTime() <= now.getTime()) &&
                (prdCoup.getEndDate() == null || prdCoup.getEndDate().getTime() > now.getTime());
    }

//    public int getTotalItemsInCart(Member member){
//        return cartRepository.findByMemberAndQtyGreaterThan(member, 0).size();
//    }
}
