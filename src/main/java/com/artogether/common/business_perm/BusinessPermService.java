package com.artogether.common.business_perm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
