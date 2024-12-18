package com.artogether.product.prd_order_detail;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class PrdOrderDetailDto {
	private Integer productId;    
    private String productName;   
    private Integer qty;          
    private Integer price;       
	private Integer orderId;

}
