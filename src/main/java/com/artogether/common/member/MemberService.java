package com.artogether.common.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {

    @Autowired
    private MemberRepo memberRepo;

    //null handling not be done
    public Member findById(int id) {
        return memberRepo.findById(id).orElse(new Member());
    }

    public Member findByEmail(String email) {
        return memberRepo.findByEmail(email);
    }

    public List<Member> findAll() {
        return memberRepo.findAll();
    }

    public Member save(Member member) {

        if(member.getStatus() == null)
            member.setStatus((byte) 0);

        return memberRepo.save(member);
    }

    public void statusUpdate(Member member) {
        Member m = findById(member.getId());
        m.setStatus(member.getStatus());
        memberRepo.save(m);
    }
}
