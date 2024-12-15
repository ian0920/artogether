package com.artogether.product.product;

import com.artogether.common.business_member.BusinessMember;
import com.artogether.common.business_member.BusinessMemberRepo;
import com.artogether.product.my_prd_coup.MyPrdCoupRepository;
import com.artogether.product.prd_catalog.PrdCatalogRepository;
import com.artogether.product.prd_img.PrdImg;
import com.artogether.product.prd_img.PrdImgRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    public PrdImgRepository prdImgRepository;

    @Autowired
    public PrdCatalogRepository prdCatalogRepository;

    @Autowired
    public BusinessMemberRepo businessMemberRepository;

    @Autowired
    private MyPrdCoupRepository myPrdCoupRepository;

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
        Optional<Product> productOptional = productRepository.findById(id);

        // 如果商品存在，處理圖片
        productOptional.ifPresent(product -> {
            List<PrdImg> prdImgs = prdImgRepository.getPrdImgByProductId(product.getId());
            if (prdImgs != null && !prdImgs.isEmpty()) {
                byte[] prdImgData = prdImgs.get(0).getImageFile();
                if (prdImgData != null) {
                    // 將字節數組轉換為 Base64 編碼字串符
                    String base64Img = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(prdImgData);
                    product.setImg(base64Img); // 假設 `img` 字段為 `String`
                }
            }
        });

        return productOptional;
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

    // 查找商家商品

    @Override
    public List<Product> getProductsByBusinessMemberId(Integer businessMemberId) {
        List<Product> products = productRepository.findProductListByBusinessId(businessMemberId);
        for (Product product : products) {
            // 獲取圖片列表並處理可能為空的情況
            List<PrdImg> prdImgs = prdImgRepository.getPrdImgByProductId(product.getId());
            if (prdImgs != null && !prdImgs.isEmpty()) {
                byte[] prdImgData = prdImgs.get(0).getImageFile();
                if (prdImgData != null) {
                	// 將字節數組轉換為 Base64 編碼字串符
                    String base64Img = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(prdImgData);
                    product.setImg(base64Img); // 假設 `img` 字段已更給為 `String`
                }
            }
        }
        return products;
    }



    public Product addProduct(Product product, List<MultipartFile> images, HttpSession session) throws IOException {
        // 獲取當前登入的商家
        BusinessMember businessMember = (BusinessMember) session.getAttribute("presentBusinessMember");
        if (businessMember == null) {
            throw new RuntimeException("未登入或商家信息無法獲取");
        }
        product.setBusinessMember(businessMember);

        // 保存商品信息
        Product savedProduct = productRepository.save(product);

        // 保存圖片信息
        List<PrdImg> prdImgs = new ArrayList<>();
        for (MultipartFile image : images) {
            PrdImg prdImg = new PrdImg();
            prdImg.setProduct(savedProduct);
            prdImg.setImageFile(image.getBytes());
            prdImgs.add(prdImg);
        }
        prdImgRepository.saveAll(prdImgs);

        return savedProduct;
    }


    public List<PrdImg> getPrdImgsByProductId(Integer productId) {
        return prdImgRepository.getPrdImgByProductId(productId);
    }
    
    @Override
    public List<Product> getTopRatedProducts(int count) {
        // 調用 Repository 方法獲取星數排名前幾名的商品
        return productRepository.findTopRatedProducts(3);
    }
    
    //商品分頁
    public Page<Product> getPaginatedProducts(int page, int size, String sortBy) {
        // 創建包含排序條件的 Pageable 物件
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

        // 使用 findAll(Pageable) 來獲取分頁和排序的資料
        return productRepository.findAll(pageable);
    }


}



