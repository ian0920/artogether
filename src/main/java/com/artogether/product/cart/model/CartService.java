package com.artogether.product.cart.model;

import com.artogether.common.member.Member;
import com.artogether.product.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class CartService {

    private final CartRepository cartRepository;

    @Autowired
    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    // 添加到購物車
    @Transactional
    public Cart addProductToCart(Member member, Product product, Integer qty){
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

//    public int getTotalItemsInCart(Member member){
//        return cartRepository.findByMemberAndQtyGreaterThan(member, 0).size();
//    }
}
