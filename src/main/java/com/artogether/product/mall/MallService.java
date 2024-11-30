package com.artogether.product.mall;

import java.util.List;

import com.artogether.product.product.Product;
import com.artogether.product.prd_catalog.PrdCatalog;

public interface MallService {

    // 1. 查看所有商品
    List<Product> getAllProducts(); // 查詢所有商品
    List<Product> getAllProductsWithPagination(int offset, int limit); // 分頁查詢商品

    // 2. 搜尋商品
    List<Product> searchProducts(String keyword); // 搜尋商品名稱
    List<Product> searchProductsByCategory(String keyword, Integer categoryId); // 搜尋商品名稱並篩選分類

    // 3. 商品分類
    List<PrdCatalog> getAllCategories(); // 獲取所有商品分類

    // 4. 商品價格排序
    List<Product> getAllProductsSortedByPrice(String order); // 按價格排序商品
    

    // 5. 查詢商品及其主圖片
    List<Object[]> getAllProductsWithMainImages(); // 獲取商品及其主圖片的數據

}

