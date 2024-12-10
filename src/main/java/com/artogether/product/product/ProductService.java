package com.artogether.product.product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    // 新增商品
    Product createProduct(Product product);

    // 更新商品
    Product updateProduct(Integer id, Product updatedProduct);

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
    
    
    
}

