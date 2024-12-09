package com.artogether.controller.eason;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.artogether.product.product.Product;
import com.artogether.product.product.ProductDto;
import com.artogether.product.product.ProductService;

@Controller
@RequestMapping("/product")
public class NewProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public String showProductsPage(Model model) {
        
        List<Product> products = productService.getAllProducts();
        
        List<ProductDto> productDtos = productService.toProductDtoList(products);
            
        System.out.println("商品資料：" + productDtos);    
       
        model.addAttribute("products", productDtos);
       
        return "product/products"; 
    }
}

//    // 新增商品
//    @PostMapping("/products")
//    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
//        Product createdProduct = productService.createProduct(product);
//        return ResponseEntity.ok(createdProduct);
//    }
//
//    // 根據 ID 更新商品
//    @PutMapping("/products/{id}")
//    public ResponseEntity<Product> updateProduct(
//            @PathVariable Integer id, 
//            @RequestBody Product product) {
//        Product updatedProduct = productService.updateProduct(id, product);
//        return ResponseEntity.ok(updatedProduct);
//    }
//
//    // 根據 ID 查詢商品
//    @GetMapping("/products/{id}")
//    public ResponseEntity<ProductDto> getProductById(@PathVariable Integer id) {
//        Optional<Product> product = productService.getProductById(id);
//        return product.map(p -> ResponseEntity.ok(productService.toProductDto(p)))
//                      .orElse(ResponseEntity.notFound().build());
//    }
//
////    // 查詢所有商品
////    @GetMapping("/products")
////    public ResponseEntity<List<ProductDto>> getAllProducts() {
////        List<Product> products = productService.getAllProducts();
////        List<ProductDto> productDtos = productService.toProductDtoList(products);
////        return ResponseEntity.ok(productDtos);
////    }
//
//    // 根據 ID 刪除商品
//    @DeleteMapping("/products/{id}")
//    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
//        productService.deleteProduct(id);
//        return ResponseEntity.noContent().build();
//    }
//
////    // 查詢所有上架商品
////    @GetMapping("/products/available")
////    public ResponseEntity<List<ProductDto>> getAvailableProducts() {
////        List<Product> availableProducts = productService.findAvailableProducts();
////        List<ProductDto> productDtos = productService.toProductDtoList(availableProducts);
////        return ResponseEntity.ok(productDtos);
////    }
//}

