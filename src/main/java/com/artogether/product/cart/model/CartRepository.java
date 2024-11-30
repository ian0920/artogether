package com.artogether.product.cart.model;


import com.artogether.common.member.Member;
import com.artogether.product.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CartRepository extends JpaRepository<Cart, Cart.CartId> {


    List<Cart> findByMember(Member member);

    Cart findByProductAndMember(Product product, Member member);

    void deleteByMember(Member member);

    // 查詢購物車中數量大於某數量的商品數
    List<Cart> findByMemberAndQtyGreaterThan(Member member, Integer qty);


}
