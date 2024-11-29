package com.artogether.product.mall;


import java.util.List;

import com.artogether.product.product.*;
import com.artogether.product.prd_catalog.*;


public interface MallDao {

    // 1. 查看所有商品
    List<Product> findAllProducts(); // 查詢所有商品
    List<Product> findAllProductsWithPagination(int offset, int limit); // 支援分頁的查詢

    // 2. 搜尋商品
    List<Product> searchProductsByName(String keyword); // 模糊搜尋商品名稱
    List<Product> searchProductsByNameAndCategory(String keyword, Integer categoryId); // 搜尋商品名稱並篩選分類

    // 3. 商品分類
    List<PrdCatalog> findAllCategories(); // 查詢所有商品分類

    // 4. 商品價格排序
    List<Product> findAllProductsSortedByPrice(String order); // 按價格排序商品（ASC 或 DESC）

    // 5. 查詢商品及其主圖片 
    List<Object[]> findAllProductsWithMainImages(); // 返回商品及主圖片的數據
  }

