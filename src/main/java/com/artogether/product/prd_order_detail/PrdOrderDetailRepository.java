package com.artogether.product.prd_order_detail;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrdOrderDetailRepository extends JpaRepository<PrdOrderDetail, PrdOrderProductId> {
}
