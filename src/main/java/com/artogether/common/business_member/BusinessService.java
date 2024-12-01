package com.artogether.common.business_member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BusinessService {

    @Autowired
    BusinessMemberRepo businessMemberRepo;

    //null handling not be done
    public BusinessMember findById(Integer id) {
        return businessMemberRepo.findById(id).orElse(null);
    }

    public List<BusinessMember> findAll() {
        return businessMemberRepo.findAll();
    }

    //沒有就新增 有就修改
    public BusinessMember save(BusinessMember businessMember) {
        return businessMemberRepo.save(businessMember);
    }


    //0→申請中(預設) 1→通過 2→停權
    public void statusUpdate(BusinessMember businessMember) {

        BusinessMember bm = findById(businessMember.getId());
        bm.setStatus(businessMember.getStatus());
        businessMemberRepo.save(bm);

    }









}
