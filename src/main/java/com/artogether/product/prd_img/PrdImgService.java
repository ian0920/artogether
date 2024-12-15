package com.artogether.product.prd_img;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.artogether.product.product.Product;
import com.artogether.product.product.ProductRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PrdImgService {
	
	@Autowired
    private  PrdImgRepository prdImgRepository;
	@Autowired
    private  ProductRepository productRepository;
    
    

    public void saveImageForProduct(Integer productId, MultipartFile imageFile) {
        
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("商品不存在，ID: " + productId));

        try {
            
            PrdImg prdImg = new PrdImg();
            prdImg.setImageFile(imageFile.getBytes()); 
            prdImg.setProduct(product); 

            
            prdImgRepository.save(prdImg);
        } catch (IOException e) {
            throw new RuntimeException("圖片儲存失敗", e);
        }
    }
    
    

    

    // 將 PrdImg 轉換為 PrdImgDto
    private PrdImgDto toDto(PrdImg prdImg) {
        return new PrdImgDto(
            prdImg.getId(),
            prdImg.getImageFile(),
            prdImg.getProduct() != null ? prdImg.getProduct().getId() : null
        );
    }

    // 將 PrdImgDto 轉換為 PrdImg
    private PrdImg toEntity(PrdImgDto prdImgDto) {
        PrdImg prdImg = new PrdImg();
        prdImg.setId(prdImgDto.getId());
        prdImg.setImageFile(prdImgDto.getImageFile());
        
        if (prdImgDto.getProductId() != null) {
            Product product = new Product();
            product.setId(prdImgDto.getProductId());
            prdImg.setProduct(product);
        }
        return prdImg;
    }

    // 查詢單個 PrdImg 並轉換為 PrdImgDto
    public PrdImgDto findById(Integer id) {
        PrdImg prdImg = prdImgRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("PrdImg not found"));
        return toDto(prdImg);
    }

    // 保存 PrdImgDto，轉換為 PrdImg 實體後存儲
    public PrdImgDto save(PrdImgDto prdImgDto) {
        PrdImg prdImg = toEntity(prdImgDto);
        PrdImg savedPrdImg = prdImgRepository.save(prdImg);
        return toDto(savedPrdImg);
    }

    // 查詢所有 PrdImg 並轉換為 PrdImgDto 列表
    public List<PrdImgDto> findAll() {
        List<PrdImg> prdImgs = prdImgRepository.findAll();
        return prdImgs.stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }
    
    
}

