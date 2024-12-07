package com.artogether.product.prd_order.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrdOrderRepository extends JpaRepository<PrdOrder, Integer> {
//    PrdOrder findByOrderId(Integer orderId);

    // 自定義方法，根據狀態查詢訂單
    List<PrdOrder> findByStatus(String status);

    List<PrdOrder> findByMemberId(Integer memberId);

//    List<PrdOrder> findByOrderDate(String start, String end);

    List<PrdOrder> findByPaymentMethod (String paymentMethod);
}
