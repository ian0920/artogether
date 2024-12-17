package com.artogether.product.product;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.artogether.product.prd_img.PrdImgRepository;

@Service
public class PrdWithImgService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PrdImgRepository prdImgRepository;

    // 獲取所有商品資訊和主要圖片
    public List<NewProductDto> getAllProductsWithImages() {
        List<Product> products = productRepository.findAll();

        return products.stream().map(product -> {
            NewProductDto dto = new NewProductDto();
            dto.setId(product.getId());
            dto.setName(product.getName());
            dto.setPrice(product.getPrice());
            dto.setQty(product.getQty());
            dto.setDescription(product.getDescription());
            dto.setStatus(product.getStatus());
            dto.setBusinessMemberName(product.getBusinessMember().getName());
            dto.setBusinessMemberId(product.getBusinessMember().getId());

            // 處理圖片
            if (product.getPrdImg() != null && !product.getPrdImg().isEmpty()) {
                byte[] imageBytes = product.getPrdImg().get(0).getImageFile();
                String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                dto.setImg(base64Image);
            } else {
                dto.setImg(null); // 沒有圖片時設置為空
            }

            return dto;
        }).collect(Collectors.toList());
    }
}

