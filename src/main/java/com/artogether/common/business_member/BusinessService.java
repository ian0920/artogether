package com.artogether.common.business_member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.artogether.event.event.Event;
import com.artogether.event.event.EventSpecifications;

import java.util.List;
import java.util.Map;

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


    // 複合查詢+分頁
    public Page<BusinessMember> searchEvents(Map<String, String> searchCriteria, Pageable pageable) {
        Specification<BusinessMember> spec = BMemberSpecification.dynamicQuery(searchCriteria);
        return businessMemberRepo.findAll(spec, pageable);
    }
    
    // 首頁隨機顯示商家
    public List<BusinessMember> getRandomBusinessMembers() {
        return businessMemberRepo.findRandomBusinessMembers(3);
    }


}
