package com.artogether.product.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Integer id;
    private String name;
    private Integer price;
    private Integer qty;
    private String description;
    private Integer status;
    private String businessMemberName; 
    private Integer businessMemberId;
    
}

