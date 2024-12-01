package com.artogether.common.business_perm;

import com.artogether.common.business_member.BusinessMember;
import com.artogether.common.member.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "business_perm")
@IdClass(BusinessPerm.BusinessPermComposite.class)
public class BusinessPerm {

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "business_id", referencedColumnName = "id")
    private BusinessMember businessMember;

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;

    @Column(name = "prd_perm")
    private boolean prdPerm;

    @Column(name = "event_perm")
    private boolean evtPerm;

    @Column(name = "venue_perm")
    private boolean vnePerm;

    @Column(name = "assign_date")
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
    static class BusinessPermComposite implements Serializable {
        private static final long serialVersionUID = 1L;
        Integer businessMember;
        Integer member;
    }

}
