package com.artogether.product.product;

import com.artogether.common.business_member.BusinessMember;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ProductService {

    // 新增商品
    Product createProduct(Product product);

    // 更新商品
    Product updateProduct(Integer id, Product updatedProduct, List<MultipartFile> images, HttpSession session) throws IOException;

    // 根據ID查詢商品
    Optional<Product> getProductById(Integer id);

    // 查詢所有商品
    List<Product> getAllProducts();

    // 根據ID刪除商品
    void deleteProduct(Integer id);

    // 查詢上架的商品
    List<Product> findAvailableProducts();
    
    ProductDto toProductDto(Product product);
    
    List<ProductDto> toProductDtoList(List<Product> products);


    List <Product> getProductsByBusinessMemberId(Integer businessMemberId);

    Product addProduct(Product product, List<MultipartFile> images, HttpSession session) throws IOException;

}

