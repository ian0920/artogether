package com.artogether.product.prd_img;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrdImgDto {

    private Integer id;       
    private byte[] imageFile; 
    private Integer productId; 
}
