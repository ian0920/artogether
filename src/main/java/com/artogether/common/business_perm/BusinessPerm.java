package com.artogether.common.business_perm;

import com.artogether.common.business_member.BusinessMember;
import com.artogether.common.member.Member;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "business_perm")
@IdClass(BusinessPerm.BusinessPermComposite.class)
public class BusinessPerm {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", referencedColumnName = "id")
    @JsonIgnore
    private BusinessMember businessMember;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    @JsonIgnore
    private Member member;

    @Column(name = "prd_perm")
    private boolean prdPerm;

    @Column(name = "event_perm")
    private boolean evtPerm;

    @Column(name = "venue_perm")
    private boolean vnePerm;
    
    @Column(name = "admin_perm")
    private boolean adminPerm;

    @Column(name = "assign_date", updatable = false, insertable = false)
    private Timestamp assignDate;

    private Byte status;

    public BusinessPermComposite getBusinessPermComposite() {
        return new BusinessPermComposite(businessMember.getId(), member.getId());
    }

    public void setBusinessPermComposite(BusinessPermComposite businessPermComposite) {
        this.businessMember.setId(businessPermComposite.businessMember);
        this.member.setId(businessPermComposite.member);
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BusinessPermComposite implements Serializable {
        private static final long serialVersionUID = 1L;
        Integer businessMember;
        Integer member;
    }

}
