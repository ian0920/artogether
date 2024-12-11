package com.artogether.product.prd_coup;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrdCoupRepository extends JpaRepository<PrdCoup, Integer> {
	
	    List<PrdCoup> findByBusinessMemberId(Integer businessMemberId);
	    List<PrdCoup> findByBusinessMemberIdAndNameContainingAndTypeAndStatusAndThresholdGreaterThanEqual(
	    	Integer	businessMemberId, String name, Integer type, Integer status, Integer threshold);
	

}
