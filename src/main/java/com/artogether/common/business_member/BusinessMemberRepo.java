package com.artogether.common.business_member;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface BusinessMemberRepo extends JpaRepository<BusinessMember, Integer>, 
											JpaSpecificationExecutor<BusinessMember> 
{
	@Query(value = "SELECT * FROM business_member ORDER BY RAND() LIMIT :count", nativeQuery = true)
	List<BusinessMember> findRandomBusinessMembers(@Param("count") int count);	
}
