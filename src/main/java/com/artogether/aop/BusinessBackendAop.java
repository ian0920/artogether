package com.artogether.aop;


import com.artogether.common.business_member.BusinessMember;
import com.artogether.common.business_perm.BusinessPerm;
import com.artogether.common.business_perm.BusinessPermAnn;
import com.artogether.common.business_perm.BusinessPermRepo;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

@Aspect
@Component
public class BusinessBackendAop {

    @Autowired
    private BusinessPermRepo businessPermRepo;

    @Autowired
    private HttpSession httpSession;


    @Around("@annotation(businessPermAnn)")
    public Object checkPermission(ProceedingJoinPoint joinPoint, BusinessPermAnn businessPermAnn) throws Throwable {


        Integer memberId = (Integer) httpSession.getAttribute("member");

        BusinessMember businessMember = (BusinessMember) httpSession.getAttribute("presentBusinessMember");
        if (businessMember == null) {
            throw new RuntimeException("尚未登入商家後台");
        }

        BusinessPerm businessPerm = businessPermRepo.findByBusinessMember_IdAndMember_Id(businessMember.getId(),memberId);


        String perm = businessPermAnn.value();
        boolean hasPermission = false;

        switch (perm.toUpperCase()) {
            case "PRD":
                hasPermission = businessPerm.isPrdPerm();
                break;
            case "EVENT":
                hasPermission = businessPerm.isEvtPerm();
                break;
            case "VENUE":
                hasPermission = businessPerm.isVnePerm();
                break;
            case "ADMIN":
                hasPermission = businessPerm.isAdminPerm();
                break;
            default:
                throw new IllegalArgumentException("無此權限: " + perm);
        }


        if (!hasPermission) {
            throw new RuntimeException("沒有相關訪問權限");
        }

        return joinPoint.proceed();
    }


}
