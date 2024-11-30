package com.artogether.common.business_perm;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusinessPermRepo extends JpaRepository<BusinessPerm, BusinessPerm.BusinessPermComposite> {

    List<BusinessPerm> findBusinessPermsByMember_Id(Integer memberId);

    List<BusinessPerm> findBusinessPermsByBusinessMember_Id(Integer businessMemberId);
}
