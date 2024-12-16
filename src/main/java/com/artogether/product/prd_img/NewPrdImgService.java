package com.artogether.product.prd_img;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.artogether.product.product.Product;
import com.artogether.product.product.ProductRepository;
import com.artogether.product.prd_img.PrdImg;
import com.artogether.product.prd_img.PrdImgRepository;

@Service
public class NewPrdImgService {

    @Autowired
    private ProductRepository productRepository; // 商品的 Repository

    @Autowired
    private PrdImgRepository prdImgRepository; // 圖片的 Repository

    public void saveImageForProduct(Integer productId, MultipartFile image) throws Exception {
        // 1. 驗證商品是否存在
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new Exception("商品不存在，ID：" + productId));

        // 2. 將圖片轉換為二進制數據
        byte[] imageBytes = image.getBytes();

        // 3. 創建 PrdImg 實例並設置屬性
        PrdImg prdImg = new PrdImg();
        prdImg.setImageFile(imageBytes);
        prdImg.setProduct(product);

        // 4. 保存圖片到數據庫
        prdImgRepository.save(prdImg);
    }
}

