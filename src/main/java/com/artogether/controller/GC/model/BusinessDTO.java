package com.artogether.controller.GC.model;

import com.artogether.product.prd_order.model.PrdOrder;
import com.artogether.product.prd_order_detail.PrdOrderDetail;
import com.artogether.product.product.Product;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
public class BusinessDTO {

    private Integer businessId;
    private String businessName;
    private Integer prdOrderId;

    private List<PrdOrderDetail> prdOrderDetails;

    public BusinessDTO(Integer businessId, String businessName, Integer prdOrderId) {
        this.businessId = businessId;
        this.businessName = businessName;
        this.prdOrderId = prdOrderId;
    }
}