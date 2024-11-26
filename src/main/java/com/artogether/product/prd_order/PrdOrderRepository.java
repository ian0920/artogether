package com.artogether.product.prd_order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrdOrderRepository extends JpaRepository<PrdOrder, Integer> {
    // 自定義方法，根據狀態查詢訂單
    List<PrdOrder> findByStatus(String status);
}
