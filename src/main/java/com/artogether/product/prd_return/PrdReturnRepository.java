package com.artogether.product.prd_return;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



@Repository
public interface PrdReturnRepository extends JpaRepository<PrdReturn, Integer> {
	
	 List<PrdReturn> findByIdAndPrdOrder_IdAndReasonAndTypeAndStatusGreaterThanEqual(
	            Integer id, Integer orderId, String reason, Integer type, Integer status);

	 List<PrdReturn> findByPrdOrder_Id(Integer orderId);
	 
	 //退換貨依據商家會員id顯示
	 @Query
	 (value = "SELECT r.*, od.*, p.* " +
			 "FROM prd_return r " +
			 "JOIN prd_order_detail od ON r.order_id = od.order_id " +
			 "JOIN product p ON od.prd_id = p.id " +
			 "WHERE p.business_id = :businessMemberId"
, 
     nativeQuery = true)
	 List<PrdReturn> findReturnsByBusinessMemberId(@Param("businessMemberId") Integer businessMemberId);
	
	 //退換貨依據一般會員id顯示
	 @Query(value = "SELECT r.* " +
             "FROM prd_return r " +
             "JOIN prd_order o ON r.order_id = o.id " +
             "WHERE o.member_id = :memberId", 
     nativeQuery = true)
List<PrdReturn> findReturnsByMemberId(@Param("memberId") Integer memberId);
}
