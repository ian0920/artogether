package com.artogether.product.prd_order_detail;


import com.artogether.product.Prd_Order_Product_Id;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository extends JpaRepository<Prd_Order_Detail, Prd_Order_Product_Id> {
}
