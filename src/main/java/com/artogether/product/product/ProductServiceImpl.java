package com.artogether.product.product;

import com.artogether.common.business_member.BusinessMember;
import com.artogether.common.business_member.BusinessMemberRepo;
import com.artogether.product.cart.model.PrdCoupForCartDTO;
import com.artogether.product.my_prd_coup.MyPrdCoup;
import com.artogether.product.my_prd_coup.MyPrdCoupRepository;
import com.artogether.product.prd_catalog.PrdCatalogRepository;
import com.artogether.product.prd_img.PrdImg;
import com.artogether.product.prd_img.PrdImgRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;
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

    @Transactional
    @Override
    public Product updateProduct(Integer id, Product updatedProduct, List<MultipartFile> images, HttpSession session) throws IOException {
        // 獲取當前登錄的商家
        BusinessMember businessMember = (BusinessMember) session.getAttribute("presentBusinessMember");
        if (businessMember == null) {
            throw new RuntimeException("未登入或商家信息無法獲取");
        }

        // 根據 ID 查找商品
        Optional<Product> existingProductOptional = productRepository.findById(id);
        if (existingProductOptional.isEmpty()) {
            throw new RuntimeException("商品 ID: " + id + " 不存在");
        }

        Product existingProduct = existingProductOptional.get();

        // 更新商品基本信息
        existingProduct.setName(updatedProduct.getName());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setQty(updatedProduct.getQty());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setStatus(updatedProduct.getStatus());
        existingProduct.setPrdCatalog(updatedProduct.getPrdCatalog());
        existingProduct.setBusinessMember(businessMember);

        // 處理圖片邏輯
        List<PrdImg> existingImages = prdImgRepository.getPrdImgByProductId(existingProduct.getId());
        if (images!= null && !images.isEmpty()) {
            // 刪除舊圖片
            for (PrdImg img : existingImages) {
                prdImgRepository.deleteAllByProductId(img.getId());
            }
            prdImgRepository.deleteAll(existingImages);

            // 保存新圖片
            List<PrdImg> newPrdImgs = new ArrayList<>();
            for (MultipartFile image : images) {
                PrdImg prdImg = new PrdImg();
                prdImg.setProduct(existingProduct);
                prdImg.setImageFile(image.getBytes());
                newPrdImgs.add(prdImg);
            }
            prdImgRepository.saveAll(newPrdImgs);
        }

        // 保存商品
        return productRepository.save(existingProduct);
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
            // 刪除與商品相關的圖片
            List<PrdImg> images = prdImgRepository.getPrdImgByProductId(id);
            if (images != null && !images.isEmpty()) {
                prdImgRepository.deleteAll(images);
            }
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
        setProductsImg(products);
        return products;
    }

    public void setProductsImg(List<Product> products) {
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

    public Map<Integer, List<PrdCoupForCartDTO>> findAllBusinessMember(List<Integer> productIdInCart, Integer memberId) {

        Set<BusinessMember> businessMembers = new HashSet<>();

        //從購物車的所有商品ID找出所有商家
        productIdInCart.stream().map(id -> productRepository.findById(id).get().getBusinessMember())
                .forEach(businessMembers::add);


        //找出此會員的所有優惠券
        myPrdCoupRepository.findAllByMember_Id(memberId);
        List<MyPrdCoup> allMyPrdCoups = myPrdCoupRepository.findAllByMember_Id(memberId);


        //Map<BusinessId, PrdCoupDTO> 該會員對於該商家所擁有的優惠券
        Map<Integer, List<PrdCoupForCartDTO>> map = new HashMap<>();

        businessMembers.forEach(businessMember -> {
            map.put(businessMember.getId(), allMyPrdCoups.stream().filter(coup -> coup.getPrdCoup().getBusinessMember().getId() == businessMember.getId())
                    .map(coup -> PrdCoupForCartDTO.prdCoupForCartDTOTransformer(coup.getPrdCoup(), coup)).toList());
        });


        return map;
    }


}



