package com.artogether.product.prd_img;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface PrdImgRepository extends JpaRepository<PrdImg, Integer> {


    @Query("SELECT p FROM PrdImg p WHERE p.product.id = ?1")
    List<PrdImg> getPrdImgByProductId(Integer id);

    
   
}
