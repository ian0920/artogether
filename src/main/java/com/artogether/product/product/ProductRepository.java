package com.artogether.product.product;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.List;


@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {


    @Query("SELECT p FROM Product p WHERE p.businessMember.id = ?1")
    List<Product> findProductListByBusinessId(Integer businessId);
    
    @Query(value = "SELECT * FROM product ORDER BY all_stars DESC LIMIT :count", nativeQuery = true)
    List<Product> findTopRatedProducts(@Param("count") int count);


}
