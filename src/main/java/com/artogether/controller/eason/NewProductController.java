package com.artogether.controller.eason;

import java.util.List;

import java.util.Optional;


import com.artogether.common.business_member.BusinessMember;
import com.artogether.product.prd_catalog.PrdCatalog;
import com.artogether.product.prd_catalog.PrdCatalogDaoImpl;
import com.artogether.product.prd_catalog.PrdCatalogRepository;
import com.artogether.product.prd_img.PrdImg;
import com.artogether.product.product.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import com.artogether.product.product.Product;
import com.artogether.product.product.ProductDto;
import com.artogether.product.product.ProductService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;


@Controller
@RequestMapping("/product")
public class NewProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private PrdCatalogDaoImpl prdCatalogDaoImpl;
    @Autowired
    private PrdCatalogRepository prdCatalogRepository;
    @Autowired
    private ProductServiceImpl productServiceImpl;

    @GetMapping("/products")
    public String showProductsPage(Model model) {
        List<Product> products = productService.getAllProducts();
        List<ProductDto> productDtos = productService.toProductDtoList(products);
        model.addAttribute("products", productDtos);
        return "product/products";
    }

    
    
    @GetMapping("/product/vendorproducts")
	public String showVendorProductPage(@RequestParam(value = "businessId", required = false) Integer businessId, Model model) {
	    
	    
	    List<Product> products = productService.getAllProducts();

	    //過濾出只需要的商家商品
	    if (businessId != null) {
	        products = products.stream()
	                           .filter(product -> product.getBusinessMember() != null &&
	                                              product.getBusinessMember().getId().equals(businessId))
	                           .collect(Collectors.toList());
	    }

	    List<ProductDto> productDtos = productService.toProductDtoList(products);	
	    
	    System.out.println("商家商品:"+ productDtos);
	    
	    model.addAttribute("vendorproducts", productDtos);
	    
	   
	    
	    return "product/vendorproducts";
	}
}



    @GetMapping("/businessProducts")
    public String getProductsByBusinessId(HttpSession session, Model model) {
        System.out.println("getProductsByBusinessId");
        Object businessMemberObject = session.getAttribute("presentBusinessMember");
        if (businessMemberObject != null && businessMemberObject instanceof BusinessMember) {
            BusinessMember businessMember = (BusinessMember) businessMemberObject;
            System.out.println("businessMember: " + businessMember);
            List<Product> products = productService.getProductsByBusinessMemberId(businessMember.getId());
            model.addAttribute("products", products);
            return "product/businessProducts";
        }
        return "redirect:/login";


    }

    @GetMapping("/addProduct")
    public String showAddProductForm(Model model) {
//        // 如果需要，傳遞初始化數據，例如分類列表
        List<PrdCatalog> catalogs = prdCatalogRepository.findAll();
        model.addAttribute("catalogs", catalogs);

        // 返回指向 Thymeleaf 的模板名稱
        return "/product/addProduct"; // addProduct.html 是新增商品的表單頁面
    }

    @PostMapping("/addProduct")
    public ResponseEntity<String> addProduct(
            @RequestParam("name") String name,
            @RequestParam("price") Integer price,
            @RequestParam("qty") Integer qty,
            @RequestParam("description") String description,
            @RequestParam(value = "catalogId", required = false) Integer catalogId,
            @RequestParam("status") Integer status,
            @RequestParam("images") List<MultipartFile> images,
            HttpSession session
    ) {
        try {
            // 創建新商品實體
            Product product = new Product();
            product.setName(name);
            product.setPrice(price);
            product.setQty(qty);
            product.setDescription(description);
            product.setStatus(status);

            // 如果有商品分類，設置分類
            if (catalogId != null) {
                PrdCatalog catalog = new PrdCatalog();
                catalog.setId(catalogId);
                product.setPrdCatalog(catalog);
            }

            // 使用 ProductService 處理商品和圖片保存
            productService.addProduct(product, images, session);

            return ResponseEntity.ok("商品已成功新增");
//            return "product/addProduct";
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("新增商品失敗: " + e.getMessage());
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
    // 根據 ID 查詢商品
    @GetMapping("/{id}")
    public String getProductById(@PathVariable Integer id, Model model) {
        System.out.println("getProductsById");
        Optional<Product> product = productService.getProductById(id);
        if (product.isPresent()) {
            model.addAttribute("product", product.get());
            return "product/productDetails";
        } else {
            // 添加默认值，避免 Thymeleaf 模板渲染时出现空指针
            model.addAttribute("product", null); // 或者一个空的 Product 对象
            model.addAttribute("errorMessage", "Product not found for ID: " + id);
            return "homepage";
        }


    }

}
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

