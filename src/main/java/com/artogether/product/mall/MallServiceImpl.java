package com.artogether.product.mall;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.artogether.product.prd_catalog.PrdCatalog;
import com.artogether.product.product.Product;

public class MallServiceImpl implements MallService {
	
	@Autowired
	private MallDao mallDao;
	
	@Override
	public List<Product>getAllProducts() {
		try {
			return mallDao.findAllProducts();
			
		}catch (Exception e) {
            e.printStackTrace();
            return null;
		
		}
	}
	
	@Override
    public List<Product> getAllProductsWithPagination(int offset, int limit) {
        try {
            return mallDao.findAllProductsWithPagination(offset, limit); // 分頁查詢商品
        } catch (Exception e) {
            e.printStackTrace();
            return null; // 返回空結果以處理異常情況
        }
    }

    @Override
    public List<Product> searchProducts(String keyword) {
        try {
            return mallDao.searchProductsByName(keyword); // 模糊搜尋商品名稱
        } catch (Exception e) {
            e.printStackTrace();
            return null; // 返回空結果以處理異常情況
        }
    }

    @Override
    public List<Product> searchProductsByCategory(String keyword, Integer categoryId) {
        try {
            return mallDao.searchProductsByNameAndCategory(keyword, categoryId); // 搜尋名稱並篩選分類
        } catch (Exception e) {
            e.printStackTrace();
            return null; // 返回空結果以處理異常情況
        }
    }

    @Override
    public List<PrdCatalog> getAllCategories() {
        try {
            return mallDao.findAllCategories(); // 查詢所有商品分類
        } catch (Exception e) {
            e.printStackTrace();
            return null; // 返回空結果以處理異常情況
        }
    }

    @Override
    public List<Product> getAllProductsSortedByPrice(String order) {
        try {
            return mallDao.findAllProductsSortedByPrice(order); // 按價格排序商品
        } catch (Exception e) {
            e.printStackTrace();
            return null; // 返回空結果以處理異常情況
        }
    }


    @Override
    public List<Object[]> getAllProductsWithMainImages() {
        try {
            return mallDao.findAllProductsWithMainImages(); // 查詢商品及其主圖片
        } catch (Exception e) {
            e.printStackTrace();
            return null; // 返回空結果以處理異常情況
        }
    }
	
	
	
	
	
	
	
	
	
	
	
	
}
