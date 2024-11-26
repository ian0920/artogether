package com.artogether.product.prd_coup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrdCoupRepository extends JpaRepository<PrdCoup, Integer> {
}
