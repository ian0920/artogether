package com.artogether.product.prd_img;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrdImgRepository extends JpaRepository<PrdImg, Integer> {
}
