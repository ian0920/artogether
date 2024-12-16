package com.artogether.common.business_perm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.artogether.common.business_member.BusinessMember;
import com.artogether.common.business_perm.BusinessPerm.BusinessPermComposite;
import com.artogether.common.member.Member;

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
    
    public BusinessPerm register(Member member, BusinessMember bMember) {
		BusinessPerm bPerm = BusinessPerm.builder().businessMember(bMember).member(member).adminPerm(true).evtPerm(true).vnePerm(true).prdPerm(true).status(Byte.valueOf((byte) 0)).build();
		bPerm.setBusinessPermComposite(new BusinessPermComposite(bMember.getId(), member.getId()));
		return repo.save(bPerm);
	}
}
