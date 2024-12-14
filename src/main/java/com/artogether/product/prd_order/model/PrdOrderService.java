package com.artogether.product.prd_order.model;


import com.artogether.product.prd_order_detail.PrdOrderDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;

@Service("prdOrderService")
public class PrdOrderService {

    private final PrdOrderRepository prdOrderRepository;



    @Autowired
    public PrdOrderService(PrdOrderRepository prdOrderRepository) {
        this.prdOrderRepository = prdOrderRepository;
    }

    @Transactional
    public PrdOrder savePrdOrder(PrdOrder prdOrder) {
        return prdOrderRepository.save(prdOrder);
    }

    public List<PrdOrder> getOrderByStatus(String status) {
        return prdOrderRepository.findByStatus(status);
    }

    public List<PrdOrder> getOrderByMemberId(Integer memberId) {
        return prdOrderRepository.findByMemberId(memberId);
    }

    public List<PrdOrder> getOrdersByPaymentMethod(String paymentMethod) {
        return prdOrderRepository.findByPaymentMethod(paymentMethod);
    }

    public PrdOrder getOrderById(Integer orderId) {
        return prdOrderRepository.findById(orderId).orElse(null);
    }

    public void deleteOrderById(Integer orderId) {
        prdOrderRepository.deleteById(orderId);
    }





}
