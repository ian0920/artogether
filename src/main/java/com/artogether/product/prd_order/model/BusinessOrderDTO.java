package com.artogether.product.prd_order.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.artogether.product.product.Product;
import com.artogether.product.prd_order_detail.PrdOrderDetail;
import com.artogether.product.prd_order.model.PrdOrder;

@Data
@NoArgsConstructor
public class BusinessOrderDTO {

    private Integer prdOrderId;
    private Integer memberId;
    private String orderDate;
    private String status;
    private Integer totalPrice;
    private String orderName;
    private String orderPhone;
    private String orderAddress;
    private String shipDate;
    private String paymentMethod;
    private Integer productId;
    private String productName;
    private Integer productPrice;
    private Integer productQty;
    private String productImg;


    public BusinessOrderDTO(Integer prdOrderId, Integer memberId, String orderDate, String status, Integer totalPrice, String orderName, String orderPhone, String orderAddress, String shipDate, String paymentMethod, Integer productId, String productName, Integer productPrice, Integer productQty, String productImg) {
        this.prdOrderId = prdOrderId;
        this.memberId = memberId;
        this.orderDate = orderDate;
        this.status = status;
        this.totalPrice = totalPrice;
        this.orderName = orderName;
        this.orderPhone = orderPhone;
        this.orderAddress = orderAddress;
        this.shipDate = shipDate;
        this.paymentMethod = paymentMethod;
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productQty = productQty;
        this.productImg = productImg;
    }
}
