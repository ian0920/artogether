package com.artogether.common.member;

import com.artogether.util.MailManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindException;

import java.util.List;

@Service
public class MemberService {

    @Autowired
    private MemberRepo memberRepo;

    @Autowired
    private MailManager mailManager;

    //null handling not be done
    public Member findById(int id) {
        return memberRepo.findById(id).orElse(null);
    }

    public Member findByEmail(String email) {
        return memberRepo.findByEmail(email);
    }

    public List<Member> findAll() {
        return memberRepo.findAll();
    }

    public Member save(Member member) {

        return memberRepo.save(member);
    }

    public void statusUpdate(Member member) {
        Member m = findById(member.getId());
        m.setStatus(member.getStatus());
        memberRepo.save(m);
    }

    //會員註冊
    public Member register(Member member) throws BindException {
        /*
            填寫資料驗證v
            錯誤驗證回傳v
            驗證信(帳號啟用)
            驗證碼(忘記密碼)
        */

        Member m  = Member.builder()
                .name(member.getName())
                .email(member.getEmail())
                .birthday(member.getBirthday())
                .gender(member.getGender())
                .prefer(member.getPrefer())
                .phone(member.getPhone())
                .password(member.getPassword())
                .status((byte) 0)
                .build();

        //Email重複的錯誤處理
        if(memberRepo.findByEmail(member.getEmail()) != null){
            throw new BindException(member, "emailDuplicate");
        }

        //寄送註冊成功信件
        Member savedMember = memberRepo.save(m);
        Integer memberId = savedMember.getId();
        mailManager.sendRegisterSuccessMail(member.getName(), member.getEmail(), memberId);

        return savedMember;
    }

    //會員帳號啟用
    public Member memberVerify(Integer id) {

        //Controller 已篩選過傳過來的id
        //getReferenceById = lazy initialization
        Member m = memberRepo.getReferenceById(id);

        m.setStatus((byte) 1);
        return memberRepo.save(m);

    }


}
