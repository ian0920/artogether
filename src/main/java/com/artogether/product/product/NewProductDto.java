package com.artogether.product.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewProductDto {
    private Integer id;                // 商品ID
    private String name;               // 商品名稱
    private Integer price;             // 商品價格
    private Integer qty;               // 庫存數量
    private String description;        // 商品描述
    private Integer status;            // 狀態
    private String businessMemberName; // 商家名稱
    private Integer businessMemberId;  // 商家ID
    private String img;        // Base64格式的圖片 (封面圖片)
}

