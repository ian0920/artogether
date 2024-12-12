package com.artogether.product.prd_return;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface PrdReturnRepository extends JpaRepository<PrdReturn, Integer> {
	
	 List<PrdReturn> findByIdAndPrdOrder_IdAndReasonAndTypeAndStatusGreaterThanEqual(
	            Integer id, Integer orderId, String reason, Integer type, Integer status);

	 List<PrdReturn> findByPrdOrder_Id(Integer orderId);
	
}
