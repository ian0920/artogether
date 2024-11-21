package com.artogether.product.prd_order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Prd_Order, Integer> {
    // 自定義方法，根據狀態查詢訂單
    List<Prd_Order> findByStatus(String status);
}
