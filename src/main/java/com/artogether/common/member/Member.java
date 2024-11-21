package com.artogether.common.member;

import com.artogether.event.evt_order.EvtOrder;
import com.artogether.event.evt_track.EvtTrackVO;
import com.artogether.event.my_evt_coup.MyEvtCoup;
import com.artogether.product.cart.Cart;
import com.artogether.product.my_prd_coup.My_Prd_Coup;
import com.artogether.product.prd_order.Prd_Order;
import com.artogether.product.prd_report.Prd_Report;
import com.artogether.product.prd_review.Prd_Review;
import com.artogether.product.prd_track.Prd_Track;
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
import java.sql.Date;
import java.sql.Timestamp;
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
    private String phone;

    @Column(nullable = false)
    @NotBlank(message = "密碼請勿空白")
    private String password;
    private Byte status;

    @Column(name = "oath_provider")
    private String oathProvider;

    @Column(name = "oath_user_id")
    private String oathUserId;


    //以下為Hibernate 一對多屬性

    /*    Product    */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member", cascade = CascadeType.ALL)
    private Set<Prd_Track> prdTracks;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member", cascade = CascadeType.ALL)
    private Set<Prd_Review> prdReviews;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member", cascade = CascadeType.ALL)
    private Set<Prd_Report> prdReports;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member", cascade = CascadeType.ALL)
    private Set<Prd_Order> prdOrders;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member", cascade = CascadeType.ALL)
    private Set<My_Prd_Coup> myPrdCoups;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member", cascade = CascadeType.ALL)
    private Set<Cart> carts;

    /*    Venue    */

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member", cascade = CascadeType.ALL)
    private Set<VneOrder> vneOrders;


    /*    Event    */

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member", cascade = CascadeType.ALL)
    private Set<EvtTrackVO> evtTracks;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member", cascade = CascadeType.ALL)
    private Set<EvtOrder> evtOrders;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member", cascade = CascadeType.ALL)
    private Set<MyEvtCoup> myEvtCoups;

}
