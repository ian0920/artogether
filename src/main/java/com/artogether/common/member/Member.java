package com.artogether.common.member;

import com.artogether.common.business_perm.BusinessPerm;
import com.artogether.common.platform_msg.PlatformMsg;
import com.artogether.event.evt_order.EvtOrder;

import com.artogether.event.evt_track.EvtTrack;

import com.artogether.event.my_evt_coup.MyEvtCoup;
import com.artogether.product.cart.model.Cart;
import com.artogether.product.my_prd_coup.MyPrdCoup;
import com.artogether.product.prd_order.model.PrdOrder;
import com.artogether.product.prd_report.PrdReport;
import com.artogether.product.prd_review.PrdReview;
import com.artogether.product.prd_track.PrdTrack;
import com.artogether.venue.vne_track.VneTrack;
import com.artogether.venue.vneorder.VneOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "member")
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(nullable = false)
    @NotBlank(message = "姓名請勿空白")
    private String name;

    @Column(nullable = false, unique = true)
    @Email(message = "Email格式有誤")
    @NotBlank(message = "Email請勿空白")
    private String email;

    @Column(name = "register_date", updatable = false, insertable = false)
    @CreationTimestamp
    private Timestamp registerDate;

    @Past(message = "生日日期需早於今天")
    private Date birthday;
    private String gender;
    private String prefer;

    @Length(min=10,max=10, message = "手機號碼長度有誤")
    @Pattern(regexp = "^09\\d{8}$", message = "手機號碼需為09開頭的10位數字")
    private String phone;

    @Column(nullable = false)
    @NotBlank(message = "密碼請勿空白")
    @Length(min=8, message = "密碼長度不足，至少8位數密碼以上")
    private String password;

    //0->驗證中 1->啟用 2->停用
    private Byte status;

    @Column(name = "oath_provider")
    private String oathProvider;

    @Column(name = "oath_user_id")
    private String oathUserId;


    //以下為Hibernate 一對多屬性

    /*    Product    */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member", cascade = CascadeType.ALL)
    private Set<PrdTrack> prdTracks;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member", cascade = CascadeType.ALL)
    private Set<PrdReview> prdReviews;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member", cascade = CascadeType.ALL)
    private Set<PrdReport> prdReports;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member", cascade = CascadeType.ALL)
    private Set<PrdOrder> prdOrders;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member", cascade = CascadeType.ALL)
    private Set<MyPrdCoup> myPrdCoups;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member", cascade = CascadeType.ALL)
    private Set<Cart> carts;

    /*    Venue    */

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member", cascade = CascadeType.ALL)
    private Set<VneOrder> vneOrders;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member", cascade = CascadeType.ALL)
    private Set<VneTrack> vneTracks;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member", cascade = CascadeType.ALL)
    private Set<PlatformMsg> platformMsgs;


    /*    Event    */


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member", cascade = CascadeType.ALL)
    private Set<EvtTrack> evtTracks;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member", cascade = CascadeType.ALL)
    private Set<EvtOrder> evtOrders;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member", cascade = CascadeType.ALL)
    private Set<MyEvtCoup> myEvtCoups;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member", cascade = CascadeType.ALL)
    private Set<BusinessPerm> businessPerms;

    public int hashCode() {
        return Objects.hash(this.id);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj != null && this.getClass() == obj.getClass()) {
            Member that = (Member)obj;
            return Objects.equals(this.id, that.id);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", registerDate=" + registerDate +
                ", birthday=" + birthday + "}";

    }

}
