package com.artogether.common.business_perm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BusinessPermService {
    @Autowired
    private BusinessPermRepo repo;


    public List<BusinessPerm> getPermsByMember(Integer memberId) {
        return repo.findBusinessPermsByMember_Id(memberId);
    }

    public List<BusinessPerm> getAllByBusinessMember(Integer businessMemberId) {
        return repo.findBusinessPermsByBusinessMember_Id(businessMemberId);
    }
    
    public List<BusinessPerm> saveAll(List<BusinessPerm> bPermList){
    	return repo.saveAll(bPermList);
    }
    
    public BusinessPerm findByPK(Integer memberId, Integer businessMemberId) {
        BusinessPerm.BusinessPermComposite compositeId = new BusinessPerm.BusinessPermComposite(businessMemberId, memberId);
        return repo.findById(compositeId).orElse(new BusinessPerm());
	}
}
