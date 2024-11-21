package com.artogether.product.prd_order_detail;

import com.artogether.product.Entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, OrderDetail.CompositeKey> {
}
