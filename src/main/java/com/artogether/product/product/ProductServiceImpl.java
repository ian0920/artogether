package com.artogether.product.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product createProduct(Product product) {
        // 新增商品到資料庫
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Integer id, Product updatedProduct) {
        // 根據 ID 查找商品
        Optional<Product> existingProduct = productRepository.findById(id);
        if (existingProduct.isPresent()) {
            Product product = existingProduct.get();
            product.setName(updatedProduct.getName());
            product.setPrice(updatedProduct.getPrice());
            product.setQty(updatedProduct.getQty());
            product.setDescription(updatedProduct.getDescription());
            product.setStatus(updatedProduct.getStatus());
            product.setAllStars(updatedProduct.getAllStars());
            product.setAllReviews(updatedProduct.getAllReviews());
            product.setPrdCatalog(updatedProduct.getPrdCatalog());
            product.setBusinessMember(updatedProduct.getBusinessMember());
            return productRepository.save(product);
        } else {
            throw new RuntimeException("商品 ID: " + id + " 不存在");
        }
    }

    @Override
    public Optional<Product> getProductById(Integer id) {
        // 根據 ID 查詢商品
        return productRepository.findById(id);
    }

    @Override
    public List<Product> getAllProducts() {
        // 查詢所有商品
        return productRepository.findAll();
    }

    @Override
    public void deleteProduct(Integer id) {
        // 刪除商品
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        } else {
            throw new RuntimeException("商品 ID: " + id + " 不存在，無法刪除");
        }
    }

    @Override
    public List<Product> findAvailableProducts() {
        // 查詢上架的商品 (假設 status = 1 表示上架)
        return productRepository.findAll()
                .stream()
                .filter(product -> product.getStatus() != null && product.getStatus() == 1)
                .toList();
    }
    
    public ProductDto toProductDto(Product product) {
        return new ProductDto(
            product.getId(),
            product.getName(),
            product.getPrice(),
            product.getQty(),
            product.getDescription(),
            product.getStatus(),
            product.getBusinessMember() != null ? product.getBusinessMember().getName() : null,
            product.getBusinessMember() != null ? product.getBusinessMember().getId() : null	
        );
    }
    
    // 列表轉換
    public List<ProductDto> toProductDtoList(List<Product> products) {
        return products.stream()
                       .map(this::toProductDto) // 調用單個轉換方法
                       .collect(Collectors.toList());
    }
    
    
    
}


