package com.artogether.common.platform_msg;

import com.artogether.common.business_member.BusinessMember;
import com.artogether.common.business_member.BusinessMemberRepo;
import com.artogether.common.member.Member;
import com.artogether.common.member.MemberRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class PlatformMsgService {

    @Autowired
    private PlatformMsgRepository platformMsgRepo;

    @Autowired
    private MemberRepo memberRepo;

    @Autowired
    private BusinessMemberRepo businessMemberRepo;

    //新增平台訊息
    public void add(PlatformMsg platform_msg){
        platformMsgRepo.save(platform_msg);
    }

    public void update(PlatformMsg platform_msg){
        platformMsgRepo.save(platform_msg);
    }

    public void delete(PlatformMsg platform_msg){
        platformMsgRepo.delete(platform_msg);
    }

    public List<PlatformMsg> findAll(){
        return platformMsgRepo.findAll();
    }

    // 發送訊息給單個會員
    public void sendToOne(PlatformMsg platformMsg) {
        Member member = memberRepo.findById(platformMsg.getMember().getId()).orElse(null);
        if (member != null) {
            PlatformMsg platformMsgId = new PlatformMsg();
            platformMsgId.setMember(member);
            platformMsgId.setMessage(platformMsg.getMessage());
            platformMsgId.setMsgTime(new Timestamp(System.currentTimeMillis()));
            platformMsgId.setStatus((byte) 0);
            platformMsgRepo.save(platformMsg);
        }
    }

    // 發送訊息給所有會員
    public void sendToAll(String message) {
        List<Member> members = memberRepo.findAll();
        for (Member member : members) {
            PlatformMsg platformMsg = new PlatformMsg();
            platformMsg.setMember(member);
            platformMsg.setMessage(message);
            platformMsg.setMsgTime(new Timestamp(System.currentTimeMillis()));
            platformMsg.setStatus((byte) 0);
            platformMsgRepo.save(platformMsg);
        }
    }

    // 發送訊息給單個商家
    public void sendToOneBusiness(PlatformMsg platformMsg) {
        BusinessMember businessMember = businessMemberRepo.findById(platformMsg.getBusinessMember().getId()).orElse(null);
        if (businessMember != null) {
            PlatformMsg platformMsgB = new PlatformMsg();
            platformMsgB.setBusinessMember(businessMember);
            platformMsgB.setMessage(platformMsg.getMessage());
            platformMsgB.setMsgTime(new Timestamp(System.currentTimeMillis()));
            platformMsgB.setStatus((byte) 0);
            platformMsgRepo.save(platformMsg);
        }

        // 發送訊息給所有商家
//        public void sendToAllBusinesses(String message) {
//            List<BusinessMember> businessMembers = businessMemberRepo.findAll();
//            for (BusinessMember businessMember : businessMembers) {
//                PlatformMsg platformMsg = new PlatformMsg();
//                platformMsg.setBusinessMember(businessMember);
//                platformMsg.setMessage(message);
//                platformMsg.setMsgTime(new Timestamp(System.currentTimeMillis()));
//                platformMsg.setStatus((byte) 0);
//                platformMsgRepo.save(platformMsg);
//            }
//        }

    }


}
