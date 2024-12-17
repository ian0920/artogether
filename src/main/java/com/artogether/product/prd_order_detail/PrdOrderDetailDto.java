package com.artogether.product.prd_order_detail;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class PrdOrderDetailDto {
	private Integer productId;    // 商品 ID
    private String productName;   // 商品名稱
    private Integer qty;          // 數量
    private Integer price;        // 單價
	

}
