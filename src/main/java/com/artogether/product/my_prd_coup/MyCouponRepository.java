package com.artogether.product.my_prd_coup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MyCouponRepository extends JpaRepository<My_Prd_Coup, My_Prd_Coup.CompositeCoup> {
}
