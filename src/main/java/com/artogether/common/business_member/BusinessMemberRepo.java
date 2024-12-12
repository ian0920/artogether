package com.artogether.common.business_member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface BusinessMemberRepo extends JpaRepository<BusinessMember, Integer>, 
											JpaSpecificationExecutor<BusinessMember> 
{
}
