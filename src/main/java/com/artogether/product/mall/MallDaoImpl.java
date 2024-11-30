package com.artogether.product.mall;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.artogether.product.product.Product;
import com.artogether.product.prd_catalog.PrdCatalog;

@Repository
public class MallDaoImpl implements MallDao {

    @Autowired
    private EntityManagerFactory sessionFactory;

    // 1. 查看所有商品
    @Override
    public List<Product> findAllProducts() {
        Session session = sessionFactory.unwrap(Session.class);
        String hql = "FROM Product WHERE status = 1"; // 只查詢上架的商品
        return session.createQuery(hql, Product.class).list();
    }

    @Override
    public List<Product> findAllProductsWithPagination(int offset, int limit) {
        Session session = sessionFactory.unwrap(Session.class);
        String hql = "FROM Product WHERE status = 1"; // 只查詢上架的商品
        return session.createQuery(hql, Product.class)
                      .setFirstResult(offset) // 設置起始位置
                      .setMaxResults(limit)   // 設置最大結果數
                      .list();
    }

    // 2. 搜尋商品
    @Override
    public List<Product> searchProductsByName(String keyword) {
        Session session = sessionFactory.unwrap(Session.class);
        String hql = "FROM Product WHERE name LIKE :keyword AND status = 1"; // 模糊搜尋上架的商品
        return session.createQuery(hql, Product.class)
                      .setParameter("keyword", "%" + keyword + "%")
                      .list();
    }

    @Override
    public List<Product> searchProductsByNameAndCategory(String keyword, Integer categoryId) {
        Session session = sessionFactory.unwrap(Session.class);
        String hql = "FROM Product WHERE name LIKE :keyword AND catalog.id = :categoryId AND status = 1"; // 搜尋並篩選分類
        return session.createQuery(hql, Product.class)
                      .setParameter("keyword", "%" + keyword + "%")
                      .setParameter("categoryId", categoryId)
                      .list();
    }

    // 3. 商品分類
    @Override
    public List<PrdCatalog> findAllCategories() {
        Session session = sessionFactory.unwrap(Session.class);
        String hql = "FROM PrdCatalog";
        return session.createQuery(hql, PrdCatalog.class).list();
    }

    // 4. 商品價格排序
    @Override
    public List<Product> findAllProductsSortedByPrice(String order) {
        Session session = sessionFactory.unwrap(Session.class);
        String hql = "FROM Product WHERE status = 1 ORDER BY price " + (order.equalsIgnoreCase("ASC") ? "ASC" : "DESC");
        return session.createQuery(hql, Product.class).list();
    }

    // 5. 查詢商品及其主圖片
    @Override
    public List<Object[]> findAllProductsWithMainImages() {
        Session session = sessionFactory.unwrap(Session.class);
        String hql = "SELECT p.id, p.name, p.price, i.imageFile " +
                     "FROM Product p " +
                     "LEFT JOIN PrdImg i ON p.id = i.prdId AND i.isMain = true " +
                     "WHERE p.status = 1"; // 僅查詢上架的商品
        return session.createQuery(hql).list();
    }
}

