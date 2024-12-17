package com.artogether.product.prd_order_detail;


import com.artogether.product.prd_order.model.PrdOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrdOrderDetailRepository extends JpaRepository<PrdOrderDetail, PrdOrderProductId> {

    List<PrdOrderDetail> findByPrdOrder(PrdOrder prdOrder);
    List<PrdOrderDetail> findByPrdOrderId(Integer orderId);
}
