package com.artogether.product.prd_review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrdReviewRepository extends JpaRepository<PrdReview, Integer> {
}
