package com.artogether.product.prd_return;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrdReturnRepository extends JpaRepository<PrdReturn, Integer> {
}
